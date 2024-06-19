package com.example.storyapp.UI.AddStory

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.storyapp.Others.EspressoResource
import com.example.storyapp.R
import com.example.storyapp.UI.Authentication.AuthenActivity
import com.example.storyapp.UI.Main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class StoryActivityTest {

    private val desc = "Ini testing upload story arya"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() = runTest{

        IdlingRegistry.getInstance().register(EspressoResource.countResource)
        ActivityScenario.launch(AuthenActivity::class.java)
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_story)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_story)).perform((click()))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoResource.countResource)
    }

    @Test
    fun componentShowCorrectly() {
        onView(withId(R.id.img_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.et_description)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_upload)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_location)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadSuccess() {
        onView(withId(R.id.btn_camera)).perform((click()))
        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform((click()))
        onView(withId(R.id.img_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_location)).check(matches(isDisplayed()))
        onView(withId(R.id.et_description)).perform(typeText(desc))
        onView(withId(R.id.switch_location)).perform((click()))
        onView(withId(R.id.btn_upload)).perform((click()))
        onView(withId(R.id.list_story)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadError() {
        onView(withId(R.id.btn_camera)).perform((click()))
        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
        onView(withId(R.id.captureImage)).perform((click()))
        onView(withId(R.id.img_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_location)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_location)).perform((click()))
        onView(withId(R.id.btn_upload)).perform((click()))
        componentShowCorrectly()
    }
}