package pl.pollub.myapplication.control

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.pollub.myapplication.R
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import pl.pollub.myapplication.ui.theme.ControlBackground
import pl.pollub.myapplication.ui.theme.GreenNeon
import kotlin.math.roundToInt

@Composable
fun SteeringView() {
    var speed by remember { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C20), RoundedCornerShape(16.dp))
            .padding(vertical = 15.dp)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            //LEWY JOYSTICK
            Joystick(
                modifier = Modifier
                    .size(160.dp),
                onMove = { directionFactor -> },
                axis = Axis.Horizontal
            )

            //SRODKOWA KOLUMNA
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.control_speed),
                        contentDescription = "Speed icon",
                        modifier = Modifier
                            .size(150.dp, 100.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Sterowanie i aktualna prędkość",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "%.1f km/h".format(speed),
                        fontSize = 34.sp,
                        color = GreenNeon
                    )
                }
            }

            //PRAWY JOYSTICK
            Joystick(
                modifier = Modifier.size(160.dp),
                onMove = { directionFactor ->
                    speed = (directionFactor * 20).coerceIn(-20f, 20f)  //WARTOSC MAX 20, MIN -20
                },
                axis = Axis.Vertical
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

enum class Axis { Horizontal, Vertical }
@Composable
fun Joystick(
    modifier: Modifier = Modifier,
    onMove: (Float) -> Unit,
    axis: Axis
) {
    var offset by remember { mutableStateOf(Offset.Zero) } //PRZESUNIECIE JOYSTICKA WZG POCZATKOWEGO POLOZENIA
    var currentValue by remember { mutableStateOf(0f) } //WARTOSC
    val radius = with(LocalDensity.current) { 60.dp.toPx() } //MAKSYMALNA ODLEGLOSC NA JAKA MOZNA PRZESUNAC

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            offset = Offset.Zero
                            currentValue = 0f
                            onMove(0f)
                        },
                        onDrag = { _, dragAmount ->
                            offset += dragAmount
                            offset = when (axis) {
                                Axis.Vertical -> Offset(0f, offset.y.coerceIn(-radius, radius))
                                Axis.Horizontal -> Offset(offset.x.coerceIn(-radius, radius), 0f)
                            }
                            val normalized = when (axis) {
                                Axis.Vertical -> -offset.y / radius
                                Axis.Horizontal -> offset.x / radius
                            }
                            currentValue = normalized.coerceIn(-1f, 1f)
                            onMove(currentValue)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            JoystickBackground(axis)
            Box(
                modifier = Modifier
                    .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                    .size(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFD3D3D3), CircleShape)
                )
                if(axis == Axis.Horizontal) {
                    Image(
                        painter = painterResource(id = R.drawable.control_arrow),
                        contentDescription = "Joystick arrow",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                else{
                    Image(
                        painter = painterResource(id = R.drawable.control_arrow),
                        contentDescription = "Joystick arrow",
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(90f),
                        contentScale = ContentScale.Fit,

                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Wartość: %.2f".format(currentValue), fontSize = 14.sp, color = Color.White)
    }
}
@Composable
fun JoystickBackground(axis: Axis) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(ControlBackground, CircleShape)
            .border(2.dp, Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
    }
}

//KOMUNIKACJA PO BLUETOOTH
//@Composable
//fun JoystickControllerWithBluetooth(bluetoothService: BluetoothService) {
//    val velocity = remember { mutableStateOf(0f) }
//    val turn = remember { mutableStateOf(0f) }
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        DualJoystickView(
//            onForwardBackwardChange = { velocity.value = it },
//            onLeftRightChange = { turn.value = it }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Aktualna prędkość: %.2f".format(velocity.value), fontSize = 18.sp)
//    }
//    LaunchedEffect(Unit) {
//        while (true) {
//            //WYSYLANIE DANYCH DO KONTROLERA
//            sendCarCommand(velocity.value, turn.value, bluetoothService)
//            delay(100)
//        }
//    }
//}
//fun sendCarCommand(speed: Float, turn: Float, bluetoothService: BluetoothService) {
//    val speedByte = ((speed.coerceIn(-1f, 1f) + 1) * 127.5f).toInt()
//    val turnByte = ((turn.coerceIn(-1f, 1f) + 1) * 127.5f).toInt()
//    val command = byteArrayOf(speedByte.toByte(), turnByte.toByte())
//    bluetoothService.sendCommand(command)
//}
//interface BluetoothService {
//    fun sendCommand(command: ByteArray)
//}


@Preview(showBackground = true, widthDp = 800, heightDp = 300)
@Composable
fun SteeringViewPreview() {
    MaterialTheme {
        SteeringView()
    }
}