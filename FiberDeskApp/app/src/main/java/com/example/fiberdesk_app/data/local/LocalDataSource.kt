package com.example.fiberdesk_app.data.local

import android.content.Context
import com.example.fiberdesk_app.data.model.Material

class LocalDataSource(context: Context) {
    private val dbHelper = MaterialDbHelper(context.applicationContext)

    fun saveMaterial(material: Material) {
        dbHelper.insertOrUpdate(material)
    }

    fun deleteMaterial(id: String) {
        dbHelper.delete(id)
    }

    fun getAllMaterials(): List<Material> {
        return dbHelper.getAll()
    }
}
