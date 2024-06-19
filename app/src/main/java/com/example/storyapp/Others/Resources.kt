package com.example.storyapp.Others

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countResource = CountingIdlingResource(RESOURCE)

    fun decrement() {
        if (!countResource.isIdleNow) {
            countResource.decrement()
        }
    }

    fun increment() {
        countResource.increment()
    }
}