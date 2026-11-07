package com.example.callfilter.adapter.input.ui.viewmodel

import com.example.callfilter.domain.model.CallLog
import com.example.callfilter.domain.port.input.GetCallLogsUseCase
import com.example.callfilter.domain.port.output.CallLogRepository
import com.example.callfilter.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FakeCallLogRepository(private val logs: List<CallLog>) : CallLogRepository {
    override suspend fun logCall(phoneNumber: String, status: String) {}
    override fun getCallLogs(): Flow<List<CallLog>> = flowOf(logs)
}

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {
    @get:Rule val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `deve inicializar com todos os logs e permitir filtragem`() = runTest {
        val logs = listOf(
            CallLog(1, "111", 1000, "ACCEPTED"),
            CallLog(2, "222", 2000, "BLOCKED")
        )
        val viewModel = HistoryViewModel(GetCallLogsUseCase(FakeCallLogRepository(logs)))

        // Inicia a coleta no background
        val job = backgroundScope.launch { viewModel.logs.collect {} }
        
        // Garante que as corrotinas pendentes processem o flatMapLatest inicial
        runCurrent() 
        assertEquals(2, viewModel.logs.value.size)

        viewModel.setFilter("BLOCKED")
        // Garante que o flatMapLatest rode com o novo filtro
        runCurrent() 
        
        assertEquals(1, viewModel.logs.value.size)
        assertEquals("222", viewModel.logs.value.first().phoneNumber)
    }
}