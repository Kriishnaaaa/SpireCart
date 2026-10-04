
package com.krishna.spirecart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems
        .collectAsStateWithLifecycle()

    val total = cartItems.sumOf {
        it.price * it.quantity
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text("Your cart is empty")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = cartItems,
                        key = { it.productId }
                    ) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {
                                AsyncImage(
                                    model = item.thumbnail,
                                    contentDescription = item.title,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(item.title)
                                Text("Price: $${item.price}")
                                Text(
                                    "Subtotal: $${
                                        "%.2f".format(
                                            item.price * item.quantity
                                        )
                                    }"
                                )

                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if(item.quantity > 1) {
                                                cartViewModel
                                                    .decreaseQuantity(
                                                        item.productId
                                                    )
                                            }else{
                                                cartViewModel
                                                    .removeCartItem(
                                                        item.productId
                                                    )
                                            }
                                        }
                                    ) {
                                        Text("−")
                                    }

                                    Text(
                                        "Qty: ${item.quantity}"
                                    )

                                    Button(
                                        onClick = {
                                            cartViewModel
                                                .increaseQuantity(
                                                    item.productId
                                                )
                                        }
                                    ) {
                                        Text("+")
                                    }

                                    Button(
                                        onClick = {
                                            cartViewModel
                                                .removeCartItem(
                                                    item.productId
                                                )
                                        }
                                    ) {
                                        Text("Remove")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(4.dp))

                Text("Total: $${"%.2f".format(total)}")

                Button(
                    onClick = { cartViewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Cart")
                }
            }
        }
    }
}