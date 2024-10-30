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
        initialMillisP1: Long,
        finalMillisP1: Long,
        initialMillisP2: Long,
        finalMillisP2: Long,
        initialMillisP3: Long,
        finalMillisP3: Long,
        initialMillisP4: Long,
        finalMillisP4: Long,
        sumMillisNormal: Long,
        sumMillisExtra: Long,
        sumMillisTotal: Long
    ) {
        dataSource.salvarRegistro(
            data,
            initialMillisP1,
            finalMillisP1,
            initialMillisP2,
            finalMillisP2,
            initialMillisP3,
            finalMillisP3,
            initialMillisP4,
            finalMillisP4,
            sumMillisNormal,
            sumMillisExtra,
            sumMillisTotal
        )
    }

    fun recuperarRegistro(data: String): Flow<MutableList<Registro>> {
        Log.d("RegistrosRepository", "Iniciando recuperação de registros para a data: $data")
        return dataSource.recuperarRegistro(data)
    }

    fun deletarRegistro(data: String) {
        Log.d("RegistrosRepository", "Iniciando deleção do registro para a data: $data")
        dataSource.deletarRegistro(data)
    }

    fun recuperarRegistrosEntreDatas(dataInicio: String, dataFim: String): Flow<MutableList<Registro>> {
        return dataSource.recuperarRegistrosEntreDatas(dataInicio, dataFim)
    }
}