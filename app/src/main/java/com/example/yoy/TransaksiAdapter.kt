package com.example.yoy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog

class TransaksiAdapter(
    private val context: Context,
    private val transaksiList: MutableList<Map<String, Any>>,
    private val db: DatabaseHelper
) : BaseAdapter() {

    override fun getCount(): Int = transaksiList.size
    override fun getItem(position: Int): Any = transaksiList[position]
    override fun getItemId(position: Int): Long = (transaksiList[position]["id"] as Int).toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false)

        val tvDetail = view.findViewById<TextView>(R.id.tvDetail)
        val tvKeterangan = view.findViewById<TextView>(R.id.tvKeterangan)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnHapus = view.findViewById<Button>(R.id.btnHapus)

        val transaksi = transaksiList[position]

        val id = transaksi["id"] as Int
        val tanggal = transaksi["tanggal"].toString()
        val jenis = transaksi["jenis"].toString()
        val nominal = transaksi["nominal"].toString()
        val keterangan = transaksi["keterangan"].toString()

        tvDetail.text = "$tanggal - $jenis : Rp $nominal"
        tvKeterangan.text = keterangan

        // Edit
        btnEdit.setOnClickListener {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("nominal", nominal)
            intent.putExtra("keterangan", keterangan)
            intent.putExtra("jenis", jenis)
            context.startActivity(intent)
        }

        // Hapus
        btnHapus.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Hapus Transaksi")
                .setMessage("Yakin ingin menghapus transaksi ini?")
                .setPositiveButton("Ya") { _, _ ->
                    if (db.deleteTransaksi(id)) {
                        transaksiList.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Gagal menghapus transaksi", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        return view
    }
}
