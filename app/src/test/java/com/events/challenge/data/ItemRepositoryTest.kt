package com.events.challenge.data

import com.events.challenge.domain.ItemRepository
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemRepositoryTest {

    private lateinit var repository: ItemRepository


    @Before
    fun setUp() {
        repository = spyk(DefaultItemRepository(Dispatchers.IO))
    }

    @Test
    fun `given a valid number, when getItems is called, then it returns success with list`() =
        runTest {
            val validNumber = 2
            every { repository.number } returns validNumber

            val result = repository.getItems().last()


            assertTrue(result.isSuccess)
            val actualList = result.getOrNull()!!
            assertEquals(validNumber, actualList.size)
            assertTrue(actualList.last().title.contains((validNumber -1).toString()))
        }

    @Test
    fun `given a bomb number, when getItems is called, then it returns failure`() = runTest {
        val bombNumber = 10
        every { repository.number } returns bombNumber

        val result = repository.getItems().first()

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is Exception)
    }
}
