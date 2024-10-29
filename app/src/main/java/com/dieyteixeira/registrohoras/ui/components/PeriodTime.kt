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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.AzulDegrade

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodTime(
    text: String,
    initialTime: String,
    finalTime: String,
    onInitialTimeClick: () -> Unit,
    onFinalTimeClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_double_arrow_left),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
                Box(
                    modifier = Modifier
                        .width(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        color = Azul1,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_double_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                )
            }
            Box(
                modifier = Modifier
                    .padding(end = 230.dp)
                    .height(35.dp)
                    .width(125.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(100)
                    )
                    .clickable(
                        onClick = onInitialTimeClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (initialTime.isEmpty()) "--" else initialTime,
                    color = Azul2,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 230.dp)
                    .height(35.dp)
                    .width(125.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(100)
                    )
                    .clickable(
                        onClick = onFinalTimeClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (finalTime.isEmpty()) "--" else finalTime,
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
        PeriodTime(
            text = "MANHÃ",
            initialTime = "08:00",
            finalTime = "12:30",
            onInitialTimeClick = {},
            onFinalTimeClick = {}
        )
    }
}