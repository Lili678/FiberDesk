package com.example.fiberdesk_app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fiberdesk_app.data.model.Material

class MaterialDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "fiberdesk.db"
        const val DATABASE_VERSION = 1
        const val TABLE_MATERIALS = "materials"
        const val COL_ID = "_id"
        const val COL_NOMBRE = "nombre"
        const val COL_CANTIDAD = "cantidad"
        const val COL_DESCRIPCION = "descripcion"
        const val COL_FECHA = "fechaRegistro"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val create = """
            CREATE TABLE $TABLE_MATERIALS (
                $COL_ID TEXT PRIMARY KEY,
                $COL_NOMBRE TEXT,
                $COL_CANTIDAD INTEGER,
                $COL_DESCRIPCION TEXT,
                $COL_FECHA TEXT
            )
        """.trimIndent()
        db.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MATERIALS")
        onCreate(db)
    }

    fun insertOrUpdate(material: Material) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_ID, material._id)
            put(COL_NOMBRE, material.nombre)
            put(COL_CANTIDAD, material.cantidad)
            put(COL_DESCRIPCION, material.descripcion)
            put(COL_FECHA, material.fechaRegistro)
        }
        // try update first
        val updated = db.update(TABLE_MATERIALS, cv, "$COL_ID = ?", arrayOf(material._id))
        if (updated == 0) {
            db.insert(TABLE_MATERIALS, null, cv)
        }
    }

    fun delete(id: String) {
        val db = writableDatabase
        db.delete(TABLE_MATERIALS, "$COL_ID = ?", arrayOf(id))
    }

    fun getAll(): List<Material> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_MATERIALS, null, null, null, null, null, "$COL_NOMBRE ASC")
        val list = mutableListOf<Material>()
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COL_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE))
            val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CANTIDAD))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA))
            list.add(Material(id, nombre, cantidad, descripcion, fecha))
        }
        cursor.close()
        return list
    }
}
