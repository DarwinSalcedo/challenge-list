package com.events.challenge.presentation

import com.events.challenge.domain.ItemModel

sealed class ItemUiState {
    object Loading : ItemUiState()
    data class Success(val items: List<ItemModel>) : ItemUiState()
    data class Error(val message: String) : ItemUiState()
}

sealed class ItemAction {
    object LoadItems : ItemAction()
    data class ItemClicked(val item: ItemModel) : ItemAction()
}

sealed class ItemSideEffect {
    data class ShowToast(val message: String) : ItemSideEffect()
}
