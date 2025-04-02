package pl.pollub.myapplication

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun SemiconStartPage(onGetStartedClick: () -> Unit = {}) {

    val context = LocalContext.current
    val activity = context as? Activity

    //TYLKO ORIENTACJA PIONOWA
    DisposableEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    //ANIMACJE
    var showLogo by remember { mutableStateOf(false) }
    var showCircles by remember { mutableStateOf(false) }
    var showCar by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (showCircles) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alphaAnim"
    )

    val carAlpha by animateFloatAsState(
        targetValue = if (showCar) 1f else 0f,
        animationSpec = tween(durationMillis = 1100),
        label = "carAlpha"
    )

    val logoScale by animateFloatAsState(
        targetValue = if (showLogo) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val circleScale by animateFloatAsState(
        targetValue = if (showCircles) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "circleScale"
    )

    val carScale by animateFloatAsState(
        targetValue = if (showCar) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "carScale"
    )


    LaunchedEffect(key1 = true) {
        showLogo = true
        delay(300)
        showCircles = true
        delay(200)
        showCar = true
        delay(500)
        showText = true
        delay(300)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // LOGO Z ANIMACJA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 5.dp)
                .align(Alignment.TopCenter)
                .zIndex(3f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.semicon_logo),
                contentDescription = "Semicon Logo",
                modifier = Modifier
                    .height(90.dp)
                    .fillMaxWidth()
                    .scale(logoScale),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            //  NAJWIEKSZA ELIPSA
            Box(
                modifier = Modifier
                    .size(680.dp)
                    .padding(bottom = 80.dp)
                    .graphicsLayer {
                        scaleX = 1.3f
                        scaleY = 0.9f
                        shape = CircleShape
                        clip = true
                        alpha = alphaAnim
                    }

                    .blur(100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF108788).copy(alpha = 0.3f),
                                    Color(0xFF108788).copy(alpha = 0.2f),
                                    Color(0xFF108788).copy(alpha = 0.1f)
                                ),
                                center = Offset.Zero,
                                radius = size.minDimension * 0.8f
                            ),
                            radius = size.minDimension / 2,
                            blendMode = BlendMode.Screen
                        )
                    }

                    .background(
                        color = Color(0xE819261D),
                        shape = CircleShape
                    )

                    .blur(150.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            )

            // SREDNIA ELIPSA
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .offset(y = (-60).dp)
                    .graphicsLayer {
                        scaleX = 1.0f
                        scaleY = 1.0f
                        shape = CircleShape
                        clip = true
                        alpha = alphaAnim
                    }
                    .drawWithCache {

                        val strokeWidth = 2.dp.toPx()
                        val strokeColor = Color(0xA885A18F)

                        onDrawWithContent {
                            drawContent()
                            drawCircle(
                                color = strokeColor,
                                radius = size.minDimension / 2 - strokeWidth/2,
                                style = Stroke(width = strokeWidth)
                            )
                        }
                    }
                    .background(
                        color = Color(0xE84F725B),
                        shape = CircleShape
                    )

                    .blur(
                        radiusX = 100.dp,
                        radiusY = 100.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    )
            )


            // NAJMNIEJSZA ELIPSA
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .offset(y = (-60).dp)
                    .graphicsLayer {
                        scaleX = 1.1f
                        scaleY = 1.0f
                        alpha = alphaAnim
                    }
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFFB3E9C6)
                    )
                    .drawBehind {
                        drawCircle(
                            color = Color(0xFFCCDAD1),
                            radius = size.minDimension / 2 + 10.dp.toPx(),
                            style = Stroke(width = 10.dp.toPx())
                        )
                    }
                    .blur(radius = 20.dp)
                    .background(Color(0xFFBBDBC7), shape = CircleShape)

            )


            // SAMOCHÓD
            Image(
                painter = painterResource(id = R.drawable.car_image),
                contentDescription = "Samochód",
                modifier = Modifier
                    .width(420.dp)
                    .height(320.dp)
                    .padding(top = 50.dp)
                    .zIndex(2f)
                    .graphicsLayer {
                        alpha = carAlpha
                        scaleX = carScale
                        scaleY = carScale
                    },

                contentScale = ContentScale.Fit
            )
        }


        // TEKST I PRZYCISK POD SAMOCHODEM Z ANIMACJAMI
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = showText,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Poczuj się jak Kubica",
                        color = Color(0xFF8DBBA4),
                        fontSize = 32.sp,
                        fontFamily = FontFamily(Font(R.font.lato_bold)),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sterowanie telefonem zapewni Ci pełen komfort",
                        color = Color(0xFF8DBBA4),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.lato_thin)),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                Button(
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Zaczynajmy!",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.lato_bold)),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SemiconStartPagePreview() {
    MaterialTheme {
        SemiconStartPage()
    }
}