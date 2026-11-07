package com.example.callfilter.domain.port.input

import com.example.callfilter.domain.port.output.CallLogRepository

class LogCallUseCase(private val repository: CallLogRepository) {
    suspend fun execute(phoneNumber: String, status: String) {
        repository.logCall(phoneNumber, status)
    }
}