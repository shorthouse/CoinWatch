package dev.shorthouse.coinwatch

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.test.runner.AndroidJUnitRunner
import coil.Coil
import coil.ImageLoader
import coil.decode.DataSource
import coil.request.SuccessResult
import dagger.hilt.android.testing.HiltTestApplication
import java.util.Locale

class HiltTestRunner : AndroidJUnitRunner() {

    private val animationScaleSettings = listOf(
        "window_animation_scale",
        "transition_animation_scale",
        "animator_duration_scale",
    )
    private val originalAnimationScales = mutableMapOf<String, String>()

    override fun onStart() {
        forceLocale()
        installFakeImageLoader()
        captureAnimationScales()
        disableAnimations()
        super.onStart()
    }

    private fun installFakeImageLoader() {
        val loader = ImageLoader.Builder(targetContext)
            .components {
                add { chain ->
                    SuccessResult(
                        drawable = ColorDrawable(Color.GRAY),
                        request = chain.request,
                        dataSource = DataSource.MEMORY,
                    )
                }
            }
            .build()
        
        Coil.setImageLoader(loader)
    }

    private fun forceLocale() {
        Locale.setDefault(Locale.US)
        val resources = targetContext.resources
        val config = Configuration(resources.configuration)
        config.setLocale(Locale.US)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        restoreAnimationScales()
        super.finish(resultCode, results)
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    private fun captureAnimationScales() {
        animationScaleSettings.forEach { setting ->
            originalAnimationScales[setting] = executeShell("settings get global $setting").trim()
        }
    }

    private fun disableAnimations() {
        animationScaleSettings.forEach { setting ->
            executeShell("settings put global $setting 0")
        }
    }

    private fun restoreAnimationScales() {
        originalAnimationScales.forEach { (setting, value) ->
            val restoreValue = if (value.isEmpty() || value == "null") "1" else value
            executeShell("settings put global $setting $restoreValue")
        }
    }

    private fun executeShell(command: String): String {
        return ParcelFileDescriptor.AutoCloseInputStream(uiAutomation.executeShellCommand(command))
            .use { it.readBytes().toString(Charsets.UTF_8) }
    }
}
