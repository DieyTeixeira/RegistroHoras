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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerCustom(
    initialDate: LocalDate,
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onOKClick: (LocalDate) -> Unit,
) {

    val selDate = remember { mutableStateOf(initialDate) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        ElevatedCard(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.extraLarge
                ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "Selecione a data",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))

                CalendarView(
                    viewDate = selDate.value,
                    onDateSelected = { date ->
                        selDate.value = date
                    }
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .height(35.dp),
                        onClick = onCancelClick
                    ) {
                        Text(
                            text = "Cancel",
                            color = Azul1,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Button(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(35.dp),
                        onClick = {
                            onOKClick(selDate.value)
                        }
                    ) {
                        Text(
                            text = "OK",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CalendarView(
    viewDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val selectedDate = remember { mutableStateOf(viewDate) }
    val currentMonth = remember { mutableStateOf(viewDate) }
    val day = selectedDate.value.dayOfMonth
    val month = selectedDate.value.monthValue
    val year = selectedDate.value.year

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .width(80.dp)
                    .background(
                        color = Azul2,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (day < 10) "0$day" else "$day",
                    style = TextStyle(
                        fontSize = 45.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = ".",
                style = TextStyle(
                    fontSize = 55.sp
                ),
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 25.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .width(80.dp)
                    .background(
                        color = Azul2,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (month < 10) "0$month" else "$month",
                    style = TextStyle(
                        fontSize = 45.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = ".",
                style = TextStyle(
                    fontSize = 55.sp
                ),
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 25.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .width(80.dp)
                    .background(
                        color = Azul2,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${year % 100}",
                    style = TextStyle(
                        fontSize = 45.sp
                    ),
                    color = Color.White
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Cabeçalho com setas para trocar o mês
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                TextButton(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                    currentMonth.value = currentMonth.value.minusMonths(1)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_double_arrow_left),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Azul2),
                        modifier = Modifier
                            .size(22.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .width(150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${getMonthAbbreviation2(currentMonth.value.monthValue)} • ${currentMonth.value.year}",
                        style = TextStyle(fontSize = 20.sp)
                    )
                }

                TextButton(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                    currentMonth.value = currentMonth.value.plusMonths(1)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_double_arrow_right),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Azul2),
                        modifier = Modifier
                            .size(22.dp)
                    )
                }
            }

            // Exibe os dias do mês atual
            DayOfWeekHeader()

            val daysInMonth = currentMonth.value.lengthOfMonth()
            val firstDayOfMonth = currentMonth.value.withDayOfMonth(1).dayOfWeek.value % 7
            val totalDays = daysInMonth + firstDayOfMonth

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(240.dp)
            ) {
                // Adiciona espaços em branco para alinhar os dias corretamente
                for (x in 0 until firstDayOfMonth) {
                    item { Box(Modifier.size(40.dp)) }
                }

                items((1..daysInMonth).toList()) { day ->
                    val date = currentMonth.value.withDayOfMonth(day)
                    DateSelectionBox(
                        date = date,
                        selected = selectedDate.value == date
                    ) {
                        selectedDate.value = date
                        onDateSelected(date)
                    }
                }

                // Adiciona espaços em branco após os dias do mês
                for (x in (totalDays % 7)..6) {
                    item { Box(Modifier.size(40.dp)) }
                }
            }
        }
    }
}

@Composable
fun getMonthAbbreviation2(month: Int): String {
    return when (month) {
        1 -> "janeiro"
        2 -> "fevereiro"
        3 -> "março"
        4 -> "abril"
        5 -> "maio"
        6 -> "junho"
        7 -> "julho"
        8 -> "agosto"
        9 -> "setembro"
        10 -> "outubro"
        11 -> "novembro"
        12 -> "dezembro"
        else -> ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateSelectionBox(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .size(40.dp)
            .background(
                color = if (selected) Azul1 else Color.Transparent,
                shape = CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = if (selected) Color.White else Color.Black,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            style = TextStyle(
                fontSize = if (selected) 20.sp else 16.sp
            )
        )
    }
}

@Composable
private fun DayOfWeekHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(100)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf("D", "S", "T", "Q", "Q", "S", "S")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    }
}