package com.example.callfilter.adapter.output

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FirebaseRemoteConfigAdapter @Inject constructor() {

    private val remoteConfig by lazy {
        try {
            Firebase.remoteConfig
        } catch (e: Exception) {
            null
        }
    }

    init {
        try {
            remoteConfig?.let { config ->
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                config.setConfigSettingsAsync(configSettings)
                config.setDefaultsAsync(mapOf("admob_enabled" to true))
            }
        } catch (e: Exception) {
            // Ignora se o Firebase não estiver inicializado no contexto de testes unitários puro
        }
    }

    open fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        try {
            remoteConfig?.fetchAndActivate()?.addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            } ?: onComplete(true)
        } catch (e: Exception) {
            onComplete(true)
        }
    }

    open fun isAdMobEnabled(): Boolean {
        return try {
            remoteConfig?.getBoolean("admob_enabled") ?: true
        } catch (e: Exception) {
            true
        }
    }
}