package com.example.yoy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

     class Tambah : AppCompatActivity() {
       lateinit var db: DatabaseHelper
        lateinit var etNominal: EditText
        lateinit var etKeterangan: EditText
        lateinit var rbPemasukan: RadioButton
        lateinit var rbPengeluaran: RadioButton
        lateinit var btnSimpan: Button

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tambah)

            db = DatabaseHelper(this)
            etNominal = findViewById(R.id.etNominal)
            etKeterangan = findViewById(R.id.etKeterangan)
            rbPemasukan = findViewById(R.id.rbPemasukan)
            rbPengeluaran = findViewById(R.id.rbPengeluaran)
            btnSimpan = findViewById(R.id.btnSimpan)

            btnSimpan.setOnClickListener {
                val nominalStr = etNominal.text.toString().trim()
                val keterangan = etKeterangan.text.toString().trim()
                val jenis = if (rbPemasukan.isChecked) "Pemasukan" else "Pengeluaran"

                if (nominalStr.isEmpty() || keterangan.isEmpty()) {
                    Toast.makeText(this, "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val nominal = nominalStr.toIntOrNull()
                if (nominal == null) {
                    Toast.makeText(this, "Nominal harus angka", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val sukses = db.tambahTransaksi(nominal, keterangan, jenis)
                if (sukses) {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }