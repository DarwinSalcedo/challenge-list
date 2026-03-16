package com.events.challenge.presentation

import androidx.lifecycle.ViewModel
import com.events.challenge.domain.GetItemsUseCase
import com.events.challenge.domain.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ItemSideEffect>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sideEffect = _sideEffect

    init {
        handleAction(ItemAction.LoadItems)
    }

    fun handleAction(action: ItemAction) {
        when (action) {
            is ItemAction.LoadItems -> loadItems()
            is ItemAction.ItemClicked -> {
                viewModelScope.launch {
                    _sideEffect.emit(ItemSideEffect.ShowToast("Display toast :: ${action.item.title}"))
                }
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            _uiState.value = ItemUiState.Loading
            getItemsUseCase().collect { result ->
                result.fold(
                    onSuccess = { items ->
                        if (items.isEmpty()) {
                            _uiState.value = ItemUiState.Error("No items found")
                        } else {
                            _uiState.value = ItemUiState.Success(items)
                        }
                    },
                    onFailure = { error ->
                        _uiState.value =
                            ItemUiState.Error(error.message ?: "Unknown error occurred")
                    }
                )
            }
        }
    }
}
