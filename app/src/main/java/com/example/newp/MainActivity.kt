package com.example.newp

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.example.newp.databinding.ActivityMainBinding
import java.io.ByteArrayInputStream
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.ui.window.application

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
        webView.addJavascriptInterface(WebAppInterface(this), "Android")


//       Glide.with(this).load(svg).into(binding.indiaMapImageView)
    }

    private class WebAppInterface(private val context: Context) {
        @JavascriptInterface
        fun onPathClicked(pathId: String,state : String) {
            // Handle the clicked path's ID
            // Example: Show a Toast message or log the ID
            println("Clicked Path ID: $pathId")
            Toast.makeText(context, "Clicked State is $state", Toast.LENGTH_SHORT).show()
        }
    }


//        clickableAreasImage.setClickableAreas(clickableAreas)
    //  binding.indiaMapImageView.setImageBitmap(bitmap)


    // Log or use the extracted IDs
//        pathIds.forEach { id ->
//            println("Path ID: $id")
//        }

    override fun onDestroy() {
        val webView: WebView = findViewById(R.id.webView)
        webView.removeJavascriptInterface("Android")
        super.onDestroy()
    }
}