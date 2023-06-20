package com.example.hkbptarutung.registrasi.request

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ActivityRegistrasiPageBinding
import com.example.hkbptarutung.registrasi.RegistrasiPagePresenter
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.setDatePicker
import com.example.hkbptarutung.utils.value
import com.example.hkbptarutung.utils.visibleGone

class RegistrasiBaptis : BaseRequestPage() {

    var presenter = RegistrasiPagePresenter(this)

    override val binding: ViewBinding
        get() = ActivityRegistrasiPageBinding.inflate(layoutInflater)

    override fun setupListener() {
        super.setupListener()
        findViewById<TextView>(R.id.edt_tanggal_lahir).setDatePicker()
        findViewById<TextView>(R.id.edt_tanggal_baptis).setDatePicker()
        findViewById<TextView>(R.id.edt_tanggal_sidi).setDatePicker()
    }

    override fun submit() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.approveBaptis(this)
            }
            return
        }
        presenter.submitBaptis(
            nik = findViewById<EditText>(R.id.edt_nik)?.value(),
            fullName = findViewById<EditText>(R.id.edt_nama_Lengkap)?.value(),
            dadName = findViewById<EditText>(R.id.edt_nama_ayah)?.value(),
            momName = findViewById<EditText>(R.id.edt_nama_ibu)?.value(),
            birthPlace = findViewById<EditText>(R.id.edt_Tempat_Lahir)?.value(),
            birthDate = findViewById<TextView>(R.id.edt_tanggal_lahir)?.value(),
            phone = findViewById<TextView>(R.id.edt_nomorHP)?.value(),
            gender = findViewById<Spinner>(R.id.sp_jekel).selectedItem.toString(),
            baptisDate = findViewById<TextView>(R.id.edt_tanggal_baptis).value(),
        )
    }

    override fun reject() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.rejectBaptis(this)
            }
        }
    }

    override fun initAdmin() {
        val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
        val docId = docId() ?: return
        FirebaseUtils.db().collection(dbName).document(docId).get().addOnSuccessListener {
            uidRequestor = "${it["requestor"]}"
            findViewById<EditText>(R.id.edt_nik).setText("${it["nik"]}")
            findViewById<EditText>(R.id.edt_nama_Lengkap).setText("${it["fullName"]}")
            findViewById<EditText>(R.id.edt_nama_ayah).setText("${it["dad"]}")
            findViewById<EditText>(R.id.edt_nama_ibu).setText("${it["mom"]}")
            findViewById<EditText>(R.id.edt_nomorHP).setText("${it["phone"]}")
            findViewById<EditText>(R.id.edt_Tempat_Lahir).setText("${it["birthPlace"]}")
            findViewById<TextView>(R.id.edt_tanggal_lahir).setText("${it["birthDate"]}")
            findViewById<TextView>(R.id.edt_tanggal_baptis).setText("${it["baptisDate"]}")
            val gender = if (it["gender"] == getString(R.string.female)) 1 else 0
            findViewById<Spinner>(R.id.sp_jekel).setSelection(gender)
        }
    }

    override fun hideView() {
        findViewById<View>(R.id.ll_tanggal_sidi).visibleGone(false)
    }
}