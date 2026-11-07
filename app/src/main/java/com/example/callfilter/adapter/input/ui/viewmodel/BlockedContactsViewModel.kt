package com.example.callfilter.adapter.input.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callfilter.domain.port.input.DeleteBlockedContactUseCase
import com.example.callfilter.domain.port.input.GetBlockedContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class BlockedContactsViewModel @Inject constructor(
    private val getBlockedContactsUseCase: GetBlockedContactsUseCase,
    private val deleteBlockedContactUseCase: DeleteBlockedContactUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val contacts = _searchQuery
        .debounce(300)
        .flatMapLatest { query -> getBlockedContactsUseCase.execute(query) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun removeContact(phoneNumber: String) {
        viewModelScope.launch {
            deleteBlockedContactUseCase.execute(phoneNumber)
        }
    }
}