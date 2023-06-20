package com.example.hkbptarutung.registrasi

import com.example.hkbptarutung.utils.FirebaseUtils
import com.google.firebase.firestore.SetOptions

interface RegistrasiPageInterface {
    fun onSessionExpired()
    fun onSuccessRegister()
    fun onFailureRegister(msg: String)
    fun onFieldEmpty()
    fun onSuccessProcess(approved: Boolean)
}

class RegistrasiPagePresenter(val presenter: RegistrasiPageInterface) {

    private fun fieldsValid(strings: Array<String?>): Boolean {
        for (item in strings) {
            if (item.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }

    fun submitBaptis(
        fullName: String?,
        dadName: String?,
        momName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        baptisDate: String?,
        nik: String?,
        phone: String?
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        if (!fieldsValid(
                arrayOf(
                    fullName,
                    dadName,
                    momName,
                    birthPlace,
                    birthDate,
                    gender,
                    baptisDate,
                    nik,
                    phone
                )
            )) {
            presenter.onFieldEmpty()
            return
        }
        FirebaseUtils.db().collection(FirebaseUtils.dbBaptis).add(
            hashMapOf(
                "nik" to nik,
                "fullName" to fullName,
                "dad" to dadName,
                "mom" to momName,
                "birthPlace" to birthPlace,
                "birthDate" to birthDate,
                "gender" to gender,
                "baptisDate" to baptisDate,
                "phone" to phone,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun approveBaptis(docId: String) {
        processDoc(FirebaseUtils.dbBaptis, docId, true)
    }

    fun approveSidi(docId: String) {
        processDoc(FirebaseUtils.dbSidi, docId, true)
    }

    fun approveMartumpol(docId: String) {
        processDoc(FirebaseUtils.dbMartupol, docId, true)
    }

    fun approveNikah(docId: String) {
        processDoc(FirebaseUtils.dbNikah, docId, true)
    }

    fun approveJemaat(docId: String) {
        processDoc(FirebaseUtils.dbJemaatBaru, docId, true)
    }

    fun approvePindah(docId: String) {
        processDoc(FirebaseUtils.dbPindahHuria, docId, true)
    }

    fun rejectBaptis(docId: String) {
        processDoc(FirebaseUtils.dbBaptis, docId, false)
    }

    fun rejectSidi(docId: String) {
        processDoc(FirebaseUtils.dbSidi, docId, false)
    }

    fun rejectMartumpol(docId: String) {
        processDoc(FirebaseUtils.dbMartupol, docId, false)
    }

    fun rejectNikah(docId: String) {
        processDoc(FirebaseUtils.dbNikah, docId, false)
    }

    fun rejectJemaat(docId: String) {
        processDoc(FirebaseUtils.dbJemaatBaru, docId, false)
    }

    fun rejectPindah(docId: String) {
        processDoc(FirebaseUtils.dbPindahHuria, docId, false)
    }

    fun processDoc(dbName: String, docId: String, value: Boolean) {
        FirebaseUtils.db().collection(dbName).document(docId).set(
            hashMapOf(
                "approved" to value
            ), SetOptions.merge()
        ).addOnSuccessListener {
            presenter.onSuccessProcess(value)
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitSidi(
        fullName: String?,
        dadName: String?,
        momName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        baptisDate: String?, sidiDate: String?, nik: String?, phone: String?
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        if (!fieldsValid(arrayOf(fullName, dadName, momName, birthPlace, birthDate, gender, baptisDate, sidiDate, nik, phone))) {
            presenter.onFieldEmpty()
            return
        }
        FirebaseUtils.db().collection(FirebaseUtils.dbSidi).add(
            hashMapOf(
                "nik" to nik,
                "fullName" to fullName,
                "dad" to dadName,
                "mom" to momName,
                "birthPlace" to birthPlace,
                "birthDate" to birthDate,
                "gender" to gender,
                "phone" to phone,
                "baptisDate" to baptisDate,
                "sidiDate" to sidiDate,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitMartupol(
        boyName: String?,
        boyAddress: String?,
        boyPhone: String?,
        boyChurch: String?,
        boyDad: String?,
        boyMom: String?,
        nikPerempuan: String?,
        girlName: String?,
        girlPhone: String?,
        girlAddress: String?,
        girlChurch: String?,
        girlDad: String?,
        girlMom: String?,
        nik: String?,
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        if (!fieldsValid(arrayOf(boyName, boyAddress, boyChurch, boyPhone, boyDad, boyMom, nikPerempuan, girlName, girlAddress, girlChurch, girlDad, girlMom, girlPhone, ))) {
            presenter.onFieldEmpty()
            return
        }
        FirebaseUtils.db().collection(FirebaseUtils.dbMartupol).add(
            hashMapOf(
                "nik" to nik,
                "boyName" to boyName,
                "boyPhone" to boyPhone,
                "boyAddress" to boyAddress,
                "boyChurch" to boyChurch,
                "boyDad" to boyDad,
                "boyMom" to boyMom,
                "girlNIK" to nikPerempuan,
                "girlName" to girlName,
                "girlPhone" to girlPhone,
                "girlAddress" to girlAddress,
                "girlChurch" to girlChurch,
                "girlDad" to girlDad,
                "girlMom" to girlMom,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitNikah(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        martumpolPlace: String?,
        martumpolDate: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        nik: String?,
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        if (!fieldsValid(arrayOf(fullName, birthPlace, birthDate, martumpolPlace, martumpolDate, bloodType, address, phone, nik))) {
            presenter.onFieldEmpty()
            return
        }
        FirebaseUtils.db().collection(FirebaseUtils.dbNikah).add(
            hashMapOf(
                "nik" to nik,
                "fullName" to fullName,
                "bloodType" to bloodType,
                "address" to address,
                "birthPlace" to birthPlace,
                "birthDate" to birthDate,
                "martumpol_place" to martumpolPlace,
                "martumpol_date" to martumpolDate,
                "phone" to phone,
                "requestor" to uid
            )
        ).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }

    fun submitJemaatBaru(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        nik: String?,
        gereja: String?
    ) {
        if (!fieldsValid(arrayOf(fullName, birthPlace, birthDate, gender, bloodType, address, phone, nik, gender))) {
            presenter.onFieldEmpty()
            return
        }
        submitOther(
            nik = nik,
            fullName = fullName,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            bloodType = bloodType,
            address = address,
            phone = phone,
            originChurch = gereja,
            db = FirebaseUtils.dbJemaatBaru,
        )
    }

    fun submitPindahHuria(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        nik: String?,
        originChurch: String?
    ) {
        if (!fieldsValid(arrayOf(fullName, birthPlace, birthDate, gender, bloodType, address, phone, nik, gender, originChurch))) {
            presenter.onFieldEmpty()
            return
        }
        submitOther(
            nik = nik,
            fullName = fullName,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            bloodType = bloodType,
            address = address,
            phone = phone,
            originChurch = originChurch,
            db = FirebaseUtils.dbPindahHuria,
        )
    }

    private fun submitOther(
        fullName: String?,
        birthPlace: String?,
        birthDate: String?,
        gender: String?,
        bloodType: String?,
        address: String?,
        phone: String?,
        originChurch: String?,
        nik: String?,
        db: String
    ) {
        val uid = FirebaseUtils.user()?.uid
        if (uid == null) presenter.onSessionExpired()
        val data = hashMapOf(
            "nik" to nik,
            "fullName" to fullName,
            "bloodType" to bloodType,
            "address" to address,
            "birthPlace" to birthPlace,
            "birthDate" to birthDate,
            "gender" to gender,
            "phone" to phone,
            "requestor" to uid
        )
        if (!originChurch.isNullOrEmpty()) data["originChurch"] = originChurch
        FirebaseUtils.db().collection(db).add(data).addOnSuccessListener {
            presenter.onSuccessRegister()
        }.addOnFailureListener {
            presenter.onFailureRegister(it.message ?: "Terjadi kesalahan")
        }
    }
}