package com.example.yoy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Tambah : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)

        db = DatabaseHelper(this)

        val etNominal = findViewById<EditText>(R.id.etNominal)
        val etKeterangan = findViewById<EditText>(R.id.etKeterangan)
        val rbPemasukan = findViewById<RadioButton>(R.id.rbPemasukan)
        val rbPengeluaran = findViewById<RadioButton>(R.id.rbPengeluaran)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        btnSimpan.setOnClickListener {
            val nominal = etNominal.text.toString().toIntOrNull()
            val keterangan = etKeterangan.text.toString()
            val jenis = if (rbPemasukan.isChecked) "Pemasukan" else "Pengeluaran"

            if (nominal != null && keterangan.isNotEmpty()) {
                val sukses = db.tambahTransaksi(nominal, keterangan, jenis)
                if (sukses) {
                    Toast.makeText(this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Isi semua data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
