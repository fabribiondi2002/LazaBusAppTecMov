package com.iua.gpi.lazabus.ui.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.iua.gpi.lazabus.ui.screen.LazabusBlue
import com.iua.gpi.lazabus.R

@Composable
fun VoiceActionButton(modifier: Modifier, onClick: () -> Unit, contentDescription: String = stringResource(id = R.string.voiceButtonDescription)) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(96.dp)
            .semantics { this.contentDescription = contentDescription },
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = LazabusBlue),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp) // Icono de micrófono grande
            )
        }
    }
}