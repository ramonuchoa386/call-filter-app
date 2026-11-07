package com.example.callfilter.adapter.output

import com.example.callfilter.adapter.output.db.dao.CallLogDao
import com.example.callfilter.adapter.output.db.entity.CallLogEntity
import com.example.callfilter.domain.port.output.CallLogRepository

class RoomCallLogRepositoryImpl(
    private val callLogDao: CallLogDao
) : CallLogRepository {
    override suspend fun logCall(phoneNumber: String, status: String) {
        callLogDao.insertCallLog(
            CallLogEntity(
                phoneNumber = phoneNumber,
                timestamp = System.currentTimeMillis(),
                status = status
            )
        )
    }
}