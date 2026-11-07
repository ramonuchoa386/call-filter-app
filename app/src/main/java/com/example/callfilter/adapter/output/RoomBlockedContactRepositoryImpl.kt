package com.example.callfilter.adapter.output

import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.domain.model.BlockedContact
import com.example.callfilter.domain.port.output.BlockedContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomBlockedContactRepositoryImpl(private val dao: BlockedContactDao) : BlockedContactRepository {
    override fun getBlockedContacts(query: String): Flow<List<BlockedContact>> {
        return dao.getAllBlockedContacts().map { list ->
            val mapped = list.map { BlockedContact(it.phoneNumber, it.name, it.addedAt) }
            if (query.isBlank()) mapped
            else mapped.filter { it.phoneNumber.contains(query, true) || it.name?.contains(query, true) == true }
        }
    }

    override suspend fun deleteContact(phoneNumber: String) {
        dao.deleteBlockedContact(phoneNumber)
    }
}