package com.dieyteixeira.registrohoras.datasource

import android.util.Log
import com.dieyteixeira.registrohoras.model.Registro
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate

class DataSource {

    private val db = FirebaseFirestore.getInstance()

    private val _registroCompleto = MutableStateFlow<MutableList<Registro>>(mutableListOf())
    private val registroCompleto: StateFlow<MutableList<Registro>> = _registroCompleto

    fun salvarRegistro(
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

        val registrosMap = hashMapOf(
            "data" to data,
            "initialMillisP1" to initialMillisP1,
            "finalMillisP1" to finalMillisP1,
            "initialMillisP2" to initialMillisP2,
            "finalMillisP2" to finalMillisP2,
            "initialMillisP3" to initialMillisP3,
            "finalMillisP3" to finalMillisP3,
            "initialMillisP4" to initialMillisP4,
            "finalMillisP4" to finalMillisP4,
            "sumMillisNormal" to sumMillisNormal,
            "sumMillisExtra" to sumMillisExtra,
            "sumMillisTotal" to sumMillisTotal
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

    fun deletarRegistro(data: String) {
        db.collection("registros").document(data).delete()
            .addOnCompleteListener {
                Log.d("DataSource", "Registro deletado com sucesso")
            }.addOnFailureListener { e ->
                Log.e("DataSource", "Erro ao deletar registro", e)
            }
    }

    fun recuperarRegistrosEntreDatas(dataInicio: String, dataFim: String): Flow<MutableList<Registro>> = callbackFlow {
        Log.d("DataSource", "Iniciando recuperação de registros entre $dataInicio e $dataFim")

        val listener = db.collection("registros")
            .whereGreaterThanOrEqualTo("data", dataInicio)
            .whereLessThanOrEqualTo("data", dataFim)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("DataSource", "Erro ao recuperar dados: ${error.message}", error)
                    close(error)
                    return@addSnapshotListener
                }

                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val listaRegistros: MutableList<Registro> = mutableListOf()
                    querySnapshot.forEach { documento ->
                        val registro = documento.toObject(Registro::class.java)
                        listaRegistros.add(registro)
                    }
                    trySend(listaRegistros).isSuccess
                } else {
                    trySend(mutableListOf()).isSuccess
                }
            }

        awaitClose {
            listener.remove()
        }
    }
}