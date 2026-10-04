
package com.krishna.spirecart

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogScreen(
    productViewModel: ProductViewModel = viewModel()
) {
    val uiState by productViewModel.uiState.collectAsStateWithLifecycle()
    val cartViewModel: CartViewModel = viewModel()
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()

    var selectedProduct by remember {
        mutableStateOf<Product?>(null)
    }

    var showCart by remember {
        mutableStateOf(false)
    }

    val totalCartItems = cartItems.sumOf { it.quantity }

    BackHandler(enabled = showCart || selectedProduct != null) {
        if (selectedProduct != null) {
            selectedProduct = null
        } else if (showCart) {
            showCart = false
        }
    }

    if (showCart) {
        CartScreen(
            onBack = { showCart = false },
            cartViewModel = cartViewModel
        )
        return
    }

    val product = selectedProduct
    if (product != null) {
        ProductDetailsScreen(
            product = product,
            quantity = cartItems
                .firstOrNull { it.productId == product.id }
                ?.quantity ?: 0,
            onBack = { selectedProduct = null },
            onAddToCart = { cartViewModel.addToCart(it) },
            onIncreaseQuantity = { cartViewModel.addToCart(it) },
            onDecreaseQuantity = {
                cartViewModel.decreaseQuantity(it.id)
            }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SpireCart",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Find something you love",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (totalCartItems > 0) {
                                Badge {
                                    Text(
                                        if (totalCartItems > 99) "99+"
                                        else totalCartItems.toString()
                                    )
                                }
                            }
                        }
                    ) {
                        Button(
                            onClick = { showCart = true },
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping cart"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Cart")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = productViewModel::onSearchQueryChanged,
                label = { Text("Search products") },
                placeholder = { Text("Try headphones, skincare...") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            )

            when {
                uiState.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text("Loading products...")
                    }
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Unable to load products",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = uiState.error!!,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = productViewModel::retry) {
                            Text("Try Again")
                        }
                    }
                }

                uiState.products.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (uiState.query.isBlank()) {
                                "No products available"
                            } else {
                                "No products found"
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (uiState.query.isBlank()) {
                            Spacer(Modifier.height(12.dp))
                            Button(onClick = productViewModel::retry) {
                                Text("Retry")
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = if (uiState.query.isBlank()) {
                            "Explore products"
                        } else {
                            "Search results"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${uiState.products.size} of ${uiState.total} products",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = uiState.products,
                            key = { product -> product.id }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = {
                                    selectedProduct = product
                                }
                            )
                        }

                        item {
                            if (uiState.hasMore) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (uiState.loadMoreError != null) {
                                        Text(
                                            text = uiState.loadMoreError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(Modifier.height(8.dp))
                                    }

                                    Button(
                                        onClick = productViewModel::loadMore,
                                        enabled = !uiState.isLoadingMore
                                    ) {
                                        if (uiState.isLoadingMore) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(18.dp),
                                                strokeWidth = 2.dp
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text("Loading...")
                                        } else {
                                            Text(
                                                if (uiState.loadMoreError != null) {
                                                    "Try Again"
                                                } else {
                                                    "Load More Products"
                                                }
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = "${uiState.products.size} of ${uiState.total} products loaded",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else if (uiState.products.isNotEmpty()) {
                                Text(
                                    text = "You've reached the end!",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No image", style = MaterialTheme.typography.bodySmall)
                    }
                },
                modifier = Modifier
                    .size(width = 112.dp, height = 112.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = product.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "$${"%.2f".format(java.util.Locale.US, product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "View details →",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}