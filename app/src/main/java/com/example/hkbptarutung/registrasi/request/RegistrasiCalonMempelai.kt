package com.example.hkbptarutung.registrasi.request

import android.widget.EditText
import androidx.viewbinding.ViewBinding
import com.example.hkbptarutung.R
import com.example.hkbptarutung.databinding.ActivityRegistrasiCalonMempelaiBinding
import com.example.hkbptarutung.registrasi.RegistrasiPagePresenter
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.value


class RegistrasiCalonMempelai : BaseRequestPage() {
    val presenter = RegistrasiPagePresenter(this)
    override fun submit() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.approveMartumpol(this)
            }
            return
        }
        presenter.submitMartupol(
            nik = findViewById<EditText>(R.id.edt_nik)?.value(),
            boyName = findViewById<EditText>(R.id.edt_nama_lakilaki).value(),
            boyPhone = findViewById<EditText>(R.id.edt_nomorHP).value(),
            boyAddress = findViewById<EditText>(R.id.edt_alamat).value(),
            boyChurch = findViewById<EditText>(R.id.edt_gereja).value(),
            boyDad = findViewById<EditText>(R.id.edt_nama_ayah).value(),
            boyMom = findViewById<EditText>(R.id.edt_nama_ibu).value(),
            nikPerempuan = findViewById<EditText>(R.id.edt_nik_perempuan).value(),
            girlName = findViewById<EditText>(R.id.edt_nama_perempuan).value(),
            girlPhone = findViewById<EditText>(R.id.edt_nomorHP_girl).value(),
            girlAddress = findViewById<EditText>(R.id.edt_alamat_perempuan).value(),
            girlChurch = findViewById<EditText>(R.id.edt_gereja_perempuan).value(),
            girlDad = findViewById<EditText>(R.id.edt_nama_ayah_perempuan).value(),
            girlMom = findViewById<EditText>(R.id.edt_nama_ibu_perempuan).value(),
        )
    }

    override fun reject() {
        if (PreferenceUtils.isAdmin(this)) {
            docId()?.apply {
                presenter.rejectMartumpol(this)
            }
            return
        }
    }

    override fun initAdmin() {
        val dbName = FirebaseUtils.getDbByType(this, titleString()) ?: return
        val docId = docId() ?: return
        FirebaseUtils.db().collection(dbName).document(docId).get().addOnSuccessListener {
            uidRequestor = "${it["requestor"]}"
            findViewById<EditText>(R.id.edt_nik_perempuan).setText("${it["girlNIK"]}")
            findViewById<EditText>(R.id.edt_nik).setText("${it["nik"]}")
            findViewById<EditText>(R.id.edt_nama_lakilaki).setText("${it["boyName"]}")
            findViewById<EditText>(R.id.edt_nomorHP).setText("${it["boyPhone"]}")
            findViewById<EditText>(R.id.edt_alamat).setText("${it["boyAddress"]}")
            findViewById<EditText>(R.id.edt_gereja).setText("${it["boyChurch"]}")
            findViewById<EditText>(R.id.edt_nama_ayah).setText("${it["boyDad"]}")
            findViewById<EditText>(R.id.edt_nama_ibu).setText("${it["boyMom"]}")
            findViewById<EditText>(R.id.edt_nama_perempuan).setText("${it["girlName"]}")
            findViewById<EditText>(R.id.edt_nomorHP_girl).setText("${it["girlPhone"]}")
            findViewById<EditText>(R.id.edt_alamat_perempuan).setText("${it["girlAddress"]}")
            findViewById<EditText>(R.id.edt_gereja_perempuan).setText("${it["girlChurch"]}")
            findViewById<EditText>(R.id.edt_nama_ayah_perempuan).setText("${it["girlDad"]}")
            findViewById<EditText>(R.id.edt_nama_ibu_perempuan).setText("${it["girlMom"]}")
        }
    }

    override val binding: ViewBinding
        get() = ActivityRegistrasiCalonMempelaiBinding.inflate(layoutInflater)
}