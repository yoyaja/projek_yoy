package com.example.yoy

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper
    lateinit var listView: ListView
    lateinit var tvSaldo: TextView
    lateinit var btnTambah: Button
    lateinit var btnLihat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)
        listView = findViewById(R.id.listTransaksi)
        tvSaldo = findViewById(R.id.tvSaldo)
        btnTambah = findViewById(R.id.btnTambah)
        btnLihat = findViewById(R.id.btnLihat)

        loadData()

        btnTambah.setOnClickListener {
            startActivity(Intent(this, Tambah::class.java))
        }

        btnLihat.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        val transaksi = db.getSemuaTransaksi()
        val dataList = transaksi.map {
            "${it["tanggal"]} - ${it["jenis"]} : Rp ${it["nominal"]}\n${it["keterangan"]}"
        }
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)

        tvSaldo.text = "Saldo: Rp ${db.getSaldo()}"
    }
}
