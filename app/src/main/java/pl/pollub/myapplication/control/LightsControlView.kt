package pl.pollub.myapplication.control

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pl.pollub.myapplication.R
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun LightsControlView() {
    var selectedColor by remember {
        mutableStateOf(Color.White)
    }
    var selectedLights by remember {
        mutableStateOf("Stałe")
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C20), RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .padding(vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.control_lights),
                contentDescription = "Lights icon",
                modifier = Modifier
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Tryb i kolor świateł", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Color(0xFF75FBCF)), shape = RoundedCornerShape(9))
            ){
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    listOf("Stałe", "Migające").forEach { tab ->
                        val isActive = selectedLights == tab
                        Button(
                            onClick = { selectedLights = tab },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isActive) Color(0xFF75FBCF) else Color(0xFF202229)
                            ),
                            shape = RoundedCornerShape(7),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                tab,
                                color = if (isActive) Color(0xFF202229) else Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
//                .width(500.dp)
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .padding(horizontal = 50.dp)
        ){
            Text("Ustaw kolor świateł", color = Color.White)
            ColorPicker(selectedColor, onColorChange = { newColor ->
                selectedColor = newColor
            })
        }

    }
}

@Composable
fun ColorPicker(selectedColor: Color, onColorChange: (Color) -> Unit) {
    val controller = rememberColorPickerController()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp)
    ){
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
                ){
                HsvColorPicker(
                    modifier = Modifier
                        .weight(1f)
                        .width(160.dp)
                        .padding(vertical = 8.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
//                        onColorChange(Color(colorEnvelope.color.toArgb()))
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    BrightnessSlider(
                        modifier = Modifier
                            .height(20.dp)
                            .width(150.dp),
                        controller = controller,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                //WYSWIETLANIE WYBRANEGO KOLORU
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ){
                    Text(
                        "Wybrano:",
                        style = TextStyle(fontSize = 12.sp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AlphaTile(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(10)),
                        controller = controller
                    )
                }
                Column (
                    horizontalAlignment = Alignment.End
                ){
                    Button(
                        onClick = {
                            //ZMIEN KOLOR SWIATEL
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF75FBCF),
                            contentColor = Color(0xFF202229)
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            "Zastosuj",
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                    Button(
                        onClick = {
//                            onColorChange(Color.White)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF202229),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color(0xFF75FBCF))
                    ) {
                        Text(
                            "Przywróć domyślne",
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
        }
    }


}


@Preview(showBackground = true, widthDp = 800, heightDp = 300)
@Composable
fun LightsControlViewPreview() {
    MaterialTheme {
        LightsControlView()
    }
}