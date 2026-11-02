package com.example.callfilter.domain

import com.example.callfilter.domain.model.CallSettings
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CallSettingsTest {

    @Test
    fun `deve bloquear numero desconhecido quando opcao de bloquear nao contatos estiver ativa`() {
        val settings = CallSettings(blockUnknown = true)
        assertTrue(settings.shouldBlock("+5521999999999", isContact = false))
    }

    @Test
    fun `deve permitir numero de contato quando opcao de bloquear nao contatos estiver ativa`() {
        val settings = CallSettings(blockUnknown = true)
        assertFalse(settings.shouldBlock("+5521988888888", isContact = true))
    }

    @Test
    fun `deve bloquear contato especifico quando o numero estiver na lista de excecao`() {
        val settings = CallSettings(blockUnknown = false, excludedContacts = setOf("+5521988888888"))
        assertTrue(settings.shouldBlock("+5521988888888", isContact = true))
    }
}