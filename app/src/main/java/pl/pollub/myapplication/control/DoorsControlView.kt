package pl.pollub.myapplication.control

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.pollub.myapplication.R
import pl.pollub.myapplication.ui.theme.ControlBackground
import pl.pollub.myapplication.ui.theme.GreenNeon


@Composable
fun DoorsControlView() {
    var leftIsOpen by remember { mutableStateOf(false) }
    var rightIsOpen by remember { mutableStateOf(false) }

    fun sendDoorsStatus(){
        //TUTAJ MOZNA PRZESLAC STATUS DRZWI, WYWOLYWANA ZA KAZDYM RAZEM PRZY ZMIANIE
        Log.d("DOOR","CHANGE OF STATE")
    }

    fun toggleDoors(isOpen: Boolean){
        leftIsOpen = isOpen
        rightIsOpen = isOpen
        sendDoorsStatus()
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C20), RoundedCornerShape(16.dp))
            .padding(vertical = 15.dp)
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 30.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Image(
                painter = painterResource(id = R.drawable.control_doors),
                contentDescription = "Doors icon",
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Otwieranie / Zamykanie drzwi", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        //OTWORZ DRZWI
                        toggleDoors(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ControlBackground
                    ),
                    border = BorderStroke(1.dp, GreenNeon),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.control_unlock),
                        contentDescription = "Unlock Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Button(
                    onClick = {
                        //ZAMKNIJ DRZWI
                        toggleDoors(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ControlBackground
                    ),
                    border = BorderStroke(1.dp, GreenNeon),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.control_lock),
                        contentDescription = "Lock Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
        }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 10.dp)
                .padding(horizontal = 60.dp)
        ){
            Text("Otwórz lewe drzwi", color = Color.White)
            Switch(
                checked = leftIsOpen,
                onCheckedChange = {
                    leftIsOpen = it
                    sendDoorsStatus()
//                    Log.d("DOORS", "OPENING LEFT")
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenNeon,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = ControlBackground
                )
            )
            Text("Otwórz Prawe drzwi", color = Color.White)
            Switch(
                checked = rightIsOpen,
                onCheckedChange = {
                    rightIsOpen = it
                    sendDoorsStatus()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenNeon,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = ControlBackground
                )
            )
        }

    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 300)
@Composable
fun DoorsControlViewPreview() {
    MaterialTheme {
        DoorsControlView()
    }
}