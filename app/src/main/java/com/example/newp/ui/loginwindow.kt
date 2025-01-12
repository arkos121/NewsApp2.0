package com.example.newp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_login, container, false)
        val email = binding.findViewById<TextView>(R.id.Email)
        val password = binding.findViewById<TextView>(R.id.Passcode)
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id)) // Ensure this matches your web client ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        // Setup the sign-in button
        val signInButton = binding.findViewById<SignInButton>(R.id.btn_google_sign_in)
        signInButton.setOnClickListener {
            signInWithGoogle()
        }

        val loginButton = binding.findViewById<Button>(R.id.signins)
        loginButton.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            loginUser(emailText, passwordText)
        }

        val registerButton = binding.findViewById<Button>(R.id.signups2)
        registerButton.setOnClickListener{
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            registerUser(emailText, passwordText)
        }
        return binding
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            } else {
                Toast.makeText(requireContext(), "Google sign-in canceled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun loginUser(email : String , password : String){
        if(email.isEmpty() || password.isEmpty()){
            showDialog("Login Failed", "Email or password cannot be empty")
            return@loginUser
        }
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = firebaseAuth.currentUser
                    Toast.makeText(requireContext(), "Welcome,"+email.substring(0,email.indexOf("@")), Toast.LENGTH_SHORT).show()
                    println("Login is successful for ${user?.displayName}")
                    if(user != null) {
                        updateUI(user)
                    }
                }
                else {
                    println("Login failed")
                    showDialog("Login Failed", "Email or password is incorrect")
                    Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun registerUser(email: String, password: String) {
        if(email.isEmpty() || password.isEmpty()){
            showDialog("Sign-Up Failed", "Email or password cannot be empty")
            return@registerUser
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    println("Registration successful: ${user?.email}")
                    Toast.makeText(requireContext(),"Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),"Registration failed ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    if(task.exception?.message == "Registration Failed The email address is already in use by another account."){
                        showDialog("Registration Failed", "The email address is already in use by another account.")

                    }
                    println("Error: ${task.exception?.message}")
                }
            }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e("GoogleSignInError", "Google sign-in failed", e)
            Toast.makeText(requireContext(), "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(requireContext(),"Welcome, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                        updateUI(user)
                    } else {
                        Log.e("AuthError", "Authentication failed: ${task.exception?.message}")
                        Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Google account is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(title : String, message : String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
       builder.create().show()
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_LONG).show()
        }
    }
}

