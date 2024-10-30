package com.dieyteixeira.registrohoras.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.AzulDegrade
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodDate(
    initialDate: LocalDate,
    finalDate: LocalDate,
    onInitialDateClick: () -> Unit,
    onFinalDateClick: () -> Unit
) {

    val initialDateDay = if (initialDate.dayOfMonth < 10) {
        "0${initialDate.dayOfMonth}"
    } else {
        "${initialDate.dayOfMonth}"
    }
    val initialDateMonth = if (initialDate.monthValue < 10) {
        "0${initialDate.monthValue}"
    } else {
        "${initialDate.monthValue}"
    }
    val initialDateYear = initialDate.year
    
    val finalDateDay = if (finalDate.dayOfMonth < 10) {
        "0${finalDate.dayOfMonth}"
    } else {
        "${finalDate.dayOfMonth}"
    }
    val finalDateMonth = if (finalDate.monthValue < 10) {
        "0${finalDate.monthValue}"
    } else {
        "${finalDate.monthValue}"
    }
    val finalDateYear = finalDate.year

    Column(
        modifier = Modifier
            .height(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .height(35.dp)
                    .width(120.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(100)
                    )
                    .clickable(
                        onClick = onInitialDateClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${initialDateDay}/${initialDateMonth}/${initialDateYear}",
                    color = Azul2,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier
                    .height(35.dp)
                    .width(120.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(100)
                    )
                    .clickable(
                        onClick = onFinalDateClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${finalDateDay}/${finalDateMonth}/${finalDateYear}",
                    color = Azul2,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun PeriodTimePreview() {
    Column(
        modifier = Modifier
            .height(60.dp)
            .background(AzulDegrade),
        verticalArrangement = Arrangement.Center
    ) {
        PeriodDate(
            initialDate = LocalDate.of(1900,1,1),
            finalDate = LocalDate.of(1900,1,1),
            onInitialDateClick = {},
            onFinalDateClick = {}
        )
    }
}