package com.events.challenge.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(): Flow<Result<List<ItemModel>>> {
        return repository.getItems()
    }
}
