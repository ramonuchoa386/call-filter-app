package com.example.callfilter.adapter.output

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.example.callfilter.domain.port.output.PremiumRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayBillingAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val premiumRepository: PremiumRepository
) {

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    fun startConnection(onConnected: () -> Unit) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    checkExistingPurchases()
                    onConnected()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Tentar reconexão se necessário
            }
        })
    }

    private fun checkExistingPurchases() {
        billingClient.queryPurchasesAsync(
            com.android.billingclient.api.QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { _, purchases ->
            val isPremium = purchases.any { it.products.contains("remove_ads_product") && it.purchaseState == Purchase.PurchaseState.PURCHASED }
            premiumRepository.setPremium(isPremium)
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (purchase.products.contains("remove_ads_product")) {
                premiumRepository.setPremium(true)
            }
        }
    }

    fun launchBillingFlow(activity: Activity, skuId: String = "remove_ads_product") {
        // Implementação do fluxo de compra padrão do BillingClient
    }
}