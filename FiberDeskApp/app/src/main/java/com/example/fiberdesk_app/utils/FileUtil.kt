package com.example.fiberdesk_app.utils

import android.content.Context
import com.example.fiberdesk_app.data.model.Material
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object FileUtil {
    private val gson = Gson()

    fun exportMaterials(context: Context, materials: List<Material>): File {
        val json = gson.toJson(materials)
        val file = File(context.getExternalFilesDir(null), "materials_export.json")
        file.writeText(json)
        return file
    }

    fun importMaterials(context: Context): List<Material> {
        val file = File(context.getExternalFilesDir(null), "materials_export.json")
        if (!file.exists()) return emptyList()
        
        val json = file.readText()
        val type = object : TypeToken<List<Material>>() {}.type
        return gson.fromJson(json, type)
    }
}
