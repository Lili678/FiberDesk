package com.example.fiberdesk_app.utils

import android.content.Context
import com.example.fiberdesk_app.data.model.Material
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtil {
    private const val EXPORT_FILENAME = "materials_export.json"

    fun exportMaterials(context: Context, materials: List<Material>): File {
        val gson = Gson()
        val json = gson.toJson(materials)
        val file = File(context.filesDir, EXPORT_FILENAME)
        FileOutputStream(file).use { it.write(json.toByteArray(Charsets.UTF_8)) }
        return file
    }

    fun importMaterials(context: Context): List<Material> {
        val file = File(context.filesDir, EXPORT_FILENAME)
        if (!file.exists()) return emptyList()
        val json = FileInputStream(file).use { it.readBytes().toString(Charsets.UTF_8) }
        val type = object : TypeToken<List<Material>>() {}.type
        return Gson().fromJson(json, type)
    }
}
