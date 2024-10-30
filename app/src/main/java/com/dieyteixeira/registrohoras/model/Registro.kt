package com.dieyteixeira.registrohoras.model

data class Registro(
    val data: String = "",
    val initialMillisP1: Long = 0L,
    val finalMillisP1: Long = 0L,
    val initialMillisP2: Long = 0L,
    val finalMillisP2: Long = 0L,
    val initialMillisP3: Long = 0L,
    val finalMillisP3: Long = 0L,
    val initialMillisP4: Long = 0L,
    val finalMillisP4: Long = 0L,
    val sumMillisNormal: Long = 0L,
    val sumMillisExtra: Long = 0L,
    val sumMillisTotal: Long = 0L
) {
    companion object {
        fun clear(): Registro {
            return Registro()
        }
    }
}