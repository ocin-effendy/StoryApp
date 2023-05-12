package com.example.storyapp.data.remote

import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.StoryResponse

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                id = "story-qXsGtxt7EKHlm4uK",
                name = "Nox",
                description = "Bakso Solo",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1683517314243_A0Ih2ecK.jpg",
                createdAt = "2023-05-08T03:41:54.245Z",
                lat = -7.5620985,
                lon = 112.341606
            )
            storyList.add(story)
        }
        return storyList
    }
}