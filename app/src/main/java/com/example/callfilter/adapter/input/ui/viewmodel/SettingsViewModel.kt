package com.example.callfilter.adapter.input.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.callfilter.domain.port.input.ToggleBlockUnknownUseCase
import com.example.callfilter.domain.port.output.CallSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CallSettingsRepository,
    private val toggleBlockUnknownUseCase: ToggleBlockUnknownUseCase
) : ViewModel() {

    private val _blockUnknown = MutableStateFlow(repository.getSettings().blockUnknown)
    val blockUnknown = _blockUnknown.asStateFlow()

    fun toggleBlockUnknown(enabled: Boolean) {
        toggleBlockUnknownUseCase.execute(enabled)
        _blockUnknown.value = enabled
    }
}