package com.example.myapplication

import VideoOverlayHelper
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.BitmapOverlay
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.StaticOverlaySettings
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import androidx.media3.transformer.Effects
import androidx.media3.transformer.Composition
import java.io.File
import androidx.core.net.toUri
import androidx.media3.transformer.ExportException

@UnstableApi
class MainActivity : ComponentActivity() {
    private lateinit var videoOverlayHelper: VideoOverlayHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoOverlayHelper = VideoOverlayHelper(this)
        setContent {
            VideoOverlayScreen()
        }
    }
    // Define permission request codes
    private val REQUEST_CODE_READ_MEDIA_IMAGES = 1
    private val REQUEST_CODE_READ_MEDIA_VIDEO = 2
    private val REQUEST_CODE_READ_MEDIA_AUDIO = 3
    fun request1stPermission() {
            // Check if the permissions are granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                // Request the permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE_READ_MEDIA_IMAGES)
            }
    }
    fun request2ndPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
            != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
                REQUEST_CODE_READ_MEDIA_VIDEO)
        }
    }

    fun request3rdPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                REQUEST_CODE_READ_MEDIA_AUDIO)
        }
    }

    @OptIn(UnstableApi::class)
    private fun processVideo() {
        // Example paths - replace with your actual video and image paths
        val videoPath = "/storage/emulated/0/Download/test.mp4"
        val imagePath = "/storage/emulated/0/Download/image.jpg"
        val outputPath = "${getExternalFilesDir(Environment.DIRECTORY_MOVIES)}/output.mp4"

        videoOverlayHelper.combineVideoWithImage(
            videoPath = videoPath,
            imagePath = imagePath,
            outputFilePath = outputPath,
            successCallback = { outputPath ->
                // Handle success
                runOnUiThread {
                    Log.d("TESSSET", "\"Video processed successfully! Output: $outputPath\": ")

                }
            },
            errorCallback = { errorMessage ->
                // Handle error
                runOnUiThread {
                    Log.d("TESSSET", "\"Error processing video: $errorMessage\": ")
                }
            }
        )
    }

    @Composable
    fun VideoOverlayScreen() {
        var status by remember { mutableStateOf("Ready to process video.") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = status)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                status = "Processing..."
                processVideo()
            }) {
                Text(text = "Process Video")
            }
            Button(onClick = {
                request1stPermission()
            }) {
                Text(text = "Request Image Permission")
            }
            Button(onClick = {
                request2ndPermission()
            }) {
                Text(text = "Request Video Permission")
            }
            Button(onClick = {
                request3rdPermission()
            }) {
                Text(text = "Request Audio Permission")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        VideoOverlayScreen()
    }
}


