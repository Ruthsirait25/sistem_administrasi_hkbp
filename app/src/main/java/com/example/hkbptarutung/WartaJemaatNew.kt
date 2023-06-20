package com.example.hkbptarutung

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.databinding.ActivityWartaJemaatNewBinding
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.showPromptAlert
import com.example.hkbptarutung.utils.visibleGone

class WartaJemaatNew : AppCompatActivity() {

    private lateinit var binding: ActivityWartaJemaatNewBinding

    private val topikLabel = "topik"
    private val peleanIbadahLabel = "ibadah"
    private val peleanIaLabel = "ia"
    private val peleanIbLabel = "ib"
    private val peleanIIlabel = "ii"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWartaJemaatNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        getData()
    }

    private fun getData() {
        FirebaseUtils.db().collection(FirebaseUtils.dbWarta).document(FirebaseUtils.dbWarta).get()
            .addOnSuccessListener {
                binding.apply {
                    tvTopikMinggu.text = "${it[topikLabel]}"
                    tvPeleanIbadah.text = "${it[peleanIbadahLabel]}"
                    tvPeleanIa.text = "${it[peleanIaLabel]}"
                    tvPeleanIb.text = "${it[peleanIbLabel]}"
                    tvPeleanIi.text = "${it[peleanIIlabel]}"
                }
            }
    }

    private fun initListeners() {
        binding.apply {
            val isAdmin = PreferenceUtils.isAdmin(this@WartaJemaatNew)
            btnUbahTopikMinggu.visibleGone(isAdmin)
            btnUbahTopikMinggu.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Topik Minggu : $str"
                    tvTopikMinggu.text = newVal
                }
            }
            btnPeleanIbadahUbah.visibleGone(false)
            btnPeleanIbadahUbah.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Pelean ibadah $str"
                    tvPeleanIbadah.text = newVal
                }
            }
            btnPeleanIa.visibleGone(isAdmin)
            btnPeleanIa.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Pelean IA : $str"
                    tvPeleanIa.text = newVal
                }
            }
            btnPeleanIb.visibleGone(isAdmin)
            btnPeleanIb.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Pelean IB : $str"
                    tvPeleanIb.text = newVal
                }
            }
            btnUbahPeleanIi.visibleGone(isAdmin)
            btnUbahPeleanIi.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Pelean II/Namarboho : $str"
                    tvPeleanIi.text = newVal
                }
            }
            btnDelete.visibleGone(isAdmin)
            btnDelete.setOnClickListener {
                delete()
            }
            btnTambah.visibleGone(isAdmin)
            btnTambah.setOnClickListener {
                simpan()
            }
            ivBtnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun simpan() {
        binding.apply {
            FirebaseUtils.db().collection(FirebaseUtils.dbWarta).document(FirebaseUtils.dbWarta)
                .set(
                    hashMapOf(
                        topikLabel to "${tvTopikMinggu.text}",
                        peleanIbadahLabel to "${tvPeleanIbadah.text}",
                        peleanIaLabel to "${tvPeleanIa.text}",
                        peleanIbLabel to "${tvPeleanIb.text}",
                        peleanIIlabel to "${tvPeleanIi.text}"
                    )
                ).addOnSuccessListener {
                    showAlert(msg = "Warta berhasil ditambah") {
                        finish()
                    }
                }
        }
    }

    private fun delete() {
        binding.apply {
            tvTopikMinggu.text = getString(R.string.topik_minggu)
            tvPeleanIbadah.text = getString(R.string.pelean_ibadah)
            tvPeleanIa.text = getString(R.string.pelean_ia)
            tvPeleanIb.text = getString(R.string.pelean_ib)
            tvPeleanIi.text = getString(R.string.pelean_ii_namarboho)
        }
    }
}