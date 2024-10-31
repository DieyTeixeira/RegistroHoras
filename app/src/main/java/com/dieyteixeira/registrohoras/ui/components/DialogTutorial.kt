package com.dieyteixeira.registrohoras.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.ui.theme.Azul1

@Composable
fun TutorialDialog(onDismiss: () -> Unit) {
    var currentScreen by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tutorial",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = getImageForScreen(currentScreen)),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getTextForScreen(currentScreen),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = Azul1,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (currentScreen < 12) {
                    currentScreen += 1
                } else {
                    onDismiss()
                }
            }) {
                Text(
                    text = if (currentScreen < 12) "Avançar" else "Finalizar",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    )
}

// Função para retornar a imagem correspondente à tela
@Composable
fun getImageForScreen(screen: Int): Int {
    return when (screen) {
        1 -> R.drawable.screen01
        2 -> R.drawable.screen02
        3 -> R.drawable.screen03
        4 -> R.drawable.screen04
        5 -> R.drawable.screen05
        6 -> R.drawable.screen06
        7 -> R.drawable.screen07
        8 -> R.drawable.screen08
        9 -> R.drawable.screen09
        10 -> R.drawable.screen10
        11 -> R.drawable.screen11
        12 -> R.drawable.screen12
        else -> R.drawable.screen01 // Default caso algo dê errado
    }
}

// Função para retornar o texto correspondente à tela
@Composable
fun getTextForScreen(screen: Int): String {
    return when (screen) {
        1 -> "Clicar no campo para abrir o calendário"
        2 -> "Selecionar data desejada e clicar em ok"
        3 -> "Botão para buscar registros"
        4 -> "Botão para exibir relatório completo"
        5 -> "Tela de relatório com os registros"
        6 -> "Clicar ou arrastar para a esquerda para trocar de tela"
        7 -> "Botão para abrir o calendário, caso seja selecionada uma data que já tenha registro, os dados serão trazidos para a tela"
        8 -> "Selecionar data desejada e clicar em ok"
        9 -> "Clicar no campo para abrir o relógio"
        10 -> "Selecionar hora e minuto e clicar em ok"
        11 -> "Botão para salvar registro após preencher dados"
        12 -> "Botão para deletar registro"
        else -> "Essa é a primeira funcionalidade." // Default
    }
}

@Preview
@Composable
private fun TutorialDialogPreview() {
    TutorialDialog(onDismiss = {})
}