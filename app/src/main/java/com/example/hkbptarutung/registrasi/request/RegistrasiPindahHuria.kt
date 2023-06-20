package com.example.hkbptarutung.registrasi.request

import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ActivityRegistrasiOtherBinding
import com.example.hkbptarutung.registrasi.RegistrasiPagePresenter
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.setDatePicker
import com.example.hkbptarutung.utils.value

class RegistrasiPindahHuria : BaseRequestPage() {
    val presenter = RegistrasiPagePresenter(this)

    override fun setupListener() {
        super.setupListener()
        findViewById<TextView>(R.id.edt_tanggal_lahir).setDatePicker()
    }

    override fun submit() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.approvePindah(this)
            }
            return
        }
        presenter.submitPindahHuria(
            nik = findViewById<EditText>(R.id.edt_nik)?.value(),
            fullName = findViewById<EditText>(R.id.edt_nama_jemaat).value(),
            birthDate = findViewById<TextView>(R.id.edt_tanggal_lahir).value(),
            birthPlace = findViewById<EditText>(R.id.edt_tempat_lahir).value(),
            gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
            bloodType = findViewById<EditText>(R.id.edt_goldar).value(),
            address = findViewById<EditText>(R.id.edt_alamat).value(),
            phone = findViewById<EditText>(R.id.edt_nomorHP).value(),
            originChurch = findViewById<EditText>(R.id.edt_gereja).value(),
        )
    }

    override fun reject() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.rejectPindah(this)
            }
            return
        }
    }

    override fun initAdmin() {
        val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
        val docId = docId() ?: return
        FirebaseUtils.db().collection(dbName).document(docId).get().addOnSuccessListener {
            uidRequestor = "${it["requestor"]}"
            findViewById<EditText>(R.id.edt_nik).setText("${it["nik"]}")
            findViewById<EditText>(R.id.edt_nama_jemaat).setText("${it["fullName"]}")
            findViewById<TextView>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
            findViewById<EditText>(R.id.edt_tempat_lahir).setText("${it["birthPlace"]}")
            val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
            findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
            findViewById<EditText>(R.id.edt_goldar).setText("${it["bloodType"]}")
            findViewById<EditText>(R.id.edt_alamat).setText("${it["address"]}")
            findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
            findViewById<EditText>(R.id.edt_gereja).setText("${it["originChurch"]}")
        }
    }

    override val binding: ViewBinding
        get() = ActivityRegistrasiOtherBinding.inflate(layoutInflater)

}