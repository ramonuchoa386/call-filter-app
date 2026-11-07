package com.example.callfilter.domain.port.input

import com.example.callfilter.domain.port.output.CallSettingsRepository
import com.example.callfilter.domain.port.output.ContactLookupPort
import javax.inject.Inject

class EvaluateCallUseCase @Inject constructor(
    private val repository: CallSettingsRepository,
    private val contactLookup: ContactLookupPort
) {
    fun execute(phoneNumber: String): Boolean {
        val settings = repository.getSettings()
        val isContact = contactLookup.isContact(phoneNumber)
        return settings.shouldBlock(phoneNumber, isContact)
    }
}

class ToggleBlockUnknownUseCase @Inject constructor(private val repository: CallSettingsRepository) {
    fun execute(enabled: Boolean) {
        val current = repository.getSettings()
        repository.saveSettings(current.copy(blockUnknown = enabled))
    }
}

class ToggleContactExclusionUseCase @Inject constructor(private val repository: CallSettingsRepository) {
    fun execute(phoneNumber: String, block: Boolean) {
        val current = repository.getSettings()
        val updated = current.excludedContacts.toMutableSet()
        if (block) updated.add(phoneNumber) else updated.remove(phoneNumber)
        repository.saveSettings(current.copy(excludedContacts = updated))
    }
}