package com.hyuse.projectc.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.compositionLocalOf

val LocalDrawerState = compositionLocalOf<DrawerState> {
    error("No DrawerState provided")
}
