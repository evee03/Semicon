package pl.pollub.myapplication.control

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pl.pollub.myapplication.R

@Composable
fun ControlScreen() {
    LockOrientationLandscape()
    var selectedTab by remember {
        mutableStateOf("Światła")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080C09))
            .padding(16.dp)
    ) {
        //NAWIGACJA W ZAKŁADCE STEROWANIE
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Światła", "Drzwi", "Prędkość", "Kierowanie").forEach { tab ->
                val isActive = selectedTab == tab
                Button(
                    onClick = { selectedTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isActive) Color(0xFF75FBCF) else Color(0xFF202229)
                    ),
                    border = if (!isActive) BorderStroke(2.dp, Color(0xFF75FBCF)) else null,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(50.dp)
                ) {
                    val iconPainter = when (tab) {
                        "Światła" -> painterResource(id = R.drawable.control_nav_lights)
                        "Drzwi" -> painterResource(id = R.drawable.control_nav_doors)
                        "Prędkość" -> painterResource(id = R.drawable.control_nav_speed)
                        "Kierowanie" -> painterResource(id = R.drawable.control_nav_steering)
                        else -> null
                    }
                    iconPainter?.let {
                        Icon(
                            painter = iconPainter,
                            contentDescription = "$tab Ikona",
                            tint = if (isActive) Color(0xFF202229) else Color.White,
                            modifier = Modifier
                                .size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Text(
                        tab,
                        color = if (isActive) Color(0xFF202229) else Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //WYSWIETLENIE ZAWARTOSCI STRONY W ZALEZNOSCI OD ZAKLADKI
        when (selectedTab) {
            "Światła" -> LightsControlView()
//            "Drzwi" ->
//            "Prędkość" ->
//            "Kierowanie" ->
        }
    }
}

//TYLKO ORIENTACJA POZIOMA
@Composable
fun LockOrientationLandscape() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as ComponentActivity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ControlScreenPreview() {
    MaterialTheme {
        ControlScreen()
    }
}