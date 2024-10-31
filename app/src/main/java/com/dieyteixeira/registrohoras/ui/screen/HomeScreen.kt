package com.dieyteixeira.registrohoras.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.registrohoras.R
import com.dieyteixeira.registrohoras.ui.components.Baseboard
import com.dieyteixeira.registrohoras.ui.components.TutorialDialog
import com.dieyteixeira.registrohoras.ui.components.clearTutorialPreferences
import com.dieyteixeira.registrohoras.ui.components.hasShownTutorial
import com.dieyteixeira.registrohoras.ui.components.nome
import com.dieyteixeira.registrohoras.ui.components.setHasShownTutorial
import com.dieyteixeira.registrohoras.ui.components.sobrenome
import com.dieyteixeira.registrohoras.ui.theme.Azul1
import com.dieyteixeira.registrohoras.ui.theme.Azul2
import com.dieyteixeira.registrohoras.ui.theme.AzulDegrade
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size })
    val selectedTabIndex = pagerState.currentPage

    var showTutorial by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!hasShownTutorial(context)) {
            showTutorial = true
        }
    }

    if (showTutorial) {
        TutorialDialog(onDismiss = {
            showTutorial = false
            setHasShownTutorial(context)
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulDegrade)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Azul1)
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .height(108.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_ponto),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(75.dp)
                        .padding(start = 20.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(108.dp)
                    .padding(20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        clearTutorialPreferences(context)
                    },
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = nome,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = sobrenome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 12.sp
                    ),
                    color = Color.White
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            containerColor = Azul2,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(horizontal = 25.dp)
                        .height(5.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        ) {
            HomeTabs.entries.forEachIndexed { index, currentTab ->
                Tab(
                    selected = selectedTabIndex == index,
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.LightGray,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(currentTab.ordinal)
                        }
                    },
                    modifier = Modifier.height(52.dp),
                    text = {
                        Column(
                            modifier = Modifier
                                .padding(bottom = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = if (selectedTabIndex == index)
                                    currentTab.selectedIcon else currentTab.unselectedIcon,
                                contentDescription = "Tab Icon",
                                modifier = Modifier.size(25.dp) // Ajuste do tamanho do ícone
                            )
                            Text(
                                text = currentTab.text,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(top = 0.dp) // Remove o espaçamento
                            )
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                            .padding(15.dp)
                    ) {
                        ListScreen()
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                            .padding(15.dp)
                    ) {
                        InsertScreen()
                    }
                }
            }
        }
    }
    Baseboard()
}

enum class HomeTabs(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val text: String = ""
) {
    List(
        unselectedIcon = Icons.Outlined.Analytics,
        selectedIcon = Icons.Filled.Analytics,
        text = "Relatório"
    ),
    Insert(
        unselectedIcon = Icons.Outlined.DateRange,
        selectedIcon = Icons.Filled.DateRange,
        text = "Lançamentos"
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}