package com.example.callfilter.domain

import com.example.callfilter.domain.model.CallSettings
import com.example.callfilter.domain.port.input.EvaluateCallUseCase
import com.example.callfilter.domain.port.output.CallSettingsRepository
import com.example.callfilter.domain.port.output.ContactLookupPort
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeCallSettingsRepository(private var settings: CallSettings = CallSettings()) : CallSettingsRepository {
    override fun getSettings(): CallSettings = settings
    override fun saveSettings(settings: CallSettings) { this.settings = settings }
}

class FakeContactLookupPort(private val contacts: Set<String>) : ContactLookupPort {
    override fun isContact(phoneNumber: String): Boolean = contacts.contains(phoneNumber)
}

class EvaluateCallUseCaseTest {

    @Test
    fun `deve retornar true para bloquear chamadas desconhecidas`() {
        val repo = FakeCallSettingsRepository(CallSettings(blockUnknown = true))
        val contactLookup = FakeContactLookupPort(emptySet())
        val useCase = EvaluateCallUseCase(repo, contactLookup)

        assertTrue(useCase.execute("+5521977777777"))
    }

    @Test
    fun `deve retornar false para contatos conhecidos`() {
        val repo = FakeCallSettingsRepository(CallSettings(blockUnknown = true))
        val contactLookup = FakeContactLookupPort(setOf("+5521977777777"))
        val useCase = EvaluateCallUseCase(repo, contactLookup)

        assertFalse(useCase.execute("+5521977777777"))
    }
}