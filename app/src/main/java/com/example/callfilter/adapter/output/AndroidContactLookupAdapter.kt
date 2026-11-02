package com.example.callfilter.adapter.output

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.example.callfilter.domain.port.output.ContactLookupPort

class AndroidContactLookupAdapter(private val context: Context) : ContactLookupPort {
    override fun isContact(phoneNumber: String): Boolean {
        if (phoneNumber.isBlank()) return false
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        context.contentResolver.query(uri, arrayOf(ContactsContract.PhoneLookup._ID), null, null, null)?.use { cursor ->
            return cursor.count > 0
        }
        return false
    }
}