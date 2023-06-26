package com.ogzkesk.eonad

class EonAdConfig (val provider: String) {

    var deviceTestIds: List<String> = emptyList()

    fun setListDeviceTest(list: List<String>) {
        this.deviceTestIds = list
    }

    companion object{
        const val PROVIDER_ADMOB = ""
        const val PROVIDER_FACEBOOK = ""
        const val PROVIDER_MAX = ""
    }
}

