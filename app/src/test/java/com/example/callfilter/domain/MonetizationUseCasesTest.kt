package com.example.callfilter.domain

import com.example.callfilter.domain.port.input.CheckPremiumStatusUseCase
import com.example.callfilter.domain.port.input.SetPremiumStatusUseCase
import com.example.callfilter.domain.port.output.PremiumRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FakePremiumRepository : PremiumRepository {
    private var isPremiumStatus: Boolean = false

    override fun isPremium(): Boolean = isPremiumStatus

    override fun setPremium(isPremium: Boolean) {
        isPremiumStatus = isPremium
    }
}

class MonetizationUseCasesTest {

    @Test
    fun `deve retornar falso para status premium inicialmente`() {
        val repository = FakePremiumRepository()
        val useCase = CheckPremiumStatusUseCase(repository)

        assertFalse(useCase.execute())
    }

    @Test
    fun `deve atualizar e retornar verdadeiro quando o usuario adquirir a versao premium`() {
        val repository = FakePremiumRepository()
        val checkUseCase = CheckPremiumStatusUseCase(repository)
        val setUseCase = SetPremiumStatusUseCase(repository)

        setUseCase.execute(true)

        assertTrue(checkUseCase.execute())
    }
}