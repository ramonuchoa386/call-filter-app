package com.example.callfilter.adapter.output

import android.content.SharedPreferences
import com.example.callfilter.domain.port.output.PremiumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesPremiumRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences
) : PremiumRepository {

    override fun isPremium(): Boolean {
        return prefs.getBoolean(KEY_IS_PREMIUM, false)
    }

    override fun setPremium(isPremium: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PREMIUM, isPremium).apply()
    }

    companion object {
        private const val KEY_IS_PREMIUM = "is_premium"
    }
}