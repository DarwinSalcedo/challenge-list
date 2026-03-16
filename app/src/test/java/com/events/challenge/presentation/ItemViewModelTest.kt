package com.events.challenge.presentation

import com.events.challenge.domain.GetItemsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ItemViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeRepository: FakeItemRepository
    private lateinit var useCase: GetItemsUseCase
    private lateinit var viewModel: ItemViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        fakeRepository = FakeItemRepository()
        useCase = GetItemsUseCase(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given success response, when viewmodel loads, then uiState is Success`() =
        runTest(testDispatcher) {
            fakeRepository.shouldReturnError = false

            viewModel = ItemViewModel(useCase)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is ItemUiState.Success)
            val successState = state as ItemUiState.Success
            assertEquals(2, successState.items.size)
            assertEquals("Test1", successState.items[0].title)
        }

    @Test
    fun `given error response, when viewmodel loads, then uiState is Error`() =
        runTest(testDispatcher) {
            fakeRepository.shouldReturnError = true

            viewModel = ItemViewModel(useCase)

            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertTrue(state is ItemUiState.Error)
            val errorState = state as ItemUiState.Error
            assertEquals("Fake Error", errorState.message)
        }
}
