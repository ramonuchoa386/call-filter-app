package com.example.callfilter.domain.port.output

interface PremiumRepository {
    fun isPremium(): Boolean
    fun setPremium(isPremium: Boolean)
}