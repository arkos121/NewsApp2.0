package com.example.newp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.newp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.newp.apis.RetrofitClient
import com.example.newp.apis.WeatherResponse
import com.example.newp.db.NewsDb
import com.example.newp.utils.convertMillisToTimeInIST
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherService = RetrofitClient.apiService
    private lateinit var adapter: MyAdapter
    private val items: MutableList<CardItem> = mutableListOf()
    lateinit var logout: Button
    private lateinit var auth: FirebaseAuth
    var lastclicked : String ?=""
    private var newsJobs : Job ?=null
    private var newsJob : Job ?= null
    private var isNewsLoaded = false

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val recyclerView: RecyclerView = binding.recyclelayout
        adapter = MyAdapter(items)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        logout = binding.logouts
        Log.d("MainActivity", "onCreate: Activity started")  // Log onCreate
        click()
        cardviews()

        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, SplashScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
    private fun cardviews() {
        binding.cardView.visibility = View.GONE

        binding.checks.setOnClickListener {
            if (binding.cardView.visibility == View.GONE) {
                binding.cardView.visibility = View.VISIBLE
                // Only fetch if we have a valid state
                if (lastclicked?.isNotEmpty() == true) {
                    fetchStateNews(lastclicked?:"")
                }
            } else {
                binding.cardView.visibility = View.GONE
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun click() {
        val webView: WebView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/indianmap.html")

        webView.addJavascriptInterface(WebAppInterface(this) { state ->
            Log.d("MainActivity", "State clicked: $state")
            state?.let {
                lastclicked = it
                binding.textviews.text = "${lastclicked} News"
                // Reset news loaded flag when new state is clicked
                isNewsLoaded = false
                // If card is visible, update news immediately
                if (binding.cardView.visibility == View.VISIBLE) {
                    fetchStateNews(lastclicked?:"")
                }
                // Call weather API
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        var weatherData: WeatherResponse? = null
                        var diesel : String ?= null
                        var petrol : String? = null
                        withContext(Dispatchers.IO) {
                            weatherData = makeApiCall(it)
                            petrol = fuelprice(it).first
                            diesel = fuelprice(it).second
                        }
                        adapter.updateData(weatherData,it,petrol,diesel)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error in API call: ${e}")
                    }
                }
            }
        }, "Android")
    }
    // Make the API call for weather ata
    private suspend fun makeApiCall(state: String): WeatherResponse {
        Log.d(
            "MainActivity",
            "makeApiCall: Making API call for state $state"
        )  // Log API call attempt
        return try {
            val weatherResponse = weatherService.getWeatherByCity(getCapital(state))
            Log.d(
                "WeatherResponse",
                "makeApiCall: Response received: $weatherResponse"
            )  // Log successful API response
            weatherResponse
        } catch (e: Exception) {
            Log.e("MainActivity", "makeApiCall: Exception: ${e.message}")  // Log exception
            e.printStackTrace()
            // Return a default weather response on error
            WeatherResponse(
                emptyList(),
                WeatherResponse.MainWeatherData(0.0, 0.0, 0.0, 0.0, 0, 0),
                WeatherResponse.WindData(0.0, 0),
                WeatherResponse.CloudData(0),
                ""
            )
        }
    }
    fun fuelprice(state: String): Pair<String,String> {
        // Initialize Python instance
        val python = Python.getInstance()

        try {
            // Call the Python function to fetch the petrol price for the given state
            val pythonScript = python.getModule("petrol") // The Python file name without the .py extension
            val result: PyObject = pythonScript.callAttr("fetch_fuel_prices_by_state", state)
            val k = result.asList()

            // Assuming the result contains two elements (petrol and diesel prices)
            if (k.size >= 2) {
                val petrolPrice = k[0].toString() // First element is petrol price
                val dieselPrice = k[1].toString() // Second element is diesel price

                return Pair(petrolPrice,dieselPrice)
                Log.d("Fuel Prices", "Petrol: $petrolPrice, Diesel: $dieselPrice for $state")
            } else {
                Log.d("Fuel Prices", "Invalid result or missing prices for $state")
            }

            // Check if the result is valid and log the price
//            if (petrolp != "None" && diselp != "None") {
//                Log.d("Fuel Prices", "Petrol: $petrolp, Diesel: $diselp for $state")
//            } else {
//                Log.d("Fuel Prices", "No data found for $state")
//            }
        } catch (e: Exception) {
            Log.e("Error", "Error fetching data: ${e.message}")
        }

        // Return the price (or empty string if no price found)
        return Pair("","")
    }

private var timestamp : Long = System.currentTimeMillis()
fun fetchStateNews(state: String) {
    val newsDao = NewsDb.getDatabase(this).newsDao()
    timestamp = System.currentTimeMillis()
    val expiry = timestamp - (12 * 60 * 60 * 1000)  // 24 hours in milliseconds

    // Cancel any existing coroutine job
    newsJob?.cancel()

    CoroutineScope(Dispatchers.IO + Job()).launch {
        try {
            // Check if cached news exists for the given state
            val cachedNews = newsDao.getNews(state)
            // If cached news exists, display it
            if (cachedNews.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    binding.datas.text = "Latest News for $state: Last updated on :${convertMillisToTimeInIST(newsDao.gettimestamp(state))}\n\n"

                    cachedNews.forEachIndexed { index, newsItem ->
                        if (newsItem.description.isNotBlank()) {
                            binding.datas.append("${index + 1}. ${newsItem.description}\n\n")
                            Log.d("StateNews", "Cached News $index for $state: ${newsItem.description}")
                        }
                    }
                }
            } else {
                // If no cached news, proceed to fetch fresh data from Python script
                withContext(Dispatchers.Main) {
                    binding.datas.text = "Loading news for $state..."
                }

                // Initialize Chaquopy if not already initialized
                if (!Python.isStarted()) {
                    Python.start(AndroidPlatform(this@MainActivity))
                }

                // Get Python instance and module
                val python = Python.getInstance()
                val pyObj = python.getModule("import_req")

                // Call Python function to fetch fresh news
                val result = pyObj.callAttr("get_news_for_state", state)

                // Switch to main thread for UI updates
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        binding.datas.text = ""

                        val newsList = result.asList()
                        if (newsList.isNotEmpty()) {
                            binding.datas.append("Latest News for $state:\n\n")

                            // Display each news item and save it to the database
                            newsList.forEachIndexed { index, newsItem ->
                                val newsText = newsItem.toString()
                                if (newsText.isNotBlank()) {
                                    binding.datas.append("${index + 1}. $newsText\n\n")
                                    newsDao.insert(com.example.newp.db.Data(state = state, description = newsText, timestamp = timestamp))
                                    Log.d("StateNews", "News $index for $state: $newsText")
                                }
                            }
                        } else {
                            binding.datas.text = "No news found for $state"
                            Log.d("StateNews", "No news found for $state")
                        }
                    }
                }
            }
            // Clean up expired news from the database
            newsDao.delNews(expiry)

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                binding.datas.text = "Error fetching news for $state: ${e.localizedMessage}"
                Log.e("StateNews", "Error fetching news for $state", e)
            }
        }
    }
}


    // Add this property at class leve
    // WebAppInterface to handle JS interface calls
    private class WebAppInterface(
        private val context: Context,
        private val stateUpdateCallBack: (String?) -> Unit
    ) {
        var lastClickedState: String? = null

        @JavascriptInterface
        fun onPathClicked(pathId: String, state: String) {
            lastClickedState = state
            // Handle the clicked path's ID
            Log.d(
                "WebAppInterface",
                "onPathClicked: Path ID $pathId, State $state"
            )  // Log path clicked
            stateUpdateCallBack(lastClickedState)
//            Toast.makeText(context, "Clicked State is $state", Toast.LENGTH_SHORT).show()
        }
    }
    // Remove JavaScript interface when the activity is destroyed
    override fun onDestroy() {
        newsJob?.cancel()
        val webView: WebView = findViewById(R.id.webView)
        webView.removeJavascriptInterface("Android")
        Log.d(
            "MainActivity",
            "onDestroy: Removed JavascriptInterface"
        )  // Log removal of JS interface
        super.onDestroy()
    }
}
