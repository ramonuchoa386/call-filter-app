package com.example.callfilter.adapter.output

import android.content.SharedPreferences
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity
import com.example.callfilter.domain.model.CallSettings
import com.example.callfilter.domain.port.output.CallSettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RoomSettingsRepositoryImpl(
    private val prefs: SharedPreferences,
    private val blockedContactDao: BlockedContactDao
) : CallSettingsRepository {

    override fun getSettings(): CallSettings = runBlocking {
        val blockUnknown = prefs.getBoolean("block_unknown", false)
        val blockedList = blockedContactDao.getAllBlockedContacts().first()
        val excludedNumbers = blockedList.map { it.phoneNumber }.toSet()
        CallSettings(blockUnknown = blockUnknown, excludedContacts = excludedNumbers)
    }

    override fun saveSettings(settings: CallSettings) = runBlocking {
        prefs.edit().putBoolean("block_unknown", settings.blockUnknown).apply()
        
        val currentBlocked = blockedContactDao.getAllBlockedContacts().first().map { it.phoneNumber }.toSet()
        
        // Adicionar novos números
        val toAdd = settings.excludedContacts - currentBlocked
        toAdd.forEach { phone ->
            blockedContactDao.insertBlockedContact(BlockedContactEntity(phoneNumber = phone))
        }

        // Remover números excluídos
        val toRemove = currentBlocked - settings.excludedContacts
        toRemove.forEach { phone ->
            blockedContactDao.deleteBlockedContact(phone)
        }
    }
}