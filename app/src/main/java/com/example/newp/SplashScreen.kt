package com.example.newp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newp.databinding.ActivitySplashScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val curruser = FirebaseAuth.getInstance().currentUser
            if (curruser != null) {
                navigateToMainActivity()
            } else {
                loadLogiFrag()
            }

        }, 500)
    }
    private fun loadLogiFrag() {
      val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, loginFragment)
            .commit()
        binding.cloud.visibility = View.GONE

    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}