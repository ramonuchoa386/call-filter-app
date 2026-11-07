package com.example.callfilter.adapter.input.service

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import com.example.callfilter.domain.port.input.EvaluateCallUseCase
import com.example.callfilter.domain.port.input.LogCallUseCase
import com.example.callfilter.domain.port.output.CallLogRepository
import com.example.callfilter.domain.port.output.CallSettingsRepository
import com.example.callfilter.domain.port.output.ContactLookupPort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@AndroidEntryPoint
class CallScreeningServiceImpl : CallScreeningService() {

    @Inject
    lateinit var settingsRepository: CallSettingsRepository

    @Inject
    lateinit var contactLookupPort: ContactLookupPort
    
    @Inject
    lateinit var callLogRepository: CallLogRepository

    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) return

        val rawHandle: Uri? = callDetails.handle
        val phoneNumber = rawHandle?.schemeSpecificPart.orEmpty()

        val evaluateUseCase = EvaluateCallUseCase(settingsRepository, contactLookupPort)
        val logCallUseCase = LogCallUseCase(callLogRepository)

        val shouldBlock = evaluateUseCase.execute(phoneNumber)
        val status = if (shouldBlock) "BLOCKED" else "ACCEPTED"

        // Grava no Room de forma assíncrona
        CoroutineScope(Dispatchers.IO).launch {
            logCallUseCase.execute(phoneNumber, status)
        }

        val response = CallResponse.Builder()
        if (shouldBlock) {
            response.setDisallowCall(true)
                .setRejectCall(true)
                .setSkipCallLog(false)
                .setSkipNotification(false)
        }

        respondToCall(callDetails, response.build())
    }
}
