package com.example.callfilter.adapter.input.service

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import com.example.callfilter.adapter.output.AndroidContactLookupAdapter
import com.example.callfilter.adapter.output.SharedPreferencesSettingsRepository
import com.example.callfilter.domain.port.input.EvaluateCallUseCase

@RequiresApi(Build.VERSION_CODES.Q)
class CallScreeningServiceImpl : CallScreeningService() {
    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) return

        val rawHandle: Uri? = callDetails.handle
        val phoneNumber = rawHandle?.schemeSpecificPart.orEmpty()

        val repository = SharedPreferencesSettingsRepository(applicationContext)
        val contactLookup = AndroidContactLookupAdapter(applicationContext)
        val useCase = EvaluateCallUseCase(repository, contactLookup)

        val shouldBlock = useCase.execute(phoneNumber)

        val response = CallResponse.Builder()
        if (shouldBlock) {
            response.setDisallowCall(true).setRejectCall(true)
        }
        respondToCall(callDetails, response.build())
    }
}