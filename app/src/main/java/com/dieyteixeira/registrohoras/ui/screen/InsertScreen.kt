package com.dieyteixeira.registrohoras.ui.screen

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.model.Registro
import com.dieyteixeira.registrohoras.repository.RegistrosRepository
import com.dieyteixeira.registrohoras.ui.components.DatePickerCustom
import com.dieyteixeira.registrohoras.ui.components.PeriodTime
import com.dieyteixeira.registrohoras.ui.components.TimePickerCustom
import com.dieyteixeira.registrohoras.ui.components.formattedTime
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.Verde2
import com.dieyteixeira.registrohoras.ui.theme.Vermelho
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InsertScreen() {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val registrosRepository = RegistrosRepository()

    var registrosFiltrados by remember { mutableStateOf(emptyList<Registro>()) }

    var deleteOption by remember { mutableStateOf(false) }

    // variáveis para salvar dados no banco de dados
    var dateSelected by remember { mutableStateOf(LocalDate.now()) }
    var dateRegistro by remember { mutableStateOf("") }
    var showCustomDatePicker by remember { mutableStateOf(false) }

    // Inicializando arrays para armazenar tempos iniciais e finais
    val initialTimes = remember { mutableStateOf(Array(4) { "" }) }
    val finalTimes = remember { mutableStateOf(Array(4) { "" }) }

    // Arrays para armazenar milissegundos
    val initialMillis = remember { mutableStateOf(LongArray(4) { 0L }) }
    val finalMillis = remember { mutableStateOf(LongArray(4) { 0L }) }
    val calMillis = remember { mutableStateOf(LongArray(4) { 0L }) }

    var sumMillisNormal by remember { mutableStateOf(0L) }
    val (totalNormal, totalHoursN, totalMinutesN) = convertMillisToTime(sumMillisNormal)

    var sumMillisExtra by remember { mutableStateOf(0L) }
    val (totalExtra, totalHoursE, totalMinutesE) = convertMillisToTime(sumMillisExtra)

    var sumMillisTotal by remember { mutableStateOf(0L) }
    val (totalTime, totalHoursT, totalMinutesT) = convertMillisToTime(sumMillisTotal)

    var currentPicker by remember { mutableStateOf<Pair<Int, Boolean>?>(null) }

    // Arrays para estados dos time pickers
    val timePickerStateP1Initial = rememberTimePickerState(is24Hour = true)
    val timePickerStateP1Final = rememberTimePickerState(is24Hour = true)
    val timePickerStateP2Initial = rememberTimePickerState(is24Hour = true)
    val timePickerStateP2Final = rememberTimePickerState(is24Hour = true)
    val timePickerStateP3Initial = rememberTimePickerState(is24Hour = true)
    val timePickerStateP3Final = rememberTimePickerState(is24Hour = true)
    val timePickerStateP4Initial = rememberTimePickerState(is24Hour = true)
    val timePickerStateP4Final = rememberTimePickerState(is24Hour = true)

    if (showCustomDatePicker) {
        DatePickerCustom(
            initialDate = dateSelected,
            onDismissRequest = { showCustomDatePicker = false },
            onCancelClick = { showCustomDatePicker = false },
            onOKClick = { date ->
                for (i in initialTimes.value.indices) {
                    initialTimes.value[i] = ""
                    finalTimes.value[i] = ""
                }
                dateRegistro = date.toString()
                dateSelected = date
                scope.launch(Dispatchers.IO) {
                    try {
                        registrosRepository.recuperarRegistro(date.toString())
                            .collect { listaRegistros ->
                                if (listaRegistros.isNotEmpty()) {
                                    deleteOption = true
                                    registrosFiltrados = listaRegistros
                                    val primeiroRegistro = registrosFiltrados[0]
                                    initialTimes.value = arrayOf(
                                        primeiroRegistro.initialTimeP1,
                                        primeiroRegistro.initialTimeP2,
                                        primeiroRegistro.initialTimeP3,
                                        primeiroRegistro.initialTimeP4
                                    )
                                    finalTimes.value = arrayOf(
                                        primeiroRegistro.finalTimeP1,
                                        primeiroRegistro.finalTimeP2,
                                        primeiroRegistro.finalTimeP3,
                                        primeiroRegistro.finalTimeP4
                                    )
                                } else {
                                    deleteOption = false
                                }
                            }
                    } catch (e: Exception) {
                        Log.e("InsertScreen", "Erro ao recuperar registros: ${e.message}", e)
                    }
                }
                Log.d("InsertScreen", "Delete Option: $deleteOption")
                showCustomDatePicker = false
            }
        )
    }

    if (currentPicker != null) {
        val (periodo, isInitial) = currentPicker!!
        val timePickerState = when (periodo) {
            1 -> if (isInitial) timePickerStateP1Initial else timePickerStateP1Final
            2 -> if (isInitial) timePickerStateP2Initial else timePickerStateP2Final
            3 -> if (isInitial) timePickerStateP3Initial else timePickerStateP3Final
            4 -> if (isInitial) timePickerStateP4Initial else timePickerStateP4Final
            else -> timePickerStateP1Initial
        }

        TimePickerCustom(
            timeState = timePickerState,
            onDismissRequest = { currentPicker = null },
            onCancelClick = { currentPicker = null },
            onOKClick = {
                val selectedTime = formattedTime(timePickerState.hour, timePickerState.minute)
                if (isInitial) {
                    initialTimes.value[periodo - 1] = selectedTime
                } else {
                    finalTimes.value[periodo - 1] = selectedTime
                }
                currentPicker = null
            },
            onMiliClick = { milliseconds ->
                val selectedMillis = milliseconds
                if (isInitial) {
                    initialMillis.value = initialMillis.value.toMutableList().apply {
                        this[periodo - 1] = selectedMillis
                    }.toLongArray()
                } else {
                    finalMillis.value = finalMillis.value.toMutableList().apply {
                        this[periodo - 1] = selectedMillis
                    }.toLongArray()
                }
            }
        )
    }

    fun updateTotalMillis() {
        for (i in 0 until 4) {
            calMillis.value[i] = finalMillis.value[i] - initialMillis.value[i]
        }
        sumMillisNormal = calMillis.value[0] + calMillis.value[1]
        sumMillisExtra = calMillis.value[2] + calMillis.value[3]
        sumMillisTotal = sumMillisNormal + sumMillisExtra
    }

    LaunchedEffect(finalMillis.value) {
        updateTotalMillis()
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
                .fillMaxWidth()
                .height(75.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Divider(
                        color = Azul2,
                        modifier = Modifier
                            .padding(start = 30.dp, top = 12.dp)
                            .width(62.dp)
                            .height(1.dp)
                    )
                    Box(
                        modifier = Modifier
                            .rotate(-90f)
                            .background(
                                color = Azul2,
                                shape = RoundedCornerShape(100)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${dateSelected.year}",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical = 1.dp, horizontal = 13.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (dateSelected.dayOfMonth < 10) "0${dateSelected.dayOfMonth}" else "${dateSelected.dayOfMonth}",
                            fontSize = 38.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = getMonthAbbreviation(dateSelected.monthValue),
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(55.dp)
                            .padding(start = 95.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = "Date",
                            tint = Azul1,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    showCustomDatePicker = true
                                }
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "Horas Trabalhadas",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleMedium,
                        color = Azul1
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 13.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = if (totalHoursT < 10) "+0${totalHoursT}h" else "+${totalHoursT}h",
                            fontSize = 38.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = if (totalMinutesT < 10) "0${totalMinutesT}min" else "${totalMinutesT}min",
                            fontSize = 23.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier
                                .padding(bottom = 2.5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Azul2,
                                    shape = RoundedCornerShape(100)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                            ) {
                                Text(
                                    text = if (totalHoursN < 10) "0${totalHoursN}h" else "${totalHoursN}h",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = if (totalMinutesN < 10) "0${totalMinutesN}min" else "${totalMinutesN}min",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Verde2,
                                    shape = RoundedCornerShape(100)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                            ) {
                                Text(
                                    text = if (totalHoursE < 10) "0${totalHoursE}h" else "${totalHoursE}h",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                                Text(
                                    text = if (totalMinutesE < 10) "0${totalMinutesE}min" else "${totalMinutesE}min",
                                    fontSize = 14.sp,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        val periodTexts = listOf("MANHÃ", "TARDE", "EXTRA", "EXTRA")

        for (i in periodTexts.indices) {
            PeriodTime(
                text = periodTexts[i],
                initialTime = initialTimes.value[i],
                finalTime = finalTimes.value[i],
                onInitialTimeClick = { currentPicker = Pair(i + 1, true) },
                onFinalTimeClick = { currentPicker = Pair(i + 1, false) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
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
                                var message = true

                                scope.launch(Dispatchers.IO) {
                                    if (initialTimes.value[0].isEmpty() || finalTimes.value[0].isEmpty()) {
                                        message = false
                                    } else if (dateRegistro == "") {
                                        registrosRepository.salvarRegistros(
                                            "${LocalDate.now()}",
                                            initialTimes.value[0], finalTimes.value[0],
                                            initialTimes.value[1], finalTimes.value[1],
                                            initialTimes.value[2], finalTimes.value[2],
                                            initialTimes.value[3], finalTimes.value[3],
                                            totalNormal, totalExtra, totalTime
                                        )
                                        dateSelected = LocalDate.now()
                                        for (i in initialTimes.value.indices) {
                                            initialTimes.value[i] = ""
                                            finalTimes.value[i] = ""
                                        }
                                        for (i in initialMillis.value.indices) {
                                            initialMillis.value[i] = 0L
                                            finalMillis.value[i] = 0L
                                            calMillis.value[i] = 0L
                                        }
                                        sumMillisTotal = 0L
                                        sumMillisNormal = 0L
                                        sumMillisExtra = 0L
                                        message = true
                                    } else {
                                        registrosRepository.salvarRegistros(
                                            dateRegistro,
                                            initialTimes.value[0], finalTimes.value[0],
                                            initialTimes.value[1], finalTimes.value[1],
                                            initialTimes.value[2], finalTimes.value[2],
                                            initialTimes.value[3], finalTimes.value[3],
                                            totalNormal, totalExtra, totalTime
                                        )
                                        dateSelected = LocalDate.now()
                                        for (i in initialTimes.value.indices) {
                                            initialTimes.value[i] = ""
                                            finalTimes.value[i] = ""
                                        }
                                        for (i in initialMillis.value.indices) {
                                            initialMillis.value[i] = 0L
                                            finalMillis.value[i] = 0L
                                            calMillis.value[i] = 0L
                                        }
                                        sumMillisTotal = 0L
                                        sumMillisNormal = 0L
                                        sumMillisExtra = 0L
                                        message = true
                                    }
                                }

                                scope.launch(Dispatchers.Main) {
                                    if (message) {
                                        Toast.makeText(
                                            context,
                                            "Dados Salvos com Sucesso!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Preencha os campos obrigatórios",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_save),
                            contentDescription = "Salvar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Salvar",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = Azul1
                    )
                }

                if (deleteOption) {
                    Spacer(modifier = Modifier.width(16.dp)) // Espaço entre os botões

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Vermelho,
                                    shape = CircleShape
                                )
                                .size(45.dp)
                                .clickable {
                                    Toast.makeText(context, "Registro Deletado", Toast.LENGTH_SHORT)
                                        .show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Deletar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Deletar",
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = Vermelho
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getMonthAbbreviation(month: Int): String {
    return when (month) {
        1 -> "jan"
        2 -> "fev"
        3 -> "mar"
        4 -> "abr"
        5 -> "mai"
        6 -> "jun"
        7 -> "jul"
        8 -> "ago"
        9 -> "set"
        10 -> "out"
        11 -> "nov"
        12 -> "dez"
        else -> ""
    }
}

@SuppressLint("DefaultLocale")
fun convertMillisToTime(totalMillis: Long): Triple<String, Int, Int> {

    val hours = (totalMillis / 1000) / 3600 // 1 hora = 3600 segundos = 3.600.000 milissegundos
    val minutes = ((totalMillis / 1000) % 3600) / 60 // Resto da divisão por 3600 para obter os minutos
    val formattedTime = String.format("%02dh %02dmin", hours, minutes)

    return Triple(formattedTime, hours.toInt(), minutes.toInt())
}