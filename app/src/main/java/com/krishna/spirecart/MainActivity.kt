
package com.krishna.spirecart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.krishna.spirecart.ui.theme.SpireCartTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpireCartTheme {
                ProductCatalogScreen()
            }
        }
    }
}