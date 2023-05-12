package com.example.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String,
    ) : PagingSource<Int, ListStoryItem>() {


    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories1(
                position,
                params.loadSize,
                0,
                "Bearer $token"
            )

            val listStory = response.listStory ?: emptyList()

            LoadResult.Page(
                data = listStory.filterNotNull(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}