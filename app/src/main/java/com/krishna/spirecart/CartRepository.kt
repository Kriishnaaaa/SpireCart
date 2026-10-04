
package com.krishna.spirecart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CartRepository(
    private val cartDao: CartDao
) {
    val cartItems: Flow<List<CartItem>> =
        cartDao.getAllCartItems()

    suspend fun addToCart(product: Product) {
        val existingItems = cartDao.getAllCartItems().first()
        val existingItem = existingItems.find {
            it.productId == product.id
        }

        if (existingItem != null) {
            cartDao.increaseQuantity(product.id)
        } else {
            cartDao.insertCartItem(
                CartItem(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    thumbnail = product.thumbnail,
                    quantity = 1
                )
            )
        }
    }

    suspend fun increaseQuantity(productId: Int) {
        cartDao.increaseQuantity(productId)
    }

    suspend fun decreaseQuantity(productId: Int) {
        val existingItem = cartDao.getAllCartItems()
            .first()
            .find { it.productId == productId }

        if (existingItem == null) return

        if (existingItem.quantity <= 1) {
            cartDao.removeCartItem(productId)
        } else {
            cartDao.decreaseQuantity(productId)
        }
    }

    suspend fun removeCartItem(productId: Int) {
        cartDao.removeCartItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}