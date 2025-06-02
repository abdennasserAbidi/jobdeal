package com.example.myjob.common

import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageHelper {

    fun updateLanguage(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)

            context.resources.updateConfiguration(
                context.resources.configuration,
                context.resources.displayMetrics
            )

        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    languageCode
                )
            )
        }
    }

    fun changeLanguage(context: Context, languageCode: String): ContextWrapper {

        var contextWrapper = context
        val configuration = context.resources.configuration
        val systemLocal = configuration.locales[0]

        if (languageCode.isNotEmpty() && languageCode != systemLocal.language) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            configuration.setLocale(locale)
            contextWrapper = context.createConfigurationContext(configuration)
        }

        return ContextWrapper(contextWrapper)
    }

}