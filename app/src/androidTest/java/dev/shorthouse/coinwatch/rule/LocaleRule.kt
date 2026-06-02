package dev.shorthouse.coinwatch.rule

import org.junit.rules.ExternalResource
import java.util.Locale

class LocaleRule(private val locale: Locale = Locale.US) : ExternalResource() {

    private lateinit var savedDefaultLocale: Locale
    private lateinit var savedDisplayLocale: Locale
    private lateinit var savedFormatLocale: Locale

    override fun before() {
        savedDefaultLocale = Locale.getDefault()
        savedDisplayLocale = Locale.getDefault(Locale.Category.DISPLAY)
        savedFormatLocale = Locale.getDefault(Locale.Category.FORMAT)
        setLocales(displayLocale = Locale.US, formatLocale = locale)
    }

    override fun after() {
        Locale.setDefault(savedDefaultLocale)
        Locale.setDefault(Locale.Category.DISPLAY, savedDisplayLocale)
        Locale.setDefault(Locale.Category.FORMAT, savedFormatLocale)
    }

    fun <T> withLocale(locale: Locale, block: () -> T): T {
        val previous = Locale.getDefault(Locale.Category.FORMAT)
        Locale.setDefault(Locale.Category.FORMAT, locale)
        try {
            return block()
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, previous)
        }
    }

    fun <T> withLocales(displayLocale: Locale, formatLocale: Locale, block: () -> T): T {
        val previousDefault = Locale.getDefault()
        val previousDisplay = Locale.getDefault(Locale.Category.DISPLAY)
        val previousFormat = Locale.getDefault(Locale.Category.FORMAT)
        setLocales(displayLocale = displayLocale, formatLocale = formatLocale)
        try {
            return block()
        } finally {
            Locale.setDefault(previousDefault)
            Locale.setDefault(Locale.Category.DISPLAY, previousDisplay)
            Locale.setDefault(Locale.Category.FORMAT, previousFormat)
        }
    }

    private fun setLocales(displayLocale: Locale, formatLocale: Locale) {
        Locale.setDefault(displayLocale)
        Locale.setDefault(Locale.Category.DISPLAY, displayLocale)
        Locale.setDefault(Locale.Category.FORMAT, formatLocale)
    }
}
