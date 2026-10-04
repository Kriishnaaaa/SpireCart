
package com.krishna.spirecart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val loadMoreError: String? = null,
    val query: String = "",
    val total: Int = 0
) {
    val hasMore: Boolean
        get() = products.size < total
}

class ProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductUiState(isLoading = true)
    )

    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var requestJob: Job? = null

    private val pageSize = 30

    init {
        loadProducts()
    }

    fun loadProducts() {
        searchJob?.cancel()
        requestJob?.cancel()

        _uiState.value = ProductUiState(
            isLoading = true
        )

        requestJob = viewModelScope.launch {
            try {
                val response = RetrofitClient.productApi.getProducts(
                    limit = pageSize,
                    skip = 0
                )

                _uiState.value = ProductUiState(
                    products = response.products,
                    total = response.total
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "We couldn't load products. Check your internet connection and try again."
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        requestJob?.cancel()

        _uiState.value = ProductUiState(
            query = query
        )

        if (query.isBlank()) {
            loadProducts()
            return
        }

        searchJob = viewModelScope.launch {
            delay(350)

            if (_uiState.value.query != query) return@launch

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            requestJob = viewModelScope.launch {
                try {
                    val response =
                        RetrofitClient.productApi.searchProducts(
                            query = query.trim(),
                            limit = pageSize,
                            skip = 0
                        )

                    if (_uiState.value.query == query) {
                        _uiState.value = _uiState.value.copy(
                            products = response.products,
                            total = response.total,
                            isLoading = false,
                            error = null
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    if (_uiState.value.query == query) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "We couldn't load products. Check your internet connection and try again."
                        )
                    }
                }
            }
        }
    }

    fun loadMore() {
        val current = _uiState.value

        if (
            current.isLoading ||
            current.isLoadingMore ||
            !current.hasMore ||
            current.error != null
        ) {
            return
        }

        val query = current.query
        val skip = current.products.size

        _uiState.value = current.copy(
            isLoadingMore = true,
            loadMoreError = null
        )

        requestJob = viewModelScope.launch {
            try {
                val response = if (query.isBlank()) {
                    RetrofitClient.productApi.getProducts(
                        limit = pageSize,
                        skip = skip
                    )
                } else {
                    RetrofitClient.productApi.searchProducts(
                        query = query.trim(),
                        limit = pageSize,
                        skip = skip
                    )
                }

                if (_uiState.value.query != query) {
                    return@launch
                }

                val latest = _uiState.value

                _uiState.value = latest.copy(
                    products = (
                            latest.products + response.products
                            ).distinctBy { it.id },
                    total = response.total,
                    isLoadingMore = false,
                    loadMoreError = null
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (_uiState.value.query == query) {
                    _uiState.value = _uiState.value.copy(
                        isLoadingMore = false,
                        loadMoreError = "Couldn't load more products. Please try again."
                    )
                }
            }
        }
    }

    fun retry() {
        val query = _uiState.value.query

        if (query.isBlank()) {
            loadProducts()
        } else {
            onSearchQueryChanged(query)
        }
    }
}