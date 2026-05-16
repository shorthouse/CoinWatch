package dev.shorthouse.coinwatch.common

import android.os.SystemClock

interface TimeProvider {
    fun elapsedRealtimeMillis(): Long
}

class SystemTimeProvider : TimeProvider {
    override fun elapsedRealtimeMillis(): Long = SystemClock.elapsedRealtime()
}
