package com.iessanalberto.dam2.cbjapp

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.iessanalberto.dam2.cbjapp.viewmodels.PasarListaScreenViewModel
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Conjunto de pruebas unitarias utilizando el framework JUnit para realizar las pruebas pertinentes,
 * que evaluan el comportamiento de LoginScreenViewModel en diferentes escenarios.
 */

//Test unitarios para PasarListaScreen
class PasarListaScreenTestsUnitarios {
    //Inicialización del ViewModel para ser utilizado en las pruebas
    val viewModel = PasarListaScreenViewModel(application = Application())
    /**
     * Prueba para campos vacíos.
     * Se espera que el método devuelva 1.
     */
    @Test
    fun camposVacios() {
        viewModel.onChanged(
            mutableStateOf(""),
            mutableStateOf(""),
            mutableStateOf(""))
        Assert.assertEquals(1, viewModel.anadirJugador())
    }

    /**
     * Prueba para campos correctos
     * Se espera que el método devuelva 2.
     */
    @Test
    fun camposCorrectos() {
        viewModel.onChanged(
            mutableStateOf("05-05-2024"),
            mutableStateOf("Mario"),
            mutableStateOf("Nadal"))
        Assert.assertEquals(2, viewModel.anadirJugador())
    }
}