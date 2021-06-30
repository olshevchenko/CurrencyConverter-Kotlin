package com.olshevchenko.currencyconverter.core

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Utility singleton for datetime- operations supporting
 */
object CurrencyDateTime {
    /** Output date-time string format */
    private val uiDatePattern = "yyyy-MM-dd HH:mm:ss" // "2021-01-01 23:59:00"
    const val UI_DATETIME_UNKNOWN = "????-??-?? ??:??:??"
    const val UI_DATETIME_IN_PROGRESS = "....-..-.. ..:..:.."

    val uiDateFormat by lazy(mode = LazyThreadSafetyMode.NONE) {
        try {
            SimpleDateFormat(uiDatePattern, Locale.getDefault())
        } catch (e: Exception) {
            Log.e("CurrencyDateTime", "SimpleDateFormat objects creation error => exit app")
            throw(e)
        }
    }
}

/**
 * String extension for currency codes -
 * splits 6-character "quotes" key to pair of 3-character currency ("From-", "To-") code
 */
fun String.splitBy3(): List<String>? =
    if (this.length == 6)
        this.chunked(3) // [0] for "From-", [1] for "To-" currency code
    else
        null

/**
 * FromToCodes extension for currency codes -
 * merges pair of 3-ch currency codes back into 6-character conversion code (i.e. "quotes" key)
 */
fun FromToCodes.mergeTo6(): String? =
    if ((this.from.length == 3) && (this.to.length == 3))
        this.from + this.to
    else
        null

/**
 * Long extension for datetime -
 * converts Epoch timestamp to datetime in suitable string form
 */
fun Long.toUIDateTime(): String =
    CurrencyDateTime.uiDateFormat.format(Date(this * 1000))


/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
