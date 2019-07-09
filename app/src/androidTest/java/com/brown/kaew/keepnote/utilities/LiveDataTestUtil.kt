package com.brown.kaew.keepnote.utilities

import androidx.lifecycle.LiveData
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Throws(InterruptedException::class)
fun <T> getLastValue(liveData: LiveData<List<T>>): T {
    var data = listOf<T>()
    val latch = CountDownLatch(1)
    liveData.observeForever {
        data = it
        latch.countDown()
    }
    latch.await(2, TimeUnit.SECONDS)

    return data[data.size-1]
}