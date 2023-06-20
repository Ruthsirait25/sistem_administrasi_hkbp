package com.example.hkbptarutung

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.hkbptarutung.databinding.ActivityPendaftaranAkunBinding
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.showErrorAlert
import com.example.hkbptarutung.utils.showErrorAlert
import com.example.hkbptarutung.utils.value
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PendaftaranAkun : AppCompatActivity() {
    lateinit var binding: ActivityPendaftaranAkunBinding
    private lateinit var auth: FirebaseAuth

    private fun email(): String = binding.edtEmail.text.toString().trim()
    private fun password(): String = binding.edtPassword.text.toString()
    private fun umur(): Int? = binding.edtUmur.text.toString().toIntOrNull()

    var dialogz: Dialog? = null

    val dialog: Dialog
        get() = dialogz!!

    private fun showDialog() {
        dialogz = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.my_loading_layout)
        dialog.show()
    }

    private fun toggleEnableDisable(enable: Boolean) {
        binding.edtUlangiPassword.isEnabled = enable
        binding.edtNamaLengkap.isEnabled = enable
        binding.edtPassword.isEnabled = enable
        binding.edtUsername.isEnabled = enable
        binding.edtNomorHP.isEnabled = enable
        binding.edtAlamat.isEnabled = enable
        binding.edtEmail.isEnabled = enable
        binding.edtUmur.isEnabled = enable
        binding.edtWijk.isEnabled = enable
    }

    private fun dismissDialog() = dialog.dismiss()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendaftaranAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        supportActionBar?.title = getString(R.string.pendaftaranAkun)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ArrayAdapter.createFromResource(
            this, R.array.jenisKelamin, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spJekel.adapter = adapter
        }
        binding.btnLanjut.setOnClickListener {
            signUp()
        }
        binding.btnBatal.setOnClickListener {
            finish()
        }
        binding.LLPendaftaran.setOnClickListener {
            finish()
        }
    }

    private fun putValue(obj: HashMap<String, String>, value: String, key: String) {
        value.apply {
            if (this.isNotEmpty()) obj[key] = this
        }
    }

    private fun signUp() {
        if (umur() == null) {
            showErrorAlert("Umur tidak boleh kosong")
            return
        }
        if (umur()!! < 18) {
            showErrorAlert("Umur minimal 18 tahun")
            return
        }
        if (email().isEmpty()) {
            showErrorAlert("email tidak boleh kosong")
            return
        }
        if (password().length < 6) {
            showErrorAlert("password harus lebih dari 5 huruf")
            return
        }
        if (password() != binding.edtUlangiPassword.value()) {
            showErrorAlert("password tidak sesuai")
            return
        }
        if (binding.edtNamaLengkap.value().isEmpty()) {
            showErrorAlert("nama lengkap harus diisi")
            return
        }
        toggleEnableDisable(false)
        auth.createUserWithEmailAndPassword(email(), password())
            .addOnCompleteListener(this) { task ->
                toggleEnableDisable(true)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    println("berhasil dgn password ${password()}")
                    val user = auth.currentUser
                    if (user?.uid == null) {
                        return@addOnCompleteListener
                    }
                    val userDB = hashMapOf(
                        "fullName" to binding.edtNamaLengkap.value(),
                        "gender" to binding.spJekel.selectedItem.toString(),
                    )
                    putValue(userDB, binding.edtUmur.value(), "age")
                    putValue(userDB, binding.edtWijk.value(), "wijk")
                    putValue(userDB, binding.edtUsername.value(), "username")
                    putValue(userDB, binding.edtNomorHP.value(), "phone")
                    putValue(userDB, binding.edtAlamat.value(), "address")
                    FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(user.uid)
                        .set(userDB)
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.pendaftaranAkun))
                        setMessage("Akun anda berhasil didaftarkan, Silahkan login")
                        setPositiveButton("Ya") { _, _ ->
                            finish()
                        }
                    }.create().show()
                } else {
                    println("error gtw kenapa")
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }.addOnFailureListener {
                println("error $it")
                toggleEnableDisable(true)
            }
    }
//
//    override fun onResume() {
//        super.onResume()
//        toggleEnableDisable(false)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}