package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.Adapter.StoryAdapter
import com.example.storyapp.Data.AuthData.AuthRepo
import com.example.storyapp.Data.StoryData.StoryRepo
import com.example.storyapp.Data.StoryData.StoryUnit
import com.example.storyapp.UI.Main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcher()

    @Mock
    private lateinit var authRepository: AuthRepo

    @Mock
    private lateinit var storyRepository: StoryRepo

    @Test
    fun whenGetStoryShouldNotNullAndReturnData() = runTest {
        val dummyStory = Dummy.generateDummy()
        val data: PagingData<StoryUnit> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryUnit>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(storyRepository, authRepository)
        val actualStory: PagingData<StoryUnit> = mainViewModel.listStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun whenGetStoryEmptyShouldReturnNoData() = runTest {
        val data: PagingData<StoryUnit> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryUnit>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getAllStories()).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(storyRepository, authRepository)
        val actualStory: PagingData<StoryUnit> = mainViewModel.listStory.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryUnit>>>() {
    companion object {
        fun snapshot(items: List<StoryUnit>): PagingData<StoryUnit> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryUnit>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryUnit>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}