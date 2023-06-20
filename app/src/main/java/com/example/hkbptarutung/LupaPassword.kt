package com.example.hkbptarutung

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.databinding.ActivityLupaPasswordBinding
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.value
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LupaPassword : AppCompatActivity() {
    lateinit var binding: ActivityLupaPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.lupaPassword)
        binding.btnForgetPass.setOnClickListener {
            Firebase.auth.sendPasswordResetEmail(binding.edtEmailForgetPas.value()).addOnSuccessListener {
                showAlert(title = "Link ubah password sudah dikirim", msg = "Silakan cek email anda") {
                    finish()
                }
            }
        }
        binding.LLForgetPass.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}