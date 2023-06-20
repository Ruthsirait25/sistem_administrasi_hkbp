package com.example.hkbptarutung.registrasi.request

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.hkbptarutung.Profile
import com.example.hkbptarutung.R
import com.example.hkbptarutung.home.HomeJemaat
import com.example.hkbptarutung.registrasi.RegistrasiPageInterface
import com.example.hkbptarutung.registrasi.RegistrasiPagePresenter
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.sendSimpleNotif
import com.example.hkbptarutung.utils.sessionExpired
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.showToast
import com.example.hkbptarutung.utils.visibleGone

abstract class BaseRequestPage : AppCompatActivity(), RegistrasiPageInterface {

    var uidRequestor = ""
    fun titleString(): String = intent?.extras?.getString("title") ?: "Registrasi"
    fun docId() = intent?.extras?.getString("docId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupListener()
        setupAdapter()
        findViewById<TextView>(R.id.tv_registrasi_title).text = titleString()
        initsAdmin()
        hideView()
    }

    private fun setupAdapter() {
        ArrayAdapter.createFromResource(
            this, R.array.jenisKelamin, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            findViewById<Spinner>(R.id.sp_jekel)?.adapter = adapter
        }
    }

    open fun setupListener() {
        findViewById<View>(R.id.btn_lanjut).setOnClickListener {
            submit()
        }

        findViewById<View>(R.id.btn_batal).setOnClickListener {
            if (PreferenceUtils.isAdmin(this)) {
                reject()
                return@setOnClickListener
            }
            finish()
        }
        findViewById<View>(R.id.iv_btn_back).setOnClickListener {

            finish()
        }
        findViewById<View>(R.id.ll_home).setOnClickListener {
            finishAffinity()
            startActivity(Intent(this, HomeJemaat::class.java))
        }

        findViewById<View>(R.id.ll_profile).setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        findViewById<View>(R.id.iv_btn_delete).setOnClickListener {
            delete()
        }
    }

    override fun onSessionExpired() {
        sessionExpired()
    }

    override fun onSuccessRegister() {
        val msg = "Anda baru saja mendapatkan pengajuan ${titleString()}"
        sendSimpleNotif("/topics/admin", msg)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.registrasiKegiatan))
            setMessage("Registrasi kegiatan sudah ditambahkan, silahkan tunggu")
            setPositiveButton("Ya") { _, _ ->
                finish()
            }
        }.create().show()
    }

    private fun notify(approved: Boolean) {
        FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(uidRequestor).get()
            .addOnSuccessListener {
                val action = if (approved) "setujui" else "tolak"
                val msg = "${titleString()} anda di$action"
                sendSimpleNotif("/topics/$uidRequestor", msg)
            }
    }

    override fun onSuccessProcess(approved: Boolean) {
        val msg = if (approved) "Berhasil approve permintaan" else "Berhasil tolak permintaan"
        notify(approved)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.registrasiKegiatan))
            setMessage(msg)
            setPositiveButton("Ya") { _, _ ->
                setResult(Activity.RESULT_OK)
                finish()
            }
        }.create().show()
    }

    override fun onFailureRegister(msg: String) {
        showToast(msg)
    }

    private fun initsAdmin() {
        val isAdmin = PreferenceUtils.isAdmin(this)
        findViewById<ImageView>(R.id.iv_btn_delete).visibleGone(isAdmin)
        if (isAdmin) {
            findViewById<Button>(R.id.btn_lanjut).text = getString(R.string.setuju)
            findViewById<Button>(R.id.btn_batal).text = getString(R.string.tolak)
            findViewById<Button>(R.id.btn_batal).setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.merah
                )
            )
            initAdmin()
        }
    }

    abstract fun submit()
    abstract fun reject()
    abstract fun initAdmin()
    open fun hideView() {}
    abstract val binding: ViewBinding

    private fun delete() {
        val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
        val docId = docId() ?: return
        FirebaseUtils.db().collection(dbName).document(docId).delete().addOnSuccessListener {
            showAlert(msg = "Anda berhasil hapus pengajuan") {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun onFieldEmpty() {
        showAlert("Error", "harap isi semua kolom") {}
    }
}