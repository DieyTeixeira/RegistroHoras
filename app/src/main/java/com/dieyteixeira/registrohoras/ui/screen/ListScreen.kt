package com.dieyteixeira.registrohoras.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.model.Registro
import com.dieyteixeira.registrohoras.repository.RegistrosRepository
import com.dieyteixeira.registrohoras.ui.components.DatePickerCustom
import com.dieyteixeira.registrohoras.ui.components.PeriodDate
import com.dieyteixeira.registrohoras.ui.components.RelatorioDialog
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.AzulDegrade
import com.dieyteixeira.registrohoras.ui.theme.Verde
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


@SuppressLint("AutoboxingStateCreation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListScreen() {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val registrosRepository = RegistrosRepository()
    var currentPicker by remember { mutableStateOf<Boolean?>(null) }

    var initialDate by remember { mutableStateOf(LocalDate.now()) }
    var finalDate by remember { mutableStateOf(LocalDate.now()) }

    var totalHorasMillis by remember { mutableLongStateOf(0L) }
    var totalNormalMillis by remember { mutableLongStateOf(0L) }
    var totalExtraMillis by remember { mutableLongStateOf(0L) }

    val (totalHorasTimes, totalMinutesTimes) = convertMillisToTimeGeral(totalHorasMillis)
    val (normalHorasTimes, normalMinutesTimes) = convertMillisToTimeGeral(totalNormalMillis)
    val (extraHorasTimes, extraMinutesTimes) = convertMillisToTimeGeral(totalExtraMillis)

    var listaRegistros by remember { mutableStateOf(listOf<Registro>()) }
    var showRelatorio by remember { mutableStateOf(false) }

    if (showRelatorio) {
        RelatorioDialog(
            onDismissRequest = { showRelatorio = false },
            onOKClick = { showRelatorio = false },
            listaRegistros = listaRegistros
        )
    }

    if (currentPicker != null) {
        val isInitial = currentPicker!!
        val datePickerState = if (isInitial) initialDate else finalDate

        DatePickerCustom(
            initialDate = datePickerState,
            onDismissRequest = { currentPicker = null },
            onCancelClick = { currentPicker = null },
            onOKClick = { date ->
                if (isInitial) {
                    initialDate = date
                    currentPicker = null
                } else {
                    if (date < initialDate) {
                        Toast.makeText(
                            context,
                            "A data final não pode ser menor que a inicial",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        finalDate = date
                        currentPicker = null
                    }
                }
            }
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PeriodDate(
                initialDate = initialDate,
                finalDate = finalDate,
                onInitialDateClick = { currentPicker = true },
                onFinalDateClick = { currentPicker = false }
            )
            Spacer(modifier = Modifier.width(15.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = Azul1,
                        shape = CircleShape
                    )
                    .size(35.dp)
                    .clickable {
                        totalHorasMillis = 0L
                        totalNormalMillis = 0L
                        totalExtraMillis = 0L
                        listaRegistros = listOf()

                        val dataInicio = initialDate.toString()
                        val dataFim = finalDate.toString()

                        scope.launch(Dispatchers.IO) {
                            registrosRepository.recuperarRegistrosEntreDatas(dataInicio, dataFim).collect { registros ->
                                for (registro in registros) {
                                    totalHorasMillis += registro.sumMillisTotal
                                    totalNormalMillis += registro.sumMillisNormal
                                    totalExtraMillis += registro.sumMillisExtra
                                }
                                listaRegistros = registros
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .width(140.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(100)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Total de Horas",
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = Azul1,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = totalHorasTimes,
                    fontSize = 43.sp,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Text(
                    text = totalMinutesTimes,
                    fontSize = 35.sp,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_double_arrow_down),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(140.dp)
                            .background(
                                color = Azul2,
                                shape = RoundedCornerShape(100)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Horas Normais",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = normalHorasTimes,
                            fontSize = 33.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = normalMinutesTimes,
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier
                                .padding(bottom = 1.5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(140.dp)
                            .background(
                                color = Verde,
                                shape = RoundedCornerShape(100)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Horas Extras",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 5.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = extraHorasTimes,
                            fontSize = 33.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = extraMinutesTimes,
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier
                                .padding(bottom = 1.5.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(125.dp))
        if (listaRegistros.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Azul1,
                                shape = CircleShape
                            )
                            .size(45.dp)
                            .clickable {
                                showRelatorio = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_list),
                            contentDescription = "Relatório",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Relatório",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = Azul1
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun convertMillisToTimeGeral(totalMillis: Long): Pair<String, String> {

    val hours = (totalMillis / 1000) / 3600
    val minutes = ((totalMillis / 1000) % 3600) / 60
    val formattedHour = String.format("%02dh", hours)
    val formattedMinute = String.format("%02dmin", minutes)

    return formattedHour to formattedMinute
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ListScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulDegrade),
        verticalArrangement = Arrangement.Center
    ) {
        ListScreen()
    }
}