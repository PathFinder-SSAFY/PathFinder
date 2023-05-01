package com.dijkstra.pathfinder.components

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dijkstra.pathfinder.ui.theme.Purple80
import com.dijkstra.pathfinder.ui.theme.nanumSquareNeo

private const val TAG = "SpeechComponents_SSAFY"

@Composable
fun SpeechDialogContent(
    transcription: MutableState<String>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "듣고 있습니다...",
            modifier = Modifier,
            fontFamily = nanumSquareNeo,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        Text(
            text = if (transcription.value == "") {
                Log.d(TAG, "if: ${transcription.value}")
                "..."
            } else {
                Log.d(TAG, "else: ${transcription.value}")
                transcription.value
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )

        CircularProgressIndicator(
            modifier = Modifier,
            color = Purple80,
        )
    } // End of Column
} // End of SpeechDialogContent


fun startListening(
    speechRecognizer: SpeechRecognizer,
    transcription: MutableState<String>,
    isRecording: MutableState<Boolean>
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        // Set the language to Korean
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(p0: Float) {}
        override fun onBufferReceived(p0: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(p0: Int) {
            Log.d(TAG, "onError: $p0")
            isRecording.value = false
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d(TAG, "onPartialResults1: $matches")
            if (matches != null && matches.isNotEmpty()) {
                transcription.value = matches[0]
                Log.d(TAG, "onPartialResults2: ${transcription.value}")
            }
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d(TAG, "onResults: ")
            if (matches != null && matches.isNotEmpty()) {
                transcription.value = matches[0]
            }
            isRecording.value = false
        }

        override fun onEvent(p0: Int, p1: Bundle?) {}
    }

    speechRecognizer.setRecognitionListener(recognitionListener)
    speechRecognizer.startListening(intent)
} // End of startListening

fun stopListening(speechRecognizer: SpeechRecognizer) {
    speechRecognizer.stopListening()
    // TODO: destroy with DisposableEffect
    speechRecognizer.destroy()
} // End of stopListening