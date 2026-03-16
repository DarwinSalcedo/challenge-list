package com.events.challenge.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetItemsUseCaseTest {

    private lateinit var mockRepository: ItemRepository
    private lateinit var useCase: GetItemsUseCase

    @Before
    fun setUp() {
        mockRepository = mockk()
        useCase = GetItemsUseCase(mockRepository)
    }

    @Test
    fun `invoke should call repository getItems and return same success flow`() = runTest {
        val mockItems = listOf(
            ItemModel("Test1", "Desc1"),
            ItemModel("Test2", "Desc2")
        )
        val expectedResult = Result.success(mockItems)
        val flowToReturn = flowOf(expectedResult)
        
        coEvery { mockRepository.getItems() } returns flowToReturn

        val resultFlow = useCase()
        val actualResult = resultFlow.first()

        coVerify(exactly = 1) { mockRepository.getItems() }
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `invoke should call repository getItems and return same error flow`() = runTest {
        val expectedResult = Result.failure<List<ItemModel>>(Exception("Error"))
        val flowToReturn = flowOf(expectedResult)
        
        coEvery { mockRepository.getItems() } returns flowToReturn

        val resultFlow = useCase()
        val actualResult = resultFlow.first()

        coVerify(exactly = 1) { mockRepository.getItems() }
        assertEquals(expectedResult, actualResult)
    }
}
