package com.example.callfilter.domain.port.input

import com.example.callfilter.domain.port.output.PremiumRepository
import javax.inject.Inject

class CheckPremiumStatusUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository
) {
    fun execute(): Boolean {
        return premiumRepository.isPremium()
    }
}

class SetPremiumStatusUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository
) {
    fun execute(isPremium: Boolean) {
        premiumRepository.setPremium(isPremium)
    }
}