package com.example.hkbptarutung

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.databinding.ActivityIbadahMingguBinding
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.PreferenceUtils
import com.example.hkbptarutung.utils.showAlert
import com.example.hkbptarutung.utils.showPromptAlert
import com.example.hkbptarutung.utils.visibleGone
import com.google.firebase.firestore.SetOptions

class AcaraIbadahMinggu : AppCompatActivity() {

    private lateinit var binding: ActivityIbadahMingguBinding

    private val titleLabel = "minggu"
    private val hohomLabel = "hohom"
    private val votumLabel = "votum"
    private val patikLabel = "patik"
    private val manopotiLabel = "manopoti"
    private val epistelLabel = "epistel"
    private val dosaLabel = "dosa"
    private val tingtingLabel = "tingting"
    private val jamitaLabel = "jamita"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIbadahMingguBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        getData()
    }

    private fun getData() {
        FirebaseUtils.db().collection(FirebaseUtils.dbAcara).document(FirebaseUtils.dbAcara).get()
            .addOnSuccessListener {
                binding.apply {
                    tvTitleAcara.text = "${it[titleLabel]}"
                    tvUbahHohomBeno.text = "${it[hohomLabel]}"
                    tvUbahVotumBeno.text = "${it[votumLabel]}"
                    tvUbahPatikBeno.text = "${it[patikLabel]}"
                    tvUbahManopatiBeno.text = "${it[manopotiLabel]}"
                    tvUbahEpistel.text = "${it[epistelLabel]}"
                    tvUbahDosa.text = "${it[dosaLabel]}"
                    tvUbahTingtingBeno.text = "${it[tingtingLabel]}"
                    tvUbahJamitaBeno.text = "${it[jamitaLabel]}"
                }
            }
    }

    private fun initListeners() {
        binding.apply {
            ivBtnBack.setOnClickListener {
                finish()
            }
            val isAdmin = PreferenceUtils.isAdmin(this@AcaraIbadahMinggu)
            btnUbahTitle.visibleGone(isAdmin)
            btnUbahTitle.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Minggu $str"
                    tvTitleAcara.text = newVal
                }
            }
            btnUbahHohom.visibleGone(isAdmin)
            btnUbahHohom.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahHohomBeno.text = newVal
                }
            }
            btnUbahVotum.visibleGone(isAdmin)
            btnUbahVotum.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahVotumBeno.text = newVal
                }
            }
            btnUbahPatik.visibleGone(isAdmin)
            btnUbahPatik.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahPatikBeno.text = newVal
                }
            }
            btnUbahManopoti.visibleGone(isAdmin)
            btnUbahManopoti.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahManopatiBeno.text = newVal
                }
            }
            btnUbahEpistel.visibleGone(isAdmin)
            btnUbahEpistel.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "Epistel:  $str"
                    tvUbahEpistel.text = newVal
                }
            }
            btnUbahDosa.visibleGone(isAdmin)
            btnUbahDosa.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahDosa.text = newVal
                }
            }
            btnUbahTingting.visibleGone(isAdmin)
            btnUbahTingting.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahTingtingBeno.text = newVal
                }
            }
            btnUbahJamita.visibleGone(isAdmin)
            btnUbahJamita.setOnClickListener {
                showPromptAlert { str ->
                    val newVal = "BE No.  $str"
                    tvUbahJamitaBeno.text = newVal
                }
            }
            btnDelete.visibleGone(isAdmin)
            btnDelete.setOnClickListener {
                deleteAll()
            }
            btnTambah.visibleGone(isAdmin)
            btnTambah.setOnClickListener {
                update()
            }
        }
    }

    private fun update() {
        binding.apply {
            FirebaseUtils.db().collection(FirebaseUtils.dbAcara).document(FirebaseUtils.dbAcara)
                .set(
                    hashMapOf(
                        titleLabel to "${tvTitleAcara.text}",
                        hohomLabel to "${tvUbahHohomBeno.text}",
                        votumLabel to "${tvUbahVotumBeno.text}",
                        patikLabel to "${tvUbahPatikBeno.text}",
                        manopotiLabel to "${tvUbahManopatiBeno.text}",
                        epistelLabel to "${tvUbahEpistel.text}",
                        dosaLabel to "${tvUbahDosa.text}",
                        tingtingLabel to "${tvUbahTingtingBeno.text}",
                        jamitaLabel to "${tvUbahJamitaBeno.text}"
                    ), SetOptions.merge()
                ).addOnSuccessListener {
                    showAlert(msg = "Acara minggu berhasil ditambah") {
                        finish()
                    }
                }
        }
    }

    private fun deleteAll() {
        binding.apply {
            val beno = getString(R.string.be_no)
            val epistel = getString(R.string.epistel)
            tvTitleAcara.text = getString(R.string.minggu)
            tvUbahDosa.text = beno
            tvUbahEpistel.text = epistel
            tvUbahVotumBeno.text = beno
            tvUbahHohomBeno.text = beno
            tvUbahPatikBeno.text = beno
            tvUbahJamitaBeno.text = beno
            tvUbahManopatiBeno.text = beno
            tvUbahTingtingBeno.text = beno
        }

    }
}