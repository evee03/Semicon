package pl.pollub.myapplication.control

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import pl.pollub.myapplication.R
import pl.pollub.myapplication.ui.theme.ControlBackground
import pl.pollub.myapplication.ui.theme.GreenNeon


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
            .padding(vertical = 15.dp)
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .fillMaxHeight()
                .padding(vertical = 30.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Image(
                painter = painterResource(id = R.drawable.control_lights),
                contentDescription = "Lights icon",
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Tryb i kolor świateł", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, GreenNeon), shape = RoundedCornerShape(9))
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
                                containerColor = if (isActive) GreenNeon else ControlBackground
                            ),
                            shape = RoundedCornerShape(7),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                tab,
                                color = if (isActive) ControlBackground else Color.White,
                                fontSize = 12.sp
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
                .padding(horizontal = 30.dp)
        ){
            Text("Ustaw kolor świateł", color = Color.White)
            ColorPicker(
                selectedColor = selectedColor,
                onColorChange = { newColor -> selectedColor = newColor },
                selectedLights = selectedLights,
                onLightsChange = { newMode -> selectedLights = newMode }
                )

        }

    }
}

@Composable
fun ColorPicker(
    selectedColor: Color,
    onColorChange: (Color) -> Unit,
    selectedLights: String,
    onLightsChange: (String) -> Unit
) {
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
                        onColorChange(Color(colorEnvelope.color.toArgb()))
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
                        "Wybrano: ",
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
                            //TUTAJ MOZNA WYSLAC TRYB I KOLOR SWIATEL


                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenNeon,
                            contentColor = ControlBackground
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
                            onColorChange(Color.White)
                            controller.selectCenter(fromUser = false)
                            onLightsChange("Stałe")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ControlBackground,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, Color.Gray)
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