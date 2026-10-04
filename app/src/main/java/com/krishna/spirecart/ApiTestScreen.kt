
package com.krishna.spirecart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ApiTestScreen() {
    var message by remember {
        mutableStateOf("Tap the button to fetch products")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message)

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
        }

        Button(
            enabled = !isLoading,
            onClick = {
                scope.launch {
                    isLoading = true
                    message = "Fetching products..."

                    try {
                        val response =
                            RetrofitClient.productApi.getProducts()

                        message =
                            "Success! Received ${response.products.size} products."
                    } catch (e: Exception) {
                        message =
                            "Request failed: ${e.message ?: "Unknown error"}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        ) {
            Text("Fetch Products")
        }
    }
}