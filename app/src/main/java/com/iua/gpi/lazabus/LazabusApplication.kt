package com.iua.gpi.lazabus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LazabusApplication : Application() {
    // Hilt hace el trabajo pesado.
}