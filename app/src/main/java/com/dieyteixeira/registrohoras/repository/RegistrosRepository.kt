package com.dieyteixeira.registrohoras.repository

import android.util.Log
import com.dieyteixeira.registrohoras.datasource.DataSource
import com.dieyteixeira.registrohoras.model.Registro
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class RegistrosRepository {

    private val dataSource = DataSource()

    fun salvarRegistros(
        data: String,
        initialTimeP1: String,
        finalTimeP1: String,
        initialTimeP2: String,
        finalTimeP2: String,
        initialTimeP3: String,
        finalTimeP3: String,
        initialTimeP4: String,
        finalTimeP4: String,
        totalNormal: String,
        totalExtra: String,
        totalTime: String
    ) {
        dataSource.salvarRegistro(
            data,
            initialTimeP1,
            finalTimeP1,
            initialTimeP2,
            finalTimeP2,
            initialTimeP3,
            finalTimeP3,
            initialTimeP4,
            finalTimeP4,
            totalNormal,
            totalExtra,
            totalTime
        )
    }

    fun recuperarRegistro(data: String): Flow<MutableList<Registro>> {
        Log.d("RegistrosRepository", "Iniciando recuperação de registros para a data: $data")
        return dataSource.recuperarRegistro(data)
    }
}