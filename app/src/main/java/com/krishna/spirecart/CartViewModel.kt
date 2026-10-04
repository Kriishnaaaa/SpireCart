
package com.krishna.spirecart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database =
        CartDatabase.getDatabase(application)

    private val repository =
        CartRepository(database.cartDao())

    val cartItems: StateFlow<List<CartItem>> =
        repository.cartItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

    fun increaseQuantity(productId: Int) {
        viewModelScope.launch {
            repository.increaseQuantity(productId)
        }
    }

    fun decreaseQuantity(productId: Int) {
        viewModelScope.launch {
            repository.decreaseQuantity(productId)
        }
    }

    fun removeCartItem(productId: Int) {
        viewModelScope.launch {
            repository.removeCartItem(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}