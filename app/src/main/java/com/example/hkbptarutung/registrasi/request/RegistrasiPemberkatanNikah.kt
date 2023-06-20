package com.example.hkbptarutung.registrasi.request

import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ActivityRegistrasiPemberkatanNikahBinding
import com.example.hkbptarutung.registrasi.RegistrasiPagePresenter
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.setDatePicker
import com.example.hkbptarutung.utils.showDatePicker
import com.example.hkbptarutung.utils.toDateStr
import com.example.hkbptarutung.utils.value
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class RegistrasiPemberkatanNikah : BaseRequestPage() {

    val presenter = RegistrasiPagePresenter(this)

    override fun setupListener() {
        super.setupListener()
        findViewById<TextView>(R.id.edt_tanggal_lahir).setDatePicker()
        findViewById<TextView>(R.id.edt_tanggal_martumpol).setDatePicker()
    }

    override fun submit() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.approveNikah(this)
            }
            return
        }
        presenter.submitNikah(
            nik = findViewById<EditText>(R.id.edt_nik)?.value(),
            fullName = findViewById<EditText>(R.id.edt_nama_jemaat).value(),
            birthDate = findViewById<TextView>(R.id.edt_tanggal_lahir).value(),
            birthPlace = findViewById<EditText>(R.id.edt_tempat_lahir).value(),
            martumpolPlace = findViewById<EditText>(R.id.edt_tempat_martumpol).value(),
            martumpolDate = findViewById<TextView>(R.id.edt_tanggal_martumpol).value(),
            bloodType = findViewById<EditText>(R.id.edt_goldar).value(),
            address = findViewById<EditText>(R.id.edt_alamat).value(),
            phone = findViewById<EditText>(R.id.edt_nomorHP).value(),
        )
    }

    override fun reject() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.rejectNikah(this)
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
            findViewById<EditText>(R.id.edt_goldar).setText("${it["bloodType"]}")
            findViewById<EditText>(R.id.edt_alamat).setText("${it["address"]}")
            findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
            findViewById<EditText>(R.id.edt_tempat_martumpol).setText("${it["martumpol_place"]?:""}")
            findViewById<TextView>(R.id.edt_tanggal_martumpol).setText("${it["martumpol_date"]?:""}")
        }
    }

    override val binding: ActivityRegistrasiPemberkatanNikahBinding
        get() = ActivityRegistrasiPemberkatanNikahBinding.inflate(layoutInflater)
}