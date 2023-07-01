package com.ogzkesk.ad.ui.screens.products

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.ogzkesk.eonad.billing.Iap
import com.ogzkesk.eonad.billing.ProductDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private fun getProducts() {
        val iap = Iap.getInstance()
        val subs = iap.getSubscriptions()
        val products = iap.getInAppProducts()
        _uiState.update { state -> state.copy(inapp = products, subs = subs) }
    }

    fun launchPurchase(productId: String, activity: Activity) {
        Iap.getInstance().launchPurchaseFlow(productId, activity)
    }

    data class UiState(
        val inapp: List<ProductDetail> = emptyList(),
        val subs: List<ProductDetail> = emptyList()
    )

    init {
        getProducts()
    }
}