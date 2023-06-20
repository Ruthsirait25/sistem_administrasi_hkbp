package com.example.hkbptarutung.model

import com.example.hkbptarutung.utils.FirebaseUtils
import com.google.firebase.firestore.DocumentSnapshot

data class ApprovalItem(
    var requestorId: String = "",
    var documentId: String = "",
    var person: String = "Riana Sianturi",
    var approved: Boolean?,
    var type: String = "Pengajuan Administrasi Baptis"
) {
    companion object {
        fun fromDoc(doc: DocumentSnapshot, output: (ApprovalItem) -> Unit) {
            val item = ApprovalItem(
                requestorId = "${doc["requestor"]}",
                documentId = doc.id,
                person = "",
                type = "",
                approved = doc["approved"] as Boolean?
            )
            FirebaseUtils.db().collection(FirebaseUtils.dbUser).document(item.requestorId).get()
                .addOnSuccessListener {
                    item.person = "${it["fullName"]}"
                    output.invoke(item)
                }.addOnFailureListener {
                    output.invoke(item)
                }
        }
    }
}