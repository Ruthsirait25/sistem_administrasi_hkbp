package com.example.hkbptarutung.registrasi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hkbptarutung.R
import com.example.hkbptarutung.adapters.ApprovalRegistrasiAdapter
import com.example.hkbptarutung.adapters.ApprovalRegistrasiInterface
import com.example.hkbptarutung.databinding.ActivityApprovalRegistrasiBinding
import com.example.hkbptarutung.model.ApprovalItem
import com.example.hkbptarutung.utils.FirebaseUtils
import com.example.hkbptarutung.utils.convertActivity
import com.example.hkbptarutung.utils.value

class ActivityApprovalRegistrasi : AppCompatActivity(), ApprovalRegistrasiInterface {

    lateinit var binding: ActivityApprovalRegistrasiBinding
    lateinit var adapter: ApprovalRegistrasiAdapter

    private fun intentType() = intent?.extras?.getString("type")
    private val query: String
        get() {
            return binding.edtCariNama.value().trim().lowercase()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApprovalRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()
        initLayout()
    }

    private fun initLayout() {
        binding.ivBtnBack.setOnClickListener {
            finish()
        }
        binding.edtCariNama.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                getData()
                return@setOnEditorActionListener true;
            }
            return@setOnEditorActionListener false;
        }
    }


    private fun getData() {
        adapter = ApprovalRegistrasiAdapter(arrayListOf(), this)
        binding.rvApproval.layoutManager = LinearLayoutManager(this)
        binding.rvApproval.adapter = adapter
        val db = FirebaseUtils.getDbByType(this, intentType()) ?: return
        FirebaseUtils.db().collection(db).get()
            .addOnSuccessListener {
                val approvalItems = arrayListOf<ApprovalItem>()
                for (i in 0 until it.documents.size) {
                    ApprovalItem.fromDoc(it.documents[i]) { item ->
                        apply {
                            item.type = " ~ Pengajuan ${intentType()}"
                            if (query.isEmpty()) {
                                approvalItems.add(item)
                            } else if (item.person.lowercase().contains(query)) {
                                approvalItems.add(item)
                            }
                            adapter = ApprovalRegistrasiAdapter(approvalItems, this)
                            binding.rvApproval.layoutManager = LinearLayoutManager(this)
                            binding.rvApproval.adapter = adapter
                        }
                    }
                }

            }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                getData()
            }
        }

    override fun onClickApprovalItem(item: ApprovalItem) {
        val title = item.type.replace(" ~ Pengajuan ", "")
        val target = convertType(title) ?: return
        resultLauncher.launch(Intent(this, convertActivity(target)).apply {
            putExtra("title", convertType(title))
            putExtra("docId", item.documentId)
        })
    }


    private fun Context.convertType(from: String): String? {
        when (from) {
            getString(R.string.menu_baptis) -> {
                return getString(R.string.registrasi_baptis)
            }

            getString(R.string.menu_pindah_huria) -> {
                return getString(R.string.registrasi_pindah_huria)
            }

            getString(R.string.menu_nikah) -> {
                return getString(R.string.registrasi_pemberkatan_nikah)
            }

            getString(R.string.menu_martumpol) -> {
                return getString(R.string.registrasi_martupol)
            }

            getString(R.string.menu_sidi) -> {
                return getString(R.string.registrasi_sidi)
            }

            getString(R.string.menu_jemaat_baru) -> {
                return getString(R.string.registrasi_jemaat_baru)
            }
        }
        return null
    }

}