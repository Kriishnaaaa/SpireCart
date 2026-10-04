
package com.krishna.spirecart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val quantity: Int = 1
)