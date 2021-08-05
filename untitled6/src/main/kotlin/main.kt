import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAwtImage
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                val offsetX = remember { mutableStateOf(0f) }
                val offsetY = remember { mutableStateOf(0f) }
                val images = remember { mutableListOf<ImageBitmap>() }
                Button(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        saveBitmaps(images)
                    }
                ) { Text("save bitmap") }
                Box(
                    Modifier
                        .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                        .size(150.dp)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consumeAllChanges()
                                offsetX.value += dragAmount.x
                                offsetY.value += dragAmount.y
                            }
                        }
                ) {
                    val bitmap = createBitmapFromText("\uD83D\uDE24")
                    images += bitmap
                    Image(bitmap, "x")
                }
            }
        }
    }
}

@Composable
fun createBitmapFromText(text: String): ImageBitmap {
    val x = ImageBitmap(50, 50)
    val canvas = Canvas(x)
    Text(text, onTextLayout = {
        TextPainter.paint(canvas, it)
    })
    return x
}

fun saveBitmaps(images: List<ImageBitmap>) {
    images.forEachIndexed { index, img ->
        val asAwtImage = img.asAwtImage()
        asAwtImage.createGraphics()
        ImageIO.write(asAwtImage, "png", File("./output_image$index.png"))
    }
}