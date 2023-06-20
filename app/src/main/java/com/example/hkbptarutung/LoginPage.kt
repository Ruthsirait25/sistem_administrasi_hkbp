package com.example.hkbptarutung

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.hkbptarutung.databinding.ActivityLoginPageBinding
import com.example.hkbptarutung.home.HomeAdminNew
import com.example.hkbptarutung.home.HomeJemaat
import com.example.hkbptarutung.home.HomeTamu
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.showToast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    private var pressedTime = 0L
    var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            loginJemaat()
        }
        binding.btnTamu.setOnClickListener {
            startActivity(Intent(this, HomeTamu::class.java))
            finish()
        }
        binding.tvForgetpass.setOnClickListener {
            startActivity(Intent(this, LupaPassword::class.java))
        }

        binding.tvDaftar.setOnClickListener {
            startActivity(Intent(this, PendaftaranAkun::class.java))
        }

        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            token = task.result
        }
    }

    private fun email() = binding.edtEmail.text.toString().trim()
    private fun password() = binding.edtPassword.text.toString()

    private fun loginJemaat() {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email(), password())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.uid == null) {
                        showToast("user tidak ditemukan")
                        return@addOnCompleteListener
                    }
                    FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(user.uid)
                        .get().addOnSuccessListener {
                            FirebaseUtils.setToken(token)
                            if (it["isAdmin"] != true) {
                                PreferenceUtils.apply {
                                    setUid(this@LoginPage, user.uid)
                                    setAdmin(this@LoginPage, false)
                                }
                                FirebaseMessaging.getInstance().subscribeToTopic(user.uid)
                                startActivity(Intent(this, HomeJemaat::class.java))
                                finishAffinity()
                                return@addOnSuccessListener
                            } else {
                                PreferenceUtils.setAdmin(this)
                                FirebaseMessaging.getInstance().subscribeToTopic("admin")
                                startActivity(Intent(this, HomeAdminNew::class.java))
                                finishAffinity()
                                return@addOnSuccessListener
                            }
                        }
                }
            }.addOnFailureListener {
                println("error ${it.message}")
                Toast.makeText(
                    baseContext,
                    "${it.message}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            /* override back pressing */
            override fun handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginPage,
                        "Tekan sekali lagi untuk keluar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                pressedTime = System.currentTimeMillis()
            }
        })
    }

}