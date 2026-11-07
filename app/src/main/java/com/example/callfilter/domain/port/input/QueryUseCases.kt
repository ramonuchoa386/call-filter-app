package com.example.callfilter.domain.port.input

import com.example.callfilter.domain.model.BlockedContact
import com.example.callfilter.domain.model.CallLog
import com.example.callfilter.domain.port.output.BlockedContactRepository
import com.example.callfilter.domain.port.output.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCallLogsUseCase @Inject constructor(private val repository: CallLogRepository) {
    fun execute(filterStatus: String): Flow<List<CallLog>> {
        return repository.getCallLogs().map { logs ->
            if (filterStatus == "ALL") logs else logs.filter { it.status == filterStatus }
        }
    }
}

class GetBlockedContactsUseCase @Inject constructor(private val repository: BlockedContactRepository) {
    fun execute(query: String): Flow<List<BlockedContact>> {
        return repository.getBlockedContacts(query)
    }
}

class DeleteBlockedContactUseCase @Inject constructor(private val repository: BlockedContactRepository) {
    suspend fun execute(phoneNumber: String) {
        repository.deleteContact(phoneNumber)
    }
}