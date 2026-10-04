# SpireCart — Android Product Catalog & Offline Cart

SpireCart is a native Android shopping application built with Kotlin and Jetpack Compose. It allows users to browse products, search the catalog, view product details, and manage a shopping cart with persistent local storage.

The application retrieves product data from the [DummyJSON Products API](https://dummyjson.com/docs/products) and uses Room to store cart items locally, allowing cart operations to continue when the device is offline.

## Features

* **Product Catalog:** Fetches and displays products from the DummyJSON API.
* **Search:** Search products by query, with debounced input to reduce unnecessary API requests.
* **Pagination:** Loads additional products on demand instead of fetching the entire catalog at once.
* **Product Details:** View product information before adding an item to the cart.
* **Shopping Cart:** Add products, increase or decrease quantities, and remove items.
* **Cart Summary:** Displays item quantities and the total price.
* **Offline Cart:** View and manage saved cart items without an internet connection.
* **Persistent Storage:** Cart contents remain available after closing and reopening the application.
* **Loading and Error States:** Handles loading indicators, API errors, and retry actions.

## Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** MVVM (Model–View–ViewModel)
* **Networking:** Retrofit
* **Asynchronous Operations:** Kotlin Coroutines
* **Local Database:** Room
* **API:** DummyJSON Products API
* **Build System:** Gradle with Kotlin DSL

## Architecture

The application follows the MVVM pattern to separate UI rendering, application state, and data operations.

* **Model:** Represents product and cart data.
* **View:** Jetpack Compose screens display the catalog, product details, and cart.
* **ViewModel:** Manages UI state, user interactions, search, pagination, and loading/error states.
* **Repository:** Coordinates data access for cart operations.
* **Remote Data Source:** Retrofit communicates with the DummyJSON API.
* **Local Data Source:** Room stores cart items and their quantities in a local database.

### Data Flow

1. The UI sends user actions to the ViewModel.
2. The ViewModel requests data through the appropriate data layer.
3. Retrofit fetches catalog data from the remote API.
4. Room persists cart data locally.
5. The ViewModel exposes state for the Compose UI to render.

## Local Storage and Offline Support

Room is used to persist cart items locally.

* Cart entries and quantities are stored in a local database.
* Quantity changes and item removal update the local cart.
* Cart data remains available after the application is closed and reopened.
* Cart operations do not require an active network connection once products have been added to the cart.

**Note:** The product catalog depends on the remote API. Offline catalog caching is not implemented, so browsing or searching products may require an internet connection.

## Setup and Build Instructions

### Prerequisites

* Android Studio
* A compatible Android device or emulator
* JDK supported by the project's Gradle configuration
* Internet connection for fetching products from DummyJSON

### Run the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/Kriishnaaaa/SpireCart.git
   ```

2. Open the `SpireCart` project in Android Studio.

3. Allow Gradle to sync and download the required dependencies.

4. Connect an Android device or start an emulator.

5. Run the `app` configuration from Android Studio.

Alternatively, build the debug APK from the project root:

```bash
./gradlew assembleDebug
```

The generated APK is typically available at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Important Design Decisions

* **Jetpack Compose:** Used for declarative UI development and state-driven rendering.
* **MVVM:** Separates UI logic from application and data responsibilities.
* **Retrofit:** Provides structured API requests using Kotlin Coroutines.
* **Room:** Keeps cart operations local and preserves cart contents across application restarts.
* **Pagination:** Fetches products in batches to avoid loading the complete catalog at once.
* **Debounced Search:** Reduces unnecessary search requests while the user types.
* **UI State Handling:** Represents loading, success, and error conditions to make the interface more predictable.

## Known Limitations

* The product catalog requires an internet connection and is not cached for offline browsing.
* Product search depends on the remote API.
* Product availability and information reflect the data returned by DummyJSON.
* Checkout, payment processing, and order placement are outside the scope of this assessment.

## API Reference

* [DummyJSON Products API](https://dummyjson.com/docs/products)
* [Kotlin Documentation](https://kotlinlang.org/docs/)
* [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
* [Retrofit Documentation](https://square.github.io/retrofit/)
* [Room Documentation](https://developer.android.com/training/data-storage/room)

## Author

**Krishna Gupta**

GitHub: [@Kriishnaaaa](https://github.com/Kriishnaaaa)
