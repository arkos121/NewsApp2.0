package com.example.newp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.newp.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherService = RetrofitClient.apiService
    private lateinit var adapter: MyAdapter
    private val items: MutableList<CardItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val recyclerView: RecyclerView = binding.recyclelayout
        adapter = MyAdapter(items)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        Log.d("MainActivity", "onCreate: Activity started")  // Log onCreate
        click()
    }

    fun additemsto(weatherData: WeatherResponse) {
        Log.d("MainActivity", "additemsto: Updating RecyclerView with weather data temp:${weatherData.main.tempInCelsius()}°C")

        // Clear and update the global items list
        val list :MutableList<CardItem> = mutableListOf()
        list.add(
                CardItem("State", getCapital(weatherData.name)),
        )
        Log.d("TAG", "additemsto: ${list.size}")
        list.add(CardItem("Temperature", "${weatherData.main.tempInCelsius()}°C"))

//            CardItem("Humidity", "${weatherData.main.humidity}%"),
//            CardItem("Wind Speed", "${weatherData.wind.speed} m/s"),
//            CardItem("Description", weatherData.weather[0].description))
        Log.d("MainActivity", "additemsto: Items added: $list")

        // Notify the adapter of data changes
//        adapter.updateData(list)
//        adapter.notifyDataSetChanged()
    }


    private fun click() {
        val webView: WebView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/indianmap.html")
        Log.d("MainActivity", "click: WebView loaded with Indian map")  // Log webView load

        webView.addJavascriptInterface(WebAppInterface(this) { state ->
            Log.d("MainActivity", "click: State clicked: $state")  // Log state clicked
            state?.let {
                println("The city is: ${getCapital(it)}")
                // Call makeApiCall inside a coroutine
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        Log.d(
                            "MainActivity",
                            "makeApiCall: Calling API for $it"
                        )  // Log before API call

                        // Update the UI on the main thread
                        var weatherData: WeatherResponse? = null
                        withContext(Dispatchers.IO) {
                            weatherData = makeApiCall(it)
                            Log.d(
                                "MainActivity",
                                "makeApiCall: API call successful $weatherData"
                            )  // Log after API call
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            adapter.updateData(weatherData)
                        }

                    } catch (e: Exception) {
                        Log.e(
                            "MainActivity",
                            "Error in API call: ${e}"
                        )  // Log error in API call
                    }
                }
            }
        }, "Android")
    }

    // Make the API call for weather data
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

    // Function to update the UI with the fetched weather data
//    private fun showWeatherData(weatherData: WeatherResponse, stateUpdateCallBack: String) {
//        Log.d("MainActivity", "showWeatherData: Showing weather data - Temp: {${weatherData.main.tempInCelsius()}}")  // Log weather data
//        Toast.makeText(this, "Temperature is ${weatherData.main.tempInCelsius()}°C for ${getCapital(stateUpdateCallBack)}", Toast.LENGTH_LONG).show()
//    }


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
            Toast.makeText(context, "Clicked State is $state", Toast.LENGTH_SHORT).show()
        }
    }

    // Remove JavaScript interface when the activity is destroyed
    override fun onDestroy() {
        val webView: WebView = findViewById(R.id.webView)
        webView.removeJavascriptInterface("Android")
        Log.d(
            "MainActivity",
            "onDestroy: Removed JavascriptInterface"
        )  // Log removal of JS interface
        super.onDestroy()
    }
}
