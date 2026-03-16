package com.events.challenge.data

import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository
import javax.inject.Inject


class DefaultItemRepository @Inject constructor() : ItemRepository {

    // todo this could come from the server or local DB
    override fun getItems(): List<ItemModel> {
        return generateDummyData()
    }

    fun generateDummyData(): List<ItemModel> {
        val resultData = mutableListOf<ItemModel>()
        repeat(30) {
            resultData.add(ItemModel("Title # $it", "This is the description #$it"))
        }
        return resultData
    }
}
