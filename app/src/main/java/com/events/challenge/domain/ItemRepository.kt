package com.events.challenge.domain

import kotlinx.coroutines.flow.Flow

interface ItemRepository {
   suspend fun getItems(): Flow<Result<List<ItemModel>>>
   val number: Int
}