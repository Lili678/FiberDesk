package com.example.fiberdesk_app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.fiberdesk_app.data.model.Material
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalDataSource(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("MaterialsCache", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveMaterial(material: Material) {
        val materials = getAllMaterials().toMutableList()
        val existingIndex = materials.indexOfFirst { it._id == material._id }
        if (existingIndex >= 0) {
            materials[existingIndex] = material
        } else {
            materials.add(material)
        }
        saveMaterials(materials)
    }

    fun getAllMaterials(): List<Material> {
        val json = prefs.getString("materials", "[]") ?: "[]"
        val type = object : TypeToken<List<Material>>() {}.type
        return gson.fromJson(json, type)
    }

    fun deleteMaterial(id: String) {
        val materials = getAllMaterials().filter { it._id != id }
        saveMaterials(materials)
    }

    private fun saveMaterials(materials: List<Material>) {
        val json = gson.toJson(materials)
        prefs.edit().putString("materials", json).apply()
    }
}
