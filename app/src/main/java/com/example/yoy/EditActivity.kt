package com.example.yoy

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private var transaksiId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        db = DatabaseHelper(this)

        val etNominal = findViewById<EditText>(R.id.etNominal)
        val etKeterangan = findViewById<EditText>(R.id.etKeterangan)
        val rbPemasukan = findViewById<RadioButton>(R.id.rbPemasukan)
        val rbPengeluaran = findViewById<RadioButton>(R.id.rbPengeluaran)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnHapus = findViewById<Button>(R.id.btnHapus)

        // Ambil data intent
        transaksiId = intent.getIntExtra("id", -1)
        val nominal = intent.getIntExtra("nominal", 0)
        val keterangan = intent.getStringExtra("keterangan") ?: ""
        val jenis = intent.getStringExtra("jenis") ?: ""

        etNominal.setText(nominal.toString())
        etKeterangan.setText(keterangan)
        if (jenis == "Pemasukan") rbPemasukan.isChecked = true else rbPengeluaran.isChecked = true

        // Update
        btnUpdate.setOnClickListener {
            val newNominal = etNominal.text.toString().toIntOrNull() ?: 0
            val newKeterangan = etKeterangan.text.toString()
            val newJenis = if (rbPemasukan.isChecked) "Pemasukan" else "Pengeluaran"

            val sukses = db.updateTransaksi(transaksiId, newNominal, newKeterangan, newJenis)
            if (sukses) {
                Toast.makeText(this, "Transaksi berhasil diupdate", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal update transaksi", Toast.LENGTH_SHORT).show()
            }
        }

        // Hapus
        btnHapus.setOnClickListener {
            val sukses = db.deleteTransaksi(transaksiId)
            if (sukses) {
                Toast.makeText(this, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal hapus transaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
