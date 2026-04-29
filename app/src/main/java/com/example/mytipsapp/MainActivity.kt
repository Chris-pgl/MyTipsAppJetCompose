package com.example.mytipsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytipsapp.components.InputField
import com.example.mytipsapp.ui.theme.MyTipsAppTheme
import com.example.mytipsapp.util.calculateTotalPerson
import com.example.mytipsapp.util.calculateTotalTip
import com.example.mytipsapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTipsAppTheme {
                MyApp(){
                    Scaffold(
                        containerColor = Color(0xFF6200EE) //backGroundColor
                    ) {
                        innerPadding -> //con questo sistemo la barra sopra l'app

                        //TopHeader()
                        MainContent()
                    }
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
fun TopHeader(totalPerPerson: Double = 134.0){
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
            val total = "%.2f".format(totalPerPerson)
            Text("Total Per Person:",
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
    val tipPercentage = (sliderPositionState.value * 100).toInt()
    val personSplit = remember { mutableStateOf(1)}
    val range = IntRange( start = 1, endInclusive = 100)
    val tipAmmountState = remember { mutableStateOf(0.0) }
    val totalBillPerPersone = remember { mutableStateOf(0.0) }

    Column{
    TopHeader(totalBillPerPersone.value)

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
            if (validState){
            //Split
            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Split", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    ),
                    color = Color.White
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
                        backgroundColor = Color.White,
                        tint = Color.Black,
                        onClick = {
                            personSplit.value
                            if (personSplit.value > range.first)
                                personSplit.value -= 1
                            else 1

                            totalBillPerPersone.value =
                            calculateTotalPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = personSplit.value,
                                tiPercentage = tipPercentage)

                        })


                    Text(
                        "${personSplit.value}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    RoundIconButton(
                        modifier = Modifier,
                        imageVector = Icons.Default.Add,
                        backgroundColor = Color.White,
                        tint = Color.Black,
                        onClick = {
                            personSplit.value
                            if (personSplit.value < range.last) {
                                personSplit.value += 1

                                totalBillPerPersone.value =
                                calculateTotalPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = personSplit.value,
                                    tiPercentage = tipPercentage)
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
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(200.dp))
                Text(
                    "€${tipAmmountState.value}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
            //percentual
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$tipPercentage%")
                Spacer(modifier = Modifier.height(14.dp))

                //slider
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = { newValue ->
                        sliderPositionState.value = newValue

                        tipAmmountState.value =
                            calculateTotalTip(
                                totalBill = totalBillState.value.toDouble(),
                                tiPercentage = tipPercentage)

                        totalBillPerPersone.value =
                            calculateTotalPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = personSplit.value,
                                tiPercentage = tipPercentage)
                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    //steps = 5,  crea dei spazi pre impostati, ma rende scattoso e poco pratico
                    onValueChangeFinished = {

                    }
                )
            }
            }else{
                Box(){}
            }
        }
        }
    }
}


