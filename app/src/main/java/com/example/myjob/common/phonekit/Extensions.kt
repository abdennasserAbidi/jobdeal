package com.example.myjob.common.phonekit

import android.content.Context
import com.example.myjob.domain.entities.NewCountry
import org.json.JSONArray

internal fun String?.toCountryList(): List<NewCountry> {
    val countries = mutableListOf<NewCountry>()
    try {
        val json = JSONArray(this)
        for (i in 0 until json.length()) {
            val country = json.getJSONObject(i)
            countries.add(
                NewCountry(
                    iso2 = country.getString("iso2"),
                    name = country.getString("name"),
                    code = country.getInt("code")
                )
            )
        }
    } catch (e: Exception) {
        // ignored
    }
    return countries
}

fun Context.getFlagResource(iso2: String?): Int =
    resources.getIdentifier("country_flag_$iso2", "drawable", packageName)
