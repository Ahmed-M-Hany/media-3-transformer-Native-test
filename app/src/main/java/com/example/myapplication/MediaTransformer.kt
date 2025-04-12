import android.content.Context
import android.graphics.BitmapFactory
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.BitmapOverlay
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.StaticOverlaySettings
import androidx.media3.effect.TextureOverlay
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.Effects
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import com.google.common.collect.ImmutableList

@UnstableApi
class VideoOverlayHelper(private val context: Context) {

    fun combineVideoWithImage(
        videoPath: String,
        imagePath: String,
        outputFilePath: String,
        successCallback: (String) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        // Decode the image file into a Bitmap
        val overlayBitmap = BitmapFactory.decodeFile(imagePath)
        if (overlayBitmap == null) {
            errorCallback("Failed to decode image from path: $imagePath")
            return
        }

        // Create StaticOverlaySettings to position the overlay at the bottom center
        val overlaySettings = StaticOverlaySettings.Builder()
            .setOverlayFrameAnchor(0.5f, 1f) // Center horizontally, bottom vertically
            .build()

        // Create the BitmapOverlay with the specified settings
        val bitmapOverlay : TextureOverlay = BitmapOverlay.createStaticBitmapOverlay(overlayBitmap, overlaySettings)

        // Create an OverlayEffect with the BitmapOverlay
        val overlayEffect = OverlayEffect(ImmutableList.of(bitmapOverlay))

        // Create an Effects object containing the OverlayEffect
        val effects = Effects(
            /* audioProcessors= */ ImmutableList.of(),
            /* videoEffects= */ ImmutableList.of(overlayEffect) as List<Effect>
        )

        // Create the MediaItem from the video file path
        val mediaItem = MediaItem.fromUri(videoPath)

        // Create the EditedMediaItem with the effects
        val editedMediaItem = EditedMediaItem.Builder(mediaItem)
            .setEffects(effects)
            .build()

        // Initialize the Transformer
        val transformer = Transformer.Builder(context)
            .addListener(object : Transformer.Listener {
                override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                    successCallback(outputFilePath)
                }

                override fun onError(
                    composition: Composition,
                    exportResult: ExportResult,
                    exportException: ExportException
                ) {
                    super.onError(composition, exportResult, exportException)
                    errorCallback("Error during transformation: ${exportException.message}")
                }
            })
            .build()

        // Start the transformation
        transformer.start(editedMediaItem, outputFilePath)
    }
}
