package com.tws.moments.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tws.moments.api.MomentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule
import java.util.concurrent.Executors


@ExperimentalCoroutinesApi
class MainViewModelUnitTest {

    @Rule @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun test_fetch_tweets() {
        val momentRepository = mockk<MomentRepository>()
        coEvery {
            momentRepository.fetchTweets()
        } returns listOf()

        val mainViewModel = MainViewModel(momentRepository)
        mainViewModel.refreshTweets()

        Assert.assertEquals(0, mainViewModel.tweets.value?.size)
    }
}