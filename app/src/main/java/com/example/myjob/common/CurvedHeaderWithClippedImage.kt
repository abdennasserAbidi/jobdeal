package com.example.myjob.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CurvedHeaderWithClippedImage(
    @DrawableRes imageRes: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Convert the drawable resource to ImageBitmap
    val imageBitmap: ImageBitmap = ImageBitmap.imageResource(id = imageRes)

    val path = remember {
        Path().apply {
            // Simple custom shape (your original vector path data converted)
            moveTo(414.27f, 0f)
            lineTo(414f, 93.57f)
            cubicTo(414f, 116.52f, 371.05f, 136.75f, 371.05f, 136.75f)
            lineTo(42.92f, 136.75f)
            cubicTo(19.97f, 136.75f, 0f, 179.93f, 0f, 179.93f)
            lineTo(0.27f, 0f)
            close()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val scaleX = canvasWidth / 414.27f
            val scaleY = canvasHeight / 179.93f

            val androidPath = android.graphics.Path().apply {
                moveTo(414.27f, 0f)
                lineTo(414f, 93.57f)
                cubicTo(414f, 116.52f, 371.05f, 136.75f, 371.05f, 136.75f)
                lineTo(42.92f, 136.75f)
                lineTo(0f, 136.75f) // 👈 Replace curve with straight line here
                lineTo(0.27f, 0f)
                close()
                transform(android.graphics.Matrix().apply {
                    setScale(scaleX, scaleY)
                })
            }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.apply {
                    save()
                    clipPath(androidPath)
                    drawBitmap(
                        imageBitmap.asAndroidBitmap(),
                        null,
                        android.graphics.RectF(0f, 0f, canvasWidth, canvasHeight),
                        null
                    )
                    restore()
                }
            }
        }

        // Overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xAA6A1B9A))
        )

        // Title text
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
        )
    }
}