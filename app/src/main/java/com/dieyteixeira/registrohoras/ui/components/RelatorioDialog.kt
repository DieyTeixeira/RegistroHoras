package com.dieyteixeira.registrohoras.ui.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.model.Registro
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.Azul3
import com.dieyteixeira.registrohoras.ui.theme.Azul4
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RelatorioDialog(
    onDismissRequest: () -> Unit,
    onOKClick: () -> Unit,
    listaRegistros: List<Registro>,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true)
    ) {
        ElevatedCard(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large
                )
                .height(600.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "Relatório Completo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(5.dp)
                        .background(color = Azul1)

                )
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(listaRegistros) { registro ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Column {
                            // Formatação da data
                            val parsedDate =
                                LocalDate.parse(registro.data) // assume que registro.data é "2024-10-30"
                            val formattedDate =
                                parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            val formattedDayOfWeek =
                                parsedDate.format(DateTimeFormatter.ofPattern("EEEE"))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Azul4,
                                        shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)
                                    )
                                    .padding(15.dp, 5.dp)
                            ){
                                Column {
                                    Text(
                                        text = "Data",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = 17.sp
                                        )
                                    )
                                    Text(
                                        text = "$formattedDate  -  $formattedDayOfWeek",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 17.sp
                                        )
                                    )
                                }
                            }

                            // Função para formatar e exibir horas
                            @Composable
                            fun formatAndDisplayTime(
                                initialMillis: Long,
                                finalMillis: Long,
                            ) {
                                if (initialMillis > 0) {
                                    val formattedStartTime = convertMillisToTime(initialMillis)
                                    val formattedEndTime = convertMillisToTime(finalMillis)

                                    Text(
                                        text = "$formattedStartTime - $formattedEndTime",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontSize = 17.sp
                                        )
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.LightGray,
                                        shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
                                    )
                                    .padding(15.dp, 5.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Horário",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontSize = 17.sp
                                        ),
                                        modifier = Modifier.width(100.dp)
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        formatAndDisplayTime(
                                            registro.initialMillisP1,
                                            registro.finalMillisP1
                                        )
                                        if (registro.initialMillisP2 > 0) {
                                            Text(
                                                text = "  |  ",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontSize = 17.sp
                                                )
                                            )
                                            formatAndDisplayTime(
                                                registro.initialMillisP2,
                                                registro.finalMillisP2
                                            )
                                        }
                                    }
                                }
                                if (registro.initialMillisP3 > 0) {
                                    Column {
                                        Text(
                                            text = "Extra",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 17.sp
                                            ),
                                            modifier = Modifier.width(100.dp)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            formatAndDisplayTime(
                                                registro.initialMillisP3,
                                                registro.finalMillisP3
                                            )
                                            if (registro.initialMillisP4 > 0) {
                                                Text(
                                                    text = "  |  ",
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontSize = 17.sp
                                                    )
                                                )
                                                formatAndDisplayTime(
                                                    registro.initialMillisP4,
                                                    registro.finalMillisP4
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Azul1,
                        shape = RoundedCornerShape(100)
                    )
                    .padding(5.dp)
                    .clickable { onOKClick() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_xis),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun convertMillisToTime(totalMillis: Long): String {
    val hours = (totalMillis / 1000) / 3600
    val minutes = ((totalMillis / 1000) % 3600) / 60
    val formattedTime = String.format("%02d:%02d", hours, minutes)

    return formattedTime
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun RelatorioDialogPreview() {
    val registrosSimulados = listOf(
        Registro(
            data = "2024-10-30",
            initialMillisP1 = 8 * 60 * 60 * 1000L, // 8:00 AM em milissegundos
            finalMillisP1 = 12 * 60 * 60 * 1000L,  // 12:00 PM
            initialMillisP2 = 13 * 60 * 60 * 1000L, // 1:00 PM
            finalMillisP2 = 17 * 60 * 60 * 1000L,  // 5:00 PM
            initialMillisP3 = 0L,
            finalMillisP3 = 0L,
            initialMillisP4 = 0L,
            finalMillisP4 = 0L
        ),
        Registro(
            data = "2024-10-29",
            initialMillisP1 = 9 * 60 * 60 * 1000L, // 9:00 AM
            finalMillisP1 = 12 * 60 * 60 * 1000L,  // 12:00 PM
            initialMillisP2 = 13 * 60 * 60 * 1000L, // 1:00 PM
            finalMillisP2 = 18 * 60 * 60 * 1000L,  // 6:00 PM
            initialMillisP3 = 18 * 60 * 60 * 1000L,
            finalMillisP3 = 20 * 60 * 60 * 1000L,
            initialMillisP4 = 0L,
            finalMillisP4 = 0L
        )
    )

    RelatorioDialog(
        onDismissRequest = {},
        onOKClick = {},
        listaRegistros = registrosSimulados
    )

}