package com.events.challenge.domain

interface ItemRepository {
    fun getItems(): List<ItemModel>
}