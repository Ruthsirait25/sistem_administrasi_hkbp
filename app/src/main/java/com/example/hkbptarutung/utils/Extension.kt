package com.example.hkbptarutung.utils

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hkbptarutung.LoginPage
import com.example.hkbptarutung.R
import com.example.hkbptarutung.registrasi.request.RegistrasiBaptis
import com.example.hkbptarutung.registrasi.request.RegistrasiCalonMempelai
import com.example.hkbptarutung.registrasi.request.RegistrasiJemaatBaru
import com.example.hkbptarutung.registrasi.request.RegistrasiPemberkatanNikah
import com.example.hkbptarutung.registrasi.request.RegistrasiPindahHuria
import com.example.hkbptarutung.registrasi.request.RegistrasiSidi
import com.google.firebase.Timestamp
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun EditText.value() = this.text.toString()

fun TextView.value() = this.text.toString()

fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.showAlert(title: String = "", msg: String, onYes: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(msg)
        setPositiveButton("Ya") { _, _ ->
            onYes.invoke()
        }
    }.create().show()
}

fun Context.showErrorAlert(msg: String) {
    showAlert("Error", msg) {}
}

fun emptyString() = ""

fun Context.showPromptAlert(onSubmit: (value: String) -> Unit) {
    val alert = AlertDialog.Builder(this)

//    alert.setTitle("Title")
    alert.setMessage("Masukkan label yang diinginkan")

// Set an EditText view to get user input

// Set an EditText view to get user input
    val input = EditText(this)
    alert.setView(input)

    alert.setPositiveButton("Ok") { _, _ ->
        val value: String = input.value()
        onSubmit.invoke(value)
    }

    alert.setNegativeButton(
        "Cancel"
    ) { _, _ ->
        // Canceled.
    }

    alert.show()
}

fun AppCompatActivity.sessionExpired() {
    showToast("sesi anda telah habis")
    startActivity(Intent(this, LoginPage::class.java))
    finishAffinity()
}

fun Context.showDatePicker(onSelected: (date: String) -> Unit) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val mon = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    val dpd = DatePickerDialog(this, { view, y, m, d ->
        onSelected("${d.zeroPadding()} ${month[m]} $y")
    }, year, mon, day)

    dpd.show()
}

val month = arrayListOf(
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "Mei",
    "Juni",
    "Juli",
    "Ags",
    "Sep",
    "Okt",
    "Nov",
    "Des"
)

fun Int.zeroPadding(): String = if (this > 9) "$this" else "0$this"

fun Context.convertActivity(input: String): Class<*> {
    val java: Class<*> = RegistrasiBaptis::class.java
    when (input) {
        getString(R.string.registrasi_sidi) -> {
            return RegistrasiSidi::class.java
        }

        getString(R.string.registrasi_martupol) -> {
            return RegistrasiCalonMempelai::class.java
        }

        getString(R.string.registrasi_pindah_huria) -> {
            return RegistrasiPindahHuria::class.java
        }

        getString(R.string.registrasi_jemaat_baru) -> {
            return RegistrasiJemaatBaru::class.java
        }

        getString(R.string.registrasi_pemberkatan_nikah) -> {
            return RegistrasiPemberkatanNikah::class.java
        }
    }
    return java
}

fun View.visibleGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Timestamp.toDateStr(): String {
    println("apa  ini ${this.toDate()}")
    return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.toDate())
}

fun TextView.setDatePicker() {
    setOnClickListener {
        context.showDatePicker { datestr ->
            this.setText(datestr)
        }
    }
}