package com.example.callfilter.domain.model

data class CallSettings(
    val blockUnknown: Boolean = false,
    val excludedContacts: Set<String> = emptySet()
) {
    fun shouldBlock(phoneNumber: String, isContact: Boolean): Boolean {
        if (excludedContacts.contains(phoneNumber)) return true
        if (blockUnknown && !isContact) return true
        return false
    }
}