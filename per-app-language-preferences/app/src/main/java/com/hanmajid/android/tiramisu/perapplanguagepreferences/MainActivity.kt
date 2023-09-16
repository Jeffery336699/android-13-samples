package com.hanmajid.android.tiramisu.perapplanguagepreferences

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

/**
 * 为特定App通过如下代码设置单独语言(不依赖于系统语言),App会经历一次重启，然后就会使用设置的新语言了
 *
 * 使用 Appcompat 1.6.0 或更高版本中的 setApplicationLocales() 和 getApplicationLocales() 方法。
 *      请注意，对于 Android 12（API 级别 32）及更低版本，向后兼容的 API 可与 AppCompatActivity 上下文一起使用，而非应用上下文。
 */
@SuppressLint("InlinedApi")
class MainActivity : AppCompatActivity() {
    private var localeManager: LocaleManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.wtf("LANG", "onCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            localeManager =
                getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        }
        // android13()
        androidAppCompat()
    }

    /**
     * AndroidX 提供的兼容版,注意需要appcompat库[1.6.0]以上
     */
    private fun androidAppCompat() {
        findViewById<Button>(R.id.button_lang_en).setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
            // Call this on the main thread as it may require Activity.restart()
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
        findViewById<Button>(R.id.button_lang_id).setOnClickListener {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("id-ID")
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
        findViewById<Button>(R.id.button_lang_reset).setOnClickListener {
            // 取消App单独设置的语言,也就会走values中默认的strings
            val appLocale: LocaleListCompat = LocaleListCompat.getEmptyLocaleList()
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    /**
     * Android 13系统自带的单应用独立语言
     */
    private fun android13() {
        findViewById<Button>(R.id.button_lang_en).setOnClickListener {
            localeManager?.applicationLocales = LocaleList(Locale.forLanguageTag("en"))
        }
        findViewById<Button>(R.id.button_lang_id).setOnClickListener {
            localeManager?.applicationLocales = LocaleList(Locale.forLanguageTag("id-ID"))
        }
        findViewById<Button>(R.id.button_lang_reset).setOnClickListener {
            // 取消App单独设置的语言,也就会走values中默认的strings
            localeManager?.applicationLocales = LocaleList.getEmptyLocaleList()
        }
    }

    override fun onResume() {
        super.onResume()
        // 这是单独设置操作,如果系统本身是中文简体,如果没有单独设置这里也是""(空)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val language = when (localeManager?.applicationLocales?.toLanguageTags()) {
                "en" -> "English"
                "id-ID" -> "Indonesian"
                else -> "Not Set"
            }
            Log.wtf("LANG", localeManager?.applicationLocales?.toLanguageTags())
            findViewById<TextView>(R.id.text_current_language).text =
                "Current In-App Language: $language"
        }
    }
}