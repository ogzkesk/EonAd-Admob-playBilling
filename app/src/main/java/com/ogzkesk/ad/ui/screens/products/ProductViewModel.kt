package com.ogzkesk.ad.ui.screens.products

import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogzkesk.eonad.billing.Iap
import com.ogzkesk.eonad.billing.ProductDetail
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val isUserPro = mutableStateOf(false)
    val product = mutableStateOf(0)
    val product2 = mutableStateOf(0)

    private fun getProducts() {
        val iap = Iap.getInstance()
        val subs = iap.getSubscriptions()
        val products = iap.getInAppProducts()
        _uiState.update { state -> state.copy(inapp = products, subs = subs) }
    }

    fun launchPurchase(productId: String, activity: Activity) {

        viewModelScope.launch {
            if (productId.contains("product")) {
                Iap.getInstance()
                    .purchase(activity, productId)
                    .listen{
                        if (it.purchases.isNotEmpty()) {
                            product2.value = product2.value + 50
                            Hawk.put("product", product.value + 50)
                        } else {
                            println("Iap Purchase Empty ${it.result.debugMessage}")
                        }
                    }

            } else {
                Iap.getInstance().subscribe(activity, productId)
                    .listen{
                        if (it.purchases.isNotEmpty()) {
                            Hawk.put("pro", true)
                        } else {
                            println("Iap Purchase Empty ${it.result.debugMessage}")
                        }
                    }

            }
        }
    }

    private fun checkIap() {
        println("isIapConnected :: ${Iap.getInstance().isConnected()}")
    }

    private fun setUserType() {
        isUserPro.value = Hawk.get("pro", false)
    }

    private fun checkProduct() {
        product.value = Hawk.get("product", 0)
    }

    data class UiState(
        val inapp: List<ProductDetail> = emptyList(),
        val subs: List<ProductDetail> = emptyList()
    )

    init {
        getProducts()
        setUserType()
        checkIap()
        checkProduct()
    }
}