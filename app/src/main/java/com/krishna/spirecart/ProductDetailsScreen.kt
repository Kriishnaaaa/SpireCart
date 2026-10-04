
package com.krishna.spirecart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    product: Product,
    quantity: Int,
    onBack: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onIncreaseQuantity: (Product) -> Unit,
    onDecreaseQuantity: (Product) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.titleLarge
            )

            Text("Rating: ${product.rating} / 5")
            Text("Category: ${product.category}")
            Text("Brand: ${product.brand ?: "N/A"}")
            Text("Stock: ${product.stock}")

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (quantity == 0) {
                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Cart")
                }
            } else {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.CheckCircle,
//                            contentDescription = null,
//                            tint = Color(0xFF2E7D32)
//                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "In your cart",
                            color = Color(0xFF2E7D32),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onDecreaseQuantity(product) },
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 0.dp
                            )
                        ) {
                            Text("−")
                        }

                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Button(
                            onClick = { onIncreaseQuantity(product) },
                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 0.dp
                            )
                        ) {
                            Text("+")
                        }
                    }
                }
            }
        }
    }
}