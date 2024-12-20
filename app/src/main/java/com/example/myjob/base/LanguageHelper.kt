package com.example.myjob.base

import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageHelper {

    fun updateLanguage(context: Context, languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)

            // Update UI or resources if necessary without recreating the activity.

            context.resources.updateConfiguration(
                context.resources.configuration,
                context.resources.displayMetrics
            )

            //context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

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