package com.example.myapplication

import VideoOverlayHelper
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.Manifest
import android.net.Uri

import android.os.Bundle
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
import  androidx.media3.transformer.Transformer
import androidx.media3.transformer.Effects
import androidx.media3.transformer.Composition
import java.io.File
import androidx.core.net.toUri
import androidx.media3.transformer.ExportException

class MainActivity : ComponentActivity() , Transformer.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                Text(text = "1st Permission")
            }
            Button(onClick = {
                request2ndPermission()
            }) {
                Text(text = "2nd Permission")
            }
            Button(onClick = {
                request3rdPermission()
            }) {
                Text(text = "3rd Permission")
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun processVideo() {

        val outputPath = File("/storage/emulated/0/").resolve("output_video.mp4").absolutePath

        // Create an instance of MainActivity to access its methods

        VideoOverlayHelper(applicationContext).combineVideoWithImage(
            "android.resource://packageName/raw/test.mp4",
            "android.resource://packageName/drawable/image.jpg",
            outputPath,

           errorCallback = {
                // Handle error
                println("Error: $it")

            },
            successCallback = {
                // Handle success
                println("Success: $it")

            }

        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        VideoOverlayScreen()
    }
}


