package com.dieyteixeira.registrohoras.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dieyteixeira.registrohoras.ui.theme.Azul1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerCustom(
    timeState: TimePickerState,
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    onOKClick: (Long) -> Unit = {},
//    onMiliClick: (Long) -> Unit = {}
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        ElevatedCard(
            modifier = Modifier
                .background(
                    color = Color.White,
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
                    text = "Selecione o horário",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp
                    )
                )

                TimePicker(
                    state = timeState,
                    layoutType = TimePickerLayoutType.Vertical,
                )

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
                            val hour = timeState.hour
                            val minute = timeState.minute
                            val milliseconds = convertToMilliseconds(hour, minute)
//                            onMiliClick(milliseconds)
                            onOKClick(milliseconds)
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

fun convertToMilliseconds(hour: Int, minute: Int): Long {
    return (hour * 3600 + minute * 60) * 1000L
}

@SuppressLint("DefaultLocale")
fun formattedTime(hour: Int, minute: Int): String {
    return String.format("%02dh %02dmin", hour, minute)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TimePickerDialogPreview() {
    TimePickerCustom(
        timeState = rememberTimePickerState(
            initialHour = 5,
            initialMinute = 35
        ),
        onDismissRequest = {},
        onCancelClick = {},
        onOKClick = {}
    )
}