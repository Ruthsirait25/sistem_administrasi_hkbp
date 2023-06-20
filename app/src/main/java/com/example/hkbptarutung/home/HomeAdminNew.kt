package com.example.hkbptarutung.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.AcaraIbadahMinggu
import com.example.hkbptarutung.LoginPage
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.WartaJemaatNew
import com.example.hkbptarutung.databinding.FragmentHomeAdminBinding
import com.example.hkbptarutung.registrasi.RegistrasiLandingPage
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.visibleGone
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class HomeAdminNew: AppCompatActivity() {

    lateinit var binding: FragmentHomeAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.ivIconMore.visibleGone(false)
        binding.clFooter.findViewById<View>(R.id.ll_logout).setOnClickListener {
            Firebase.auth.signOut()
            FirebaseMessaging.getInstance().unsubscribeFromTopic("admin")
            PreferenceUtils.setAdmin(this, false)
            finishAffinity()
            startActivity(Intent(this, LoginPage::class.java))

        }
        binding.clFooter.findViewById<View>(R.id.ll_profile).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
        binding.clRegistrasi.setOnClickListener {
            startActivity(Intent(this, RegistrasiLandingPage::class.java))
        }
        binding.cvAcaraMinggu.setOnClickListener {
            startActivity(Intent(this, AcaraIbadahMinggu::class.java))
        }
        binding.cvWartaJemaat.setOnClickListener {
            startActivity(Intent(this, WartaJemaatNew::class.java))
        }

    }
}