package com.example.hkbptarutung.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.AcaraIbadahMinggu
import com.example.hkbptarutung.HistoryPage
import com.example.hkbptarutung.LoginPage
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.WartaJemaatNew
import com.example.hkbptarutung.databinding.ActivityHomeJemaatBinding
import com.example.hkbptarutung.registrasi.RegistrasiLandingPage
import com.example.hkbptarutung.utils.PreferenceUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class HomeJemaat : AppCompatActivity() {
    lateinit var binding: ActivityHomeJemaatBinding
    private var pressedTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeJemaatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.cvHistory.setOnClickListener {
            startActivity(Intent(this, HistoryPage::class.java))
        }

        binding.cvAcaraMinggu.setOnClickListener {
            startActivity(Intent(this, AcaraIbadahMinggu::class.java))
        }

        binding.cvWartaJemaat.setOnClickListener {
            startActivity(Intent(this, WartaJemaatNew::class.java))
        }

        binding.cvRegistrasi.setOnClickListener {
            startActivity(Intent(this, RegistrasiLandingPage::class.java))
        }

        binding.ivProfile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        findViewById<View>(R.id.ll_logout).setOnClickListener {
            Firebase.auth.signOut()
            PreferenceUtils.getUid(this)?.let { uid ->
                FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(uid)
            }
            finishAffinity()
            startActivity(Intent(this, LoginPage::class.java))
        }

        findViewById<View>(R.id.ll_register).visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}