package com.example.myapplication

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.Manifest

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
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import  androidx.media3.transformer.Transformer
import androidx.media3.transformer.Effects
import androidx.media3.transformer.Composition
import java.io.File

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

    @OptIn(UnstableApi::class)
    fun combineVideoWithImage(
        videoPath: String,
        imagePath: String,
        outputFilePath: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val overlayBitmap = BitmapFactory.decodeFile(imagePath)
        if (overlayBitmap == null) {
            onError("Failed to decode image from path: $imagePath")
            return
        }

        val overlaySettings = StaticOverlaySettings.Builder()
            .setOverlayFrameAnchor(0.5f, 1f) // Center horizontally, bottom vertically
            .build()

        val bitmapOverlay = BitmapOverlay.createStaticBitmapOverlay(overlayBitmap, overlaySettings)

        val overlayEffect = OverlayEffect(listOf(bitmapOverlay))

        val effects = Effects(
            listOf(),
            listOf(overlayEffect)
        )

        val mediaItem = MediaItem.fromUri(videoPath)

        val editedMediaItem = EditedMediaItem.Builder(mediaItem)
            .setEffects(effects)
            .build()
        val ctx = this.applicationContext
        val transformer = Transformer.Builder(ctx)
            .addListener(object : Transformer.Listener {
                override fun onCompleted(
                    composition: androidx.media3.transformer.Composition,
                    exportResult: ExportResult
                ) {
                    onSuccess(outputFilePath)
                }

                override fun onError(
                    composition: Composition,
                    exportResult: ExportResult,
                    exportException: ExportException
                ) {
                    onError(exportException.message ?: "Unknown error")
                }
            })
            .build()

        transformer.start(editedMediaItem, outputFilePath)
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

    fun processVideo() {
        // Define the file paths
        val videoPath = "/storage/emulated/0/test.mp4"
        val imagePath = "/storage/emulated/0/DSC_1695.JPG"
        val outputPath = File("/storage/emulated/0/").resolve("output_video.mp4").absolutePath

        // Create an instance of MainActivity to access its methods
        val activity = MainActivity()

        activity.combineVideoWithImage(videoPath, imagePath, outputPath,
            onSuccess = { path ->

            },
            onError = { errorMessage ->
                println("Error: $errorMessage")
            })
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        VideoOverlayScreen()
    }
}


