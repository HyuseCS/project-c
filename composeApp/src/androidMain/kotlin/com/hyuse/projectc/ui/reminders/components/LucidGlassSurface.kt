package com.hyuse.projectc.ui.reminders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LucidGlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Float = 32f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(Color.White.copy(alpha = 0.7f)) // 70% opacity white
            .blur(radius = 20.dp) // Tactile Glass blur
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f), // Subtle translucent border
                shape = RoundedCornerShape(cornerRadius.dp)
            )
    ) {
        content()
    }
}
