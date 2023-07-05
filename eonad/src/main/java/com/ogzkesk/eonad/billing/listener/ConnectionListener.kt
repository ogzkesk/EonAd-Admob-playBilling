package com.ogzkesk.eonad.billing.listener

fun interface ConnectionListener {

    fun onConnectionState(connected: Boolean, disconnected: Boolean)

}