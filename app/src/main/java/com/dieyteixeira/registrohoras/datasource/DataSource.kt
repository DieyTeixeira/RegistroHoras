package com.dieyteixeira.registrohoras.datasource

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dieyteixeira.registrohoras.model.Registro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class DataSource {

    private val db = FirebaseFirestore.getInstance()

    private val _registroCompleto = MutableStateFlow<MutableList<Registro>>(mutableListOf())
    private val registroCompleto: StateFlow<MutableList<Registro>> = _registroCompleto

    fun salvarRegistro(
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

        val registrosMap = hashMapOf(
            "data" to data,
            "initialTimeP1" to initialTimeP1,
            "finalTimeP1" to finalTimeP1,
            "initialTimeP2" to initialTimeP2,
            "finalTimeP2" to finalTimeP2,
            "initialTimeP3" to initialTimeP3,
            "finalTimeP3" to finalTimeP3,
            "initialTimeP4" to initialTimeP4,
            "finalTimeP4" to finalTimeP4,
            "totalNormal" to totalNormal,
            "totalExtra" to totalExtra,
            "totalTime" to totalTime
        )

        db.collection("registros").document(data).set(registrosMap).addOnCompleteListener {
            Log.d("DataSource", "Registro salvo com sucesso")
        }.addOnFailureListener {
            Log.e("DataSource", "Erro ao salvar registro", it)
        }
    }

    fun recuperarRegistro(): Flow<MutableList<Registro>> {

        val listaRegistros: MutableList<Registro> = mutableListOf()

        db.collection("registros").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                for (documento in querySnapshot.result) {
                    val data = documento.toObject(Registro::class.java)
                    listaRegistros.add(data)
                    _registroCompleto.value = listaRegistros
                }
            }
        }
        return registroCompleto
    }
}