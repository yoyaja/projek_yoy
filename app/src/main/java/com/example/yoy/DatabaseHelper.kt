package com.example.yoy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "keuangan.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE transaksi (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nominal INTEGER,
                keterangan TEXT,
                jenis TEXT,
                tanggal DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS transaksi")
        onCreate(db)
    }

    fun tambahTransaksi(nominal: Int, keterangan: String, jenis: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nominal", nominal)
            put("keterangan", keterangan)
            put("jenis", jenis)
        }
        val result = db.insert("transaksi", null, values)
        db.close()
        return result != -1L
    }

    fun getSemuaTransaksi(): List<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transaksi ORDER BY id DESC", null)
        val list = mutableListOf<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val map = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow("id")).toString(),
                    "nominal" to cursor.getInt(cursor.getColumnIndexOrThrow("nominal")).toString(),
                    "keterangan" to cursor.getString(cursor.getColumnIndexOrThrow("keterangan")),
                    "jenis" to cursor.getString(cursor.getColumnIndexOrThrow("jenis")),
                    "tanggal" to cursor.getString(cursor.getColumnIndexOrThrow("tanggal"))
                )
                list.add(map)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun getSaldo(): Int {
        val db = readableDatabase
        val pemasukan = db.rawQuery(
            "SELECT SUM(nominal) FROM transaksi WHERE jenis='Pemasukan'", null
        )
        val pengeluaran = db.rawQuery(
            "SELECT SUM(nominal) FROM transaksi WHERE jenis='Pengeluaran'", null
        )

        var totalPemasukan = 0
        var totalPengeluaran = 0

        if (pemasukan.moveToFirst()) totalPemasukan = pemasukan.getInt(0)
        if (pengeluaran.moveToFirst()) totalPengeluaran = pengeluaran.getInt(0)

        pemasukan.close()
        pengeluaran.close()
        db.close()

        return totalPemasukan - totalPengeluaran
    }

    fun updateTransaksi(id: Int, nominal: Int, keterangan: String, jenis: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nominal", nominal)
            put("keterangan", keterangan)
            put("jenis", jenis)
        }
        val result = db.update("transaksi", values, "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun deleteTransaksi(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("transaksi", "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}
