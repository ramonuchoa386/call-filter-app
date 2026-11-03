package com.example.callfilter.adapter.output.db.migration

import android.content.SharedPreferences
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity

class PreferencesToRoomMigrator(
    private val sharedPreferences: SharedPreferences,
    private val blockedContactDao: BlockedContactDao
) {

    suspend fun migrateIfNeeded() {
        val legacyContacts = sharedPreferences.getStringSet(KEY_EXCLUDED_CONTACTS, null)
        if (!legacyContacts.isNullOrEmpty()) {
            legacyContacts.forEach { phoneNumber ->
                blockedContactDao.insertBlockedContact(
                    BlockedContactEntity(
                        phoneNumber = phoneNumber,
                        name = null,
                        addedAt = System.currentTimeMillis()
                    )
                )
            }
            // Remove a chave legada após a migração bem-sucedida
            sharedPreferences.edit().remove(KEY_EXCLUDED_CONTACTS).apply()
        }
    }

    companion object {
        private const val KEY_EXCLUDED_CONTACTS = "excluded_contacts"
    }
}