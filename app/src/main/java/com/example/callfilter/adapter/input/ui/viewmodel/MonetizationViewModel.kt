package com.example.callfilter.adapter.input.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.callfilter.adapter.output.FirebaseRemoteConfigAdapter
import com.example.callfilter.domain.port.input.CheckPremiumStatusUseCase
import com.example.callfilter.domain.port.input.SetPremiumStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MonetizationViewModel @Inject constructor(
    private val checkPremiumStatusUseCase: CheckPremiumStatusUseCase,
    private val setPremiumStatusUseCase: SetPremiumStatusUseCase,
    private val remoteConfigAdapter: FirebaseRemoteConfigAdapter
) : ViewModel() {

    private val _isPremium = MutableStateFlow(checkPremiumStatusUseCase.execute())
    val isPremium = _isPremium.asStateFlow()

    private val _isAdEnabled = MutableStateFlow(true)
    val isAdEnabled = _isAdEnabled.asStateFlow()

    init {
        remoteConfigAdapter.fetchAndActivate { success ->
            if (success) {
                _isAdEnabled.value = remoteConfigAdapter.isAdMobEnabled()
            }
        }
    }

    fun setPremium(isPremium: Boolean) {
        setPremiumStatusUseCase.execute(isPremium)
        _isPremium.value = isPremium
    }
}