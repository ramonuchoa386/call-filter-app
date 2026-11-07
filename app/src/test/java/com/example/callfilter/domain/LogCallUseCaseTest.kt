package com.example.callfilter.domain

import com.example.callfilter.domain.model.CallLog
import com.example.callfilter.domain.port.input.LogCallUseCase
import com.example.callfilter.domain.port.output.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeCallLogRepository : CallLogRepository {
    val logs = mutableListOf<Pair<String, String>>()
    
    override suspend fun logCall(phoneNumber: String, status: String) {
        logs.add(Pair(phoneNumber, status))
    }

    // Método adicionado para cumprir o contrato atualizado da interface
    override fun getCallLogs(): Flow<List<CallLog>> {
        return flowOf(emptyList())
    }
}

class LogCallUseCaseTest {
    @Test
    fun `deve registrar chamada com o status recebido`() = runBlocking {
        val fakeRepo = FakeCallLogRepository()
        val useCase = LogCallUseCase(fakeRepo)

        useCase.execute("+5521999999999", "BLOCKED")

        assertEquals(1, fakeRepo.logs.size)
        assertEquals("+5521999999999", fakeRepo.logs[0].first)
        assertEquals("BLOCKED", fakeRepo.logs[0].second)
    }
}