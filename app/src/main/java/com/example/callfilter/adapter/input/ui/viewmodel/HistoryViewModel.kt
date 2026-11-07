package com.example.callfilter.adapter.input.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callfilter.domain.port.input.GetCallLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModel @Inject constructor(
    private val getCallLogsUseCase: GetCallLogsUseCase
) : ViewModel() {

    private val _filter = MutableStateFlow("ALL")
    val filter = _filter.asStateFlow()

    val logs = _filter
        .flatMapLatest { status -> getCallLogsUseCase.execute(status) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setFilter(status: String) {
        _filter.value = status
    }
}