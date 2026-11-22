package com.example.callfilter.adapter.output

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.callfilter.domain.port.output.PremiumRepository
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlayBillingAdapterTest {

    @Test
    fun `deve construir o adapter de billing sem lancar excecao`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val adapter = PlayBillingAdapter(context, FakePremiumRepositoryForAdapterSmokeTest())

        assertNotNull(adapter)
    }

    private class FakePremiumRepositoryForAdapterSmokeTest : PremiumRepository {
        private var premium = false

        override fun isPremium(): Boolean = premium

        override fun setPremium(isPremium: Boolean) {
            premium = isPremium
        }
    }
}
