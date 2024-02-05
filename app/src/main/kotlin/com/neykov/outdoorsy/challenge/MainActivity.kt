package com.neykov.outdoorsy.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.neykov.outdoorsy.challenge.ui.listing.RentalsListingScreen
import com.neykov.outdoorsy.challenge.ui.theme.OutdoorsyChallengeTheme

/**
 * Main UI component of the application
 *
 * A system container for displaying the application's user interface.
 *
 * @see RentalsListingScreen
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OutdoorsyChallengeTheme {
                RentalsListingScreen()
            }
        }
    }
}
