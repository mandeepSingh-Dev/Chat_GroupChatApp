package com.example.chat__groupchatapp.Utils.Widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun BounceButton(buttonText: String, onButtonClick : () -> Unit){
    var buttonState by remember{ mutableStateOf(ButtonState.IDLE) }
    val scale by animateFloatAsState(targetValue = (if (buttonState == ButtonState.PRESSED) 0.70f else 1f), label = "", animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy), finishedListener = {} )
    var colorAnimate = animateColorAsState(targetValue = if(buttonState == ButtonState.PRESSED) Color.Black else Color.Blue, label = "", animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)).value


    OutlinedButton(onClick = {onButtonClick()}, colors = ButtonDefaults.buttonColors(colorAnimate), modifier = Modifier
        .fillMaxWidth()

        /* .graphicsLayer {
            scaleX = scale
            scaleY = scale
        } */
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {}
        )
        .pointerInput(buttonState) {

            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.PRESSED) {
                    waitForUpOrCancellation()
                    // colorAnimate = Color.Black
                    ButtonState.IDLE

                } else {
                    awaitFirstDown(false)
                    // colorAnimate = Color.Magenta
                    ButtonState.PRESSED
                }
            }
        }.scale(scale)
        , content = {
            Text(text = buttonText)
        })


}

enum class ButtonState{
    IDLE,
    PRESSED
}

