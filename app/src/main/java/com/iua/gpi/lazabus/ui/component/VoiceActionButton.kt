package com.iua.gpi.lazabus.ui.component

import android.graphics.drawable.Icon
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.iua.gpi.lazabus.ui.theme.LazabusBlue
import com.iua.gpi.lazabus.R

@Composable
fun VoiceActionButton(modifier: Modifier, onClick: () -> Unit,
                      contentDescription: String = stringResource(id = R.string.voiceButtonDescription),
                      imageVector: ImageVector) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(144.dp)
            .semantics { this.contentDescription = contentDescription },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = LazabusBlue),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Crossfade (
                targetState = imageVector, // El ImageVector es el estado que cambia
                animationSpec = tween(durationMillis = 180), // Duración para la transición
                label = "IconTransition"
            ) { icon ->
                Icon(
                    imageVector = icon, // Usa el 'icon' que viene del Crossfade
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}