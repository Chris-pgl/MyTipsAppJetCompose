package com.example.mytipsapp

import android.R
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytipsapp.components.InputField
import com.example.mytipsapp.ui.theme.MyTipsAppTheme
import com.example.mytipsapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTipsAppTheme {
                MyApp(){
                    //TopHeader()
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    content()
}


//@Preview
@Composable
fun TopHeader(data: Double = 134.0){
    Box(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp)
            .background(Color.LightGray, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(data)
            Text("Total Tip for Person:",
                style = MaterialTheme.typography.bodyMedium)
            Text("€$total",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold)
        }
    }

}

@Preview
@Composable
fun MainContent(){
    BillForm(){ billAmt ->
        Log.d("AMT", "MainContent: ${billAmt.toInt() * 100}")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillForm(modifier: Modifier = Modifier,
             onValueChange: (String)-> Unit = {}){

    val totalBillState = remember { mutableStateOf("") }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sliderPositionState = remember { mutableStateOf(0f) }
    val personoSplit = remember { mutableStateOf(1)}
    val range = IntRange( start = 1, endInclusive = 100)

    Column{
    TopHeader()

    Card(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),

        ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(

                valueState = totalBillState,
                lableId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())

                    keyboardController?.hide()
                })
            // if (validState){
            //Split
            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Split", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    ),
                    color = Color.Blue
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    //button add and remove
                    RoundIconButton(
                        modifier = Modifier,
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            if (personoSplit.value > range.first)
                                personoSplit.value -= 1
                            else 1

                        })

                    //TODO add a value remember
                    Text(
                        "${personoSplit.value}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    RoundIconButton(
                        modifier = Modifier,
                        imageVector = Icons.Default.Add,
                        onClick = {
                            if (personoSplit.value < range.last) {
                                personoSplit.value += 1
                            }
                        })

                }
            }
            //Tip
            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Tip", modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 12.dp),
                    color = Color.Blue
                )
                Spacer(modifier = Modifier.width(200.dp))
                Text(
                    "€33.00",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
            //percentual
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("33%")
                Spacer(modifier = Modifier.height(14.dp))

                //slider
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = { newValue ->
                        sliderPositionState.value = newValue
                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    steps = 5,
                    onValueChangeFinished = {

                    }
                )
            }
            //}else{
            //    Box(){}
            //}
        }
        }
    }
}
