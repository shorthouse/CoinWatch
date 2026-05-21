package dev.shorthouse.coinwatch.rule

import org.junit.rules.ExternalResource
import java.util.Locale

class LocaleRule(private val locale: Locale = Locale.US) : ExternalResource() {

    private var savedFormatLocale: Locale? = null

    override fun before() {
        savedFormatLocale = Locale.getDefault(Locale.Category.FORMAT)
        Locale.setDefault(Locale.Category.FORMAT, locale)
    }

    override fun after() {
        savedFormatLocale?.let { Locale.setDefault(Locale.Category.FORMAT, it) }
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
        Locale.setDefault(displayLocale)
        Locale.setDefault(Locale.Category.DISPLAY, displayLocale)
        Locale.setDefault(Locale.Category.FORMAT, formatLocale)
        try {
            return block()
        } finally {
            Locale.setDefault(previousDefault)
            Locale.setDefault(Locale.Category.DISPLAY, previousDisplay)
            Locale.setDefault(Locale.Category.FORMAT, previousFormat)
        }
    }
}
