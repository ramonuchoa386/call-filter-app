package com.example.callfilter.adapter.output

import com.example.callfilter.adapter.output.db.dao.CallLogDao
import com.example.callfilter.adapter.output.db.entity.CallLogEntity
import com.example.callfilter.domain.model.CallLog
import com.example.callfilter.domain.port.output.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCallLogRepositoryImpl(private val callLogDao: CallLogDao) : CallLogRepository {
    override suspend fun logCall(phoneNumber: String, status: String) {
        callLogDao.insertCallLog(
            CallLogEntity(phoneNumber = phoneNumber, timestamp = System.currentTimeMillis(), status = status)
        )
    }

    override fun getCallLogs(): Flow<List<CallLog>> {
        return callLogDao.getCallLogs().map { list ->
            list.map { CallLog(it.id, it.phoneNumber, it.timestamp, it.status) }
        }
    }
}