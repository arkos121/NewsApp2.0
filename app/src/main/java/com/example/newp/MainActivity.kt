package com.example.newp

import android.content.Context
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.newp.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherService = RetrofitClient.apiService
    private lateinit var coordinates: GeoCodeResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        click()
    }

    private fun click() {
        val webView: WebView = binding.webView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/indianmap.html")
        webView.addJavascriptInterface(WebAppInterface(this) { state ->
            state?.let {
                println("The city is: ${getCapital(it)}")
                // Call makeApiCall inside a coroutine
                CoroutineScope(Dispatchers.IO).launch {
                    val weatherData = makeApiCall(it)
                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        showWeatherData(weatherData)
                    }
                }
            }
        }, "Android")
    }

    // Make the API call for weather data
    private suspend fun makeApiCall(state: String): WeatherResponse {
        return try {
            val weatherResponse = weatherService.getWeatherByCity(getCapital(state))
            weatherResponse
        } catch (e: Exception) {
            e.printStackTrace()
            // Return a default weather response on error
            WeatherResponse(emptyList(), WeatherResponse.MainWeatherData(0.0, 0.0, 0.0, 0.0, 0, 0), WeatherResponse.WindData(0.0, 0),WeatherResponse.CloudData(0),"")
        }
    }

    // Function to update the UI with the fetched weather data
    private fun showWeatherData(weatherData: WeatherResponse) {
        // Example: Show a toast with the temperature
        Toast.makeText(this, "Temperature is ${weatherData.main.temp}°C", Toast.LENGTH_LONG).show()
    }

    // WebAppInterface to handle JS interface calls
    private class WebAppInterface(private val context: Context, private val stateUpdateCallBack: (String?) -> Unit) {
        var lastClickedState: String? = null

        @JavascriptInterface
        fun onPathClicked(pathId: String, state: String) {
            lastClickedState = state
            // Handle the clicked path's ID
            stateUpdateCallBack(lastClickedState)
            println("Clicked Path ID: $pathId")
            Toast.makeText(context, "Clicked State is $state", Toast.LENGTH_SHORT).show()
        }
    }

    // Remove JavaScript interface when the activity is destroyed
    override fun onDestroy() {
        val webView: WebView = findViewById(R.id.webView)
        webView.removeJavascriptInterface("Android")
        super.onDestroy()
    }
}
