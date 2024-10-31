package com.dieyteixeira.registrohoras.ui.components

import android.content.Context

fun hasShownTutorial(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("hasShownTutorial", false)
}

fun setHasShownTutorial(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("hasShownTutorial", true)
        apply()
    }
}

fun clearTutorialPreferences(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        clear() // Limpa todas as preferências
        apply()
    }
}