package com.example.callfilter.adapter.input.ui.viewmodel

import com.example.callfilter.domain.model.BlockedContact
import com.example.callfilter.domain.port.input.DeleteBlockedContactUseCase
import com.example.callfilter.domain.port.input.GetBlockedContactsUseCase
import com.example.callfilter.domain.port.output.BlockedContactRepository
import com.example.callfilter.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FakeBlockedContactRepository : BlockedContactRepository {
    val contacts = mutableListOf<BlockedContact>()
    override fun getBlockedContacts(query: String): Flow<List<BlockedContact>> {
        val filtered = if (query.isBlank()) contacts else contacts.filter { it.phoneNumber.contains(query) }
        return flowOf(filtered)
    }
    override suspend fun deleteContact(phoneNumber: String) { contacts.removeAll { it.phoneNumber == phoneNumber } }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BlockedContactsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    @get:Rule val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `deve aplicar debounce de 300ms na busca`() = runTest(testDispatcher) {
        val repo = FakeBlockedContactRepository().apply {
            contacts.add(BlockedContact("111", null, 1000))
            contacts.add(BlockedContact("222", null, 2000))
        }
        val viewModel = BlockedContactsViewModel(GetBlockedContactsUseCase(repo), DeleteBlockedContactUseCase(repo))
        val job = backgroundScope.launch { viewModel.contacts.collect {} }
        
        // O valor inicial ("") cai no debounce(300). Precisamos avançar o tempo virtual
        // para que a lista inicial seja emitida antes de testarmos.
        advanceTimeBy(301)
        runCurrent()
        assertEquals(2, viewModel.contacts.value.size)

        // Atualiza a busca
        viewModel.updateSearchQuery("111")
        
        advanceTimeBy(200)
        runCurrent()
        assertEquals(2, viewModel.contacts.value.size) // Antes de 300ms, a lista segue intacta

        advanceTimeBy(101)
        runCurrent()
        assertEquals(1, viewModel.contacts.value.size) // Passados 301ms, filtro aplicado
    }
}