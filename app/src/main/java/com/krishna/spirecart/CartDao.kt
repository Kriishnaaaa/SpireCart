
package com.krishna.spirecart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items ORDER BY title ASC")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Query("""
        UPDATE cart_items
        SET quantity = quantity + 1
        WHERE productId = :productId
    """)
    suspend fun increaseQuantity(productId: Int)

    @Query("""
        UPDATE cart_items
        SET quantity = quantity - 1
        WHERE productId = :productId AND quantity > 1
    """)
    suspend fun decreaseQuantity(productId: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeCartItem(productId: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}