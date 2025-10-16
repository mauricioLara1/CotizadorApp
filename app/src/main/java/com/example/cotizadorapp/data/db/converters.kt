package com.example.cotizadorapp.data.db

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.example.cotizadorapp.model.Item

object Converters {

    private val moshi = Moshi.Builder().build()

    // 🔹 Correcto: definimos el tipo de lista de Items
    private val type = Types.newParameterizedType(List::class.java, Item::class.java)

    // 🔹 Creamos un adaptador para convertir entre JSON <-> List<Item>
    private val adapter = moshi.adapter<List<Item>>(type)

    @TypeConverter
    @JvmStatic
    fun fromItemsList(items: List<Item>?): String {
        return adapter.toJson(items ?: emptyList())
    }

    @TypeConverter
    @JvmStatic
    fun toItemsList(json: String?): List<Item> {
        return if (json.isNullOrEmpty()) emptyList() else adapter.fromJson(json) ?: emptyList()
    }
}
