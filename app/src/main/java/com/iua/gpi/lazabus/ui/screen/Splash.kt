package com.iua.gpi.lazabus.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iua.gpi.lazabus.R
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue

/**
 * Composable del splash.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit, viewModel: TtsViewModel = hiltViewModel())
{
    val isTtsReady by viewModel.isTtsReady.collectAsState()
    LaunchedEffect(isTtsReady) {
        delay(1000)
        onTimeout()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        contentAlignment = Alignment.Center
    )
    {
        Image (
            modifier = Modifier
                .size(350.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit
        )

    }
}