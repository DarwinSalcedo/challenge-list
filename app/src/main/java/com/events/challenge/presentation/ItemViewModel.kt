package com.events.challenge.presentation

import androidx.lifecycle.ViewModel
import com.events.challenge.domain.ItemModel
import com.events.challenge.domain.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<ItemModel>>(emptyList())
    val uiState: StateFlow<List<ItemModel>> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        _uiState.value = itemRepository.getItems()
    }
}
