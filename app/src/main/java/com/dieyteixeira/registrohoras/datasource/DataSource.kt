package com.dieyteixeira.registrohoras.datasource

import android.util.Log
import com.dieyteixeira.registrohoras.model.Registro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

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

    fun recuperarRegistro(data: String): Flow<MutableList<Registro>> = callbackFlow {
        Log.d("DataSource", "Iniciando recuperação de registros para a data: $data")

        val listener = db.collection("registros")
            .whereEqualTo("data", data)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("DataSource", "Erro ao recuperar dados: ${error.message}", error)
                    // Envia o erro para o coletor e fecha o fluxo
                    close(error)
                    return@addSnapshotListener
                }

                Log.d("DataSource", "Consulta bem-sucedida para a data: $data")

                if (!(querySnapshot == null || querySnapshot.isEmpty)) {
                    Log.d("DataSource", "Documentos encontrados: ${querySnapshot.size()}")

                    val listaRegistros: MutableList<Registro> = mutableListOf()
                    querySnapshot.forEach { documento ->
                        val dataBanco = documento.toObject(Registro::class.java)

                        // Log para verificar a estrutura do objeto recebido
                        Log.d("DataSource", "Registro recebido: $dataBanco")

                        // Adiciona o registro à lista
                        listaRegistros.add(dataBanco)
                    }

                    // Envia a lista para o coletor
                    if (listaRegistros.isNotEmpty()) {
                        Log.d("DataSource", "Total de registros enviados: ${listaRegistros.size}")
                        trySend(listaRegistros).isSuccess
                    } else {
                        Log.d("DataSource", "Nenhum registro encontrado para a data: $data")
                        trySend(listaRegistros).isSuccess // Envia uma lista vazia se nenhum registro for encontrado
                    }
                } else {
                    Log.d("DataSource", "Nenhuma consulta ou resultados vazios para a data: $data")
                    trySend(mutableListOf()).isSuccess // Envia uma lista vazia
                }
            }

        // Retornar um fechamento do listener quando não houver mais coletores
        awaitClose {
            Log.d("DataSource", "Fechando o listener para a recuperação de registros.")
            listener.remove()
        }
    }
}