package com.example.myjob.common

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class DefaultingTypeAdapterFactory<T>(
    private val defaultValueProvider: () -> T
) : TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val delegate: TypeAdapter<T> = gson.getDelegateAdapter(this, type)
        val elementAdapter: TypeAdapter<JsonElement> = gson.getAdapter(JsonElement::class.java)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                delegate.write(out, value)
            }

            override fun read(reader: JsonReader): T {
                val jsonElement = elementAdapter.read(reader)

                // If field is null, fallback to default values
                val instance = delegate.fromJsonTree(jsonElement)
                val default = defaultValueProvider()

                return mergeWithDefault(instance, default) as T
            }
        }
    }

    private fun <T> mergeWithDefault(parsed: T?, default: T): T {
        if (parsed == null) return default

        // Reflection-based merging
        val klass = parsed!!::class
        val copy = klass.members.firstOrNull { it.name == "copy" }
        if (copy != null) {
            val params = copy.parameters.associateWith { param ->
                val value = param.name?.let { name ->
                    klass.members.firstOrNull { it.name == name }?.call(parsed)
                }
                value ?: param.name?.let { name ->
                    klass.members.firstOrNull { it.name == name }?.call(default)
                }
            }
            return copy.callBy(params) as T
        }
        return parsed
    }
}
