package com.example.callfilter.adapter.input.service

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import com.example.callfilter.domain.port.input.EvaluateCallUseCase
import com.example.callfilter.domain.port.output.CallSettingsRepository
import com.example.callfilter.domain.port.output.ContactLookupPort
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
@AndroidEntryPoint
class CallScreeningServiceImpl : CallScreeningService() {

    @Inject
    lateinit var repository: CallSettingsRepository

    @Inject
    lateinit var contactLookupPort: ContactLookupPort

    override fun onScreenCall(callDetails: Call.Details) {
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) return

        val rawHandle: Uri? = callDetails.handle
        val phoneNumber = rawHandle?.schemeSpecificPart.orEmpty()

        val evaluateCallUseCase = EvaluateCallUseCase(repository, contactLookupPort)
        val shouldBlock = evaluateCallUseCase.execute(phoneNumber)

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