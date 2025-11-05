package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.iua.gpi.lazabus.service.interf.SttServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SttViewModel @Inject constructor(
    private val sstService : SttServiceI
) : ViewModel() {

    // El ViewModel expone directamente los Flows del Service a la UI
    val uiText: StateFlow<String> = sstService.recognizedText
    val isListening: StateFlow<Boolean> = sstService.isListening

    fun startVoiceInput() {
        sstService.startListening()
    }

    fun stopVoiceInput() {
        sstService.stopListening()
    }

    // Asegúrate de detener el reconocimiento cuando el ViewModel se borra
    override fun onCleared() {
        sstService.stopListening()
        super.onCleared()
    }
}