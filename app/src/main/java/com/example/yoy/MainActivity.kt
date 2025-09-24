package com.example.yoy

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var tvSaldo: TextView
    private lateinit var btnTambah: Button
    private lateinit var btnLihat: Button

    private lateinit var transaksi: List<Map<String, String>>

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

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = transaksi[position]
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", item["id"]?.toInt() ?: -1)
            intent.putExtra("nominal", item["nominal"]?.toInt() ?: 0)
            intent.putExtra("keterangan", item["keterangan"])
            intent.putExtra("jenis", item["jenis"])
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        transaksi = db.getSemuaTransaksi()
        val dataList = transaksi.map {
            "${it["tanggal"]} - ${it["jenis"]} : Rp ${it["nominal"]}\n${it["keterangan"]}"
        }
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        tvSaldo.text = "Saldo: Rp ${db.getSaldo()}"
    }
}
