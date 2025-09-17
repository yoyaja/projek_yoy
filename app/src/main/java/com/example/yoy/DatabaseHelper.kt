package com.example.yoy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "yoy.db", null, 1) {

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
        val values = ContentValues()
        values.put("nominal", nominal)
        values.put("keterangan", keterangan)
        values.put("jenis", jenis)
        val result = db.insert("transaksi", null, values)
        return result != -1L
    }

    fun getSemuaTransaksi(): MutableList<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transaksi ORDER BY id DESC", null)
        val list = mutableListOf<Map<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val item = mapOf(
                    "id" to cursor.getInt(0).toString(),
                    "nominal" to cursor.getInt(1).toString(),
                    "keterangan" to cursor.getString(2),
                    "jenis" to cursor.getString(3),
                    "tanggal" to cursor.getString(4)
                )
                list.add(item)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getSaldo(): Int {
        val db = readableDatabase
        var saldo = 0

        val cursor = db.rawQuery("SELECT nominal, jenis FROM transaksi", null)
        if (cursor.moveToFirst()) {
            do {
                val nominal = cursor.getInt(0)
                val jenis = cursor.getString(1)
                saldo += if (jenis == "Pemasukan") nominal else -nominal
            } while (cursor.moveToNext())
        }
        cursor.close()
        return saldo
    }
}
