package dev.shorthouse.coinwatch.common

import android.os.SystemClock
import java.time.Clock

interface TimeProvider {
    fun elapsedRealtimeMillis(): Long
    fun currentEpochMillis(): Long
}

class SystemTimeProvider(
    private val clock: Clock = Clock.systemUTC()
) : TimeProvider {
    override fun elapsedRealtimeMillis(): Long = SystemClock.elapsedRealtime()

    override fun currentEpochMillis(): Long = clock.millis()
}
