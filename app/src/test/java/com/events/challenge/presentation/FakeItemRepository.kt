package com.events.challenge.presentation

import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeItemRepository : ItemRepository {
    var shouldReturnError = false
    var itemsToReturn = listOf(
        ItemModel("Test1", "Desc1"),
        ItemModel("Test2", "Desc2")
    )

    override val number: Int = 1

    override suspend fun getItems(): Flow<Result<List<ItemModel>>> = flow {
        if (shouldReturnError) {
            emit(Result.failure(Exception("Fake Error")))
        } else {
            emit(Result.success(itemsToReturn))
        }
    }
}