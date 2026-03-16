package com.events.challenge.data

import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository


class DefaultItemRepository : ItemRepository {
    // todo this could come from the server or local DB
    override fun getItems(): List<ItemModel> {
        return listOf(
            ItemModel("one", "description"),
            ItemModel("two", "description"),
            ItemModel("three", "description"),
            ItemModel("four", "description")
        )
    }
}
