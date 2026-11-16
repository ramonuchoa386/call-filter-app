package com.example.callfilter.adapter.input.ui.viewmodel

import com.example.callfilter.domain.port.input.CheckPremiumStatusUseCase
import com.example.callfilter.domain.port.input.SetPremiumStatusUseCase
import com.example.callfilter.domain.port.output.PremiumRepository
import com.example.callfilter.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FakePremiumRepo : PremiumRepository {
    private var premium = false
    override fun isPremium(): Boolean = premium
    override fun setPremium(isPremium: Boolean) { premium = isPremium }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MonetizationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    @Test
    fun `deve iniciar com status nao premium`() = runTest(testDispatcher) {
        val repo = FakePremiumRepo()
        val checkUseCase = CheckPremiumStatusUseCase(repo)
        val setUseCase = SetPremiumStatusUseCase(repo)
        val viewModel = MonetizationViewModel(checkUseCase, setUseCase)

        assertFalse(viewModel.isPremium.value)
    }

    @Test
    fun `deve atualizar para premium quando adquirido`() = runTest(testDispatcher) {
        val repo = FakePremiumRepo()
        val checkUseCase = CheckPremiumStatusUseCase(repo)
        val setUseCase = SetPremiumStatusUseCase(repo)
        val viewModel = MonetizationViewModel(checkUseCase, setUseCase)

        viewModel.setPremium(true)

        assertTrue(viewModel.isPremium.value)
    }
}