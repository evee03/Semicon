package pl.pollub.myapplication

import android.content.res.Configuration
import android.os.Bundle
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pl.pollub.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.xr.scenecore.GltfModel
import com.google.android.filament.utils.Utils

class CarDetailsPage : ComponentActivity() {
    private lateinit var surfaceView: SurfaceView
    init {
        Utils.init()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val renderer = ModelRenderer()
        enableEdgeToEdge()
        surfaceView = SurfaceView(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                200
            )
        }
        renderer.onSurfaceAvailable(surfaceView,lifecycle)
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CarDetailsContent(modifier = Modifier.padding(innerPadding),
                        renderer = renderer,
                        lifecycle = lifecycle
                    )
                }
            }
        }
    }
}

@Composable
fun CarDetailsContent(
    modifier: Modifier = Modifier,
    renderer: ModelRenderer,
    lifecycle: Lifecycle
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        CarDetailsLandscape(modifier, renderer, lifecycle)
    } else {
        CarDetailsPortrait(modifier, renderer, lifecycle)
    }
}


@Composable
fun CarDetailsPortrait(
    modifier: Modifier = Modifier,
    renderer: ModelRenderer,
    lifecycle: Lifecycle
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF080C09))
    ) {
        Android3DView(renderer, lifecycle, Modifier.weight(0.5f))
        DetailsPanel(Modifier.weight(0.6f))
    }
}
@Composable
fun CarDetailsLandscape(
    modifier: Modifier = Modifier,
    renderer: ModelRenderer,
    lifecycle: Lifecycle
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF080C09))
    ) {
        Android3DView(renderer, lifecycle, Modifier.weight(1f))
        DetailsPanel(Modifier.weight(1f))
    }
}
@Composable
fun Android3DView(renderer: ModelRenderer, lifecycle: Lifecycle, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            SurfaceView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                renderer.onSurfaceAvailable(this, lifecycle)
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF080C09))
    )
}
@Composable
fun DetailsPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BatteryBox(modifier= Modifier.weight(0.4f).fillMaxSize())

            DistanceBox(modifier = modifier.weight(0.6f).fillMaxSize())
        }
        SlidersBox()
    }
}

@Composable
fun BatteryBox(modifier: Modifier=Modifier) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF8FBC8F), Color(0xFF4A654A)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 200f)
                )
            )
            .drawBehind {
                val stroke = 2.dp.toPx()
                val gradient = Brush.linearGradient(
                    listOf(Color(0xFFF3E2E2), Color(0xFF252020)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height)
                )
                drawRoundRect(brush = gradient, size = size, style = Stroke(stroke), cornerRadius = CornerRadius(20f, 20f))
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.battery),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text("75%", color = Color.White, modifier = Modifier.align(Alignment.CenterEnd).padding(10.dp), fontSize = 35.sp, fontWeight = FontWeight.Bold)
        Text("Stan baterii", color = Color.White, modifier = Modifier.align(Alignment.BottomCenter).padding(2.dp), fontSize = 12.sp)
    }
}
@Composable
fun DistanceBox(modifier: Modifier=Modifier) {
    Box(
        modifier = Modifier

            .height(100.dp)
            .background(

                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF8FBC8F), Color(0xFF4A654A)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 200f)
                )
                )
            .drawBehind {
                val stroke = 2.dp.toPx()
                val gradient = Brush.linearGradient(
                    listOf(Color(0xFFF3E2E2), Color(0xFF252020)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height)
                )
                drawRoundRect(brush = gradient, size = size, style = Stroke(stroke), cornerRadius = CornerRadius(20f, 20f))
            }
    ) {
        Text(
            text = "315 km",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(10.dp),
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Zasięg",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(2.dp),
            fontSize = 12.sp
        )
    }
}
@Composable
fun SlidersBox(modifier: Modifier=Modifier) {
    Box(
        modifier = Modifier
            .width(320.dp)
            .height(270.dp)
            .padding(10.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8FBC8F),
                        Color(0xFF4A654A)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, 200f)
                )

            )
            .drawBehind {
                val strokeWith = 2.dp.toPx()
                val gradient = Brush.linearGradient(
                    colors = listOf(Color(0xFFF3E2E2), Color(0xFF252020)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height)
                )
                drawRoundRect(
                    brush = gradient,
                    size = size,
                    style = Stroke(width = strokeWith),
                    cornerRadius = CornerRadius(
                        x = 20f,
                        y = 20f
                    )
                )
            }
    )

    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var sliderPosition by remember { mutableFloatStateOf(0f) }
            var sliderPosition1 by remember { mutableFloatStateOf(0f) }
            var sliderPosition2 by remember { mutableFloatStateOf(0f) }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        steps = 99,
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color(0xFF4A654A),
                            inactiveTickColor = Color.Gray
                        )
                    )
                    Text(
                        text = "${sliderPosition.toInt()}",
                        color = Color.White,
                        modifier = Modifier.padding(top = 0.dp)
                    )
                }

                Text(
                    text = "Servo",
                    color = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }



            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = sliderPosition1,
                        onValueChange = { sliderPosition1 = it },
                        steps = 99,
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color(0xFF4A654A),
                            inactiveTickColor = Color.Gray
                        )
                    )
                    Text(
                        text = "${sliderPosition1.toInt()}",
                        color = Color.White,
                        modifier = Modifier.padding(top = 0.dp)
                    )
                }

                Text(
                    text = "Kąt skrętu",
                    color = Color.White,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = sliderPosition2,
                        onValueChange = { sliderPosition2 = it },
                        steps = 99,
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color(0xFF4A654A),
                            inactiveTickColor = Color.Gray
                        )
                    )
                    Text(
                        text = "${sliderPosition2.toInt()}",
                        color = Color.White,
                        modifier = Modifier.padding(top = 0.dp)
                    )
                }

                Text(
                    text = "Chuj wie co jeszce",
                    color = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

    }
}


//@Preview
//@Composable
//fun CarDetailsPagePreview() {
//    MyApplicationTheme {
//        CarDetailsContent(
//            renderer = TODO(),
//            lifecycle = TODO()
//        )
//    }
//}
