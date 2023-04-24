package com.bazuma.partytip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bazuma.partytip.Widgets.RoundIconButton
import com.bazuma.partytip.components.InputField
import com.bazuma.partytip.ui.theme.PartyTipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                TopHeader()
            }
        }
    }
}
//Container functions
@Composable
fun MyApp(content :@Composable () -> Unit){
    PartyTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

//It know its need a data
//@Preview
@Composable
fun TopHeader(totalPerPerson:Double=100.0){
   Surface(modifier = Modifier
       .fillMaxWidth()
       .padding(18.dp)
       .height(150.dp)
       .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            val total="%.2f".format(totalPerPerson)
            Text(text="Total Per Person", style =MaterialTheme.typography.h5)
            Text(text="Ksh$totalPerPerson",style =MaterialTheme.typography.h4, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
    BillForm(){

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier=Modifier,
            onValChange:(String) -> Unit ={}
             ){
    val totalBillState =remember{
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

    val splitByState= remember {
        mutableStateOf(1)
    }
    val range=IntRange(start=1, endInclusive = 100)
    TopHeader()

    val keyboardControler=LocalSoftwareKeyboardController.current



    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = CircleShape.copy(all= CornerSize(8.dp)),
        border = BorderStroke(width = 2.dp, color = Color.LightGray) )
    {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState =totalBillState ,
                labelId ="Enter Bill" ,
                enabled =true ,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardControler?.hide()
                }
            )
            if (validState){
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start){
                Text( "Split",
                    modifier = Modifier.align(
                    alignment = Alignment.CenterVertically
                ))
                Spacer(modifier =Modifier.width(120.dp))
                Row(modifier = Modifier.padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.End) {
                        RoundIconButton(
                            modifier,
                            imageVector = Icons.Default.Remove ,
                            onClick = {
                                splitByState.value =
                                    if(splitByState.value > 1)
                                        splitByState.value - 1
                                    else 1
                            })
                        Text(
                                  text = "${splitByState.value}",
                                  modifier = Modifier
                                      .align(Alignment.CenterVertically)
                                      .padding(start = 9.dp, end = 9.dp))

                       RoundIconButton(
                        imageVector = Icons.Default.Add ,
                        onClick = {
                            if (splitByState.value < range.last ){
                                splitByState.value += 1
                            }
                        })
                }
                }
                //Tip Row
                Row (modifier = Modifier
                    .padding(horizontal = 3.dp,vertical=12.dp)){
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(text = "Ksh30")

                }
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$tipPercentage % ")
                    Spacer(modifier = Modifier.height(14.dp))

                    //Slider
                    Slider(
                        value =sliderPositionState.value ,
                        onValueChange = { newVal ->
                             sliderPositionState.value=newVal
                        },
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            steps = 5,
                        )

                }
            }else{
                Box(){

                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PartyTipTheme {
        MyApp {
            Text(text = "Hello There")
        }
    }
}