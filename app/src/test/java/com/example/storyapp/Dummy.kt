package com.example.storyapp

import com.example.storyapp.Data.StoryData.StoryUnit

object Dummy {
    fun generateDummy(): List<StoryUnit> {
        val items: MutableList<StoryUnit> = arrayListOf()
        for (i in 0..100) {
            val story = StoryUnit(
                i.toString(),
                "name + $i",
                "desc $i",
                "photo $i",
                "created at $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}