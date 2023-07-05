package com.ogzkesk.eonad.billing

import com.android.billingclient.api.AccountIdentifiers

class AccountIdentifier(private val identifiers: AccountIdentifiers?) {
    val obfuscatedAccountId: String?
        get() = identifiers?.obfuscatedAccountId
    val obfuscatedProfileId: String?
        get() = identifiers?.obfuscatedProfileId
}