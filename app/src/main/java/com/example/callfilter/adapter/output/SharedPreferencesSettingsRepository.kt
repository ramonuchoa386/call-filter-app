package com.example.callfilter.adapter.output

import android.content.Context
import com.example.callfilter.domain.model.CallSettings
import com.example.callfilter.domain.port.output.CallSettingsRepository

class SharedPreferencesSettingsRepository(context: Context) : CallSettingsRepository {
    private val prefs = context.getSharedPreferences("call_filter_prefs", Context.MODE_PRIVATE)

    override fun getSettings(): CallSettings {
        val blockUnknown = prefs.getBoolean("block_unknown", false)
        val excluded = prefs.getStringSet("excluded_contacts", emptySet()) ?: emptySet()
        return CallSettings(blockUnknown, excluded)
    }

    override fun saveSettings(settings: CallSettings) {
        prefs.edit()
            .putBoolean("block_unknown", settings.blockUnknown)
            .putStringSet("excluded_contacts", settings.excludedContacts)
            .apply()
    }
}