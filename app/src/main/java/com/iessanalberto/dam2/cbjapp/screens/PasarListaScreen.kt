package com.iessanalberto.dam2.cbjapp.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.iessanalberto.dam2.cbjapp.data.Jugador
import androidx.compose.ui.Modifier
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.Button
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.sourceInformation
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.loginfactoriaproyectos.navigation.AppScreens
import com.iessanalberto.dam2.cbjapp.data.Asistencia
import com.iessanalberto.dam2.cbjapp.viewmodels.PasarListaScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun PasarListaScreen(navController: NavController,
                   viewModel: PasarListaScreenViewModel,
                   equipo: String) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialogBorrado by remember { mutableStateOf(false) }
    var showEliminar by remember { mutableStateOf(false) }
    val pasarListaScreenUiState by viewModel.uiState.collectAsState()
    val jugadoresByGrupo by viewModel.getJugadoresByGrupo(equipo).collectAsState(initial = emptyList())
    var verPrueba = remember { mutableStateOf(0)}

    var fecha: String by rememberSaveable { mutableStateOf(LocalDate.now().toString()) }

    val anio: Int
    val mes: Int
    val dia: Int
    val mCalendar = Calendar.getInstance()
    anio = mCalendar.get(Calendar.YEAR)
    mes = mCalendar.get(Calendar.MONTH)
    dia = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mDatePickerDialog =
        DatePickerDialog(LocalContext.current, { DatePicker, anio: Int, mes: Int, dia: Int -> // Crear un objeto Date
            fecha = "$dia/${mes+1}/$anio" // Almacenamos la fecha
            if (mes<10){
                viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf("$anio-0$mes-$dia"), playerNameUi = pasarListaScreenUiState.playerName, pasarListaScreenUiState.playerApellidos)
            }else{
                viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf("$anio-$mes-$dia"), playerNameUi = pasarListaScreenUiState.playerName, pasarListaScreenUiState.playerApellidos)
            }
        }, anio, mes, dia)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically){
                        Text(text = equipo)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF023CBA),
                    titleContentColor = Color.Black)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                backgroundColor = Color(0xFF023CBA),
                contentColor = Color(0xFF023CBA)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Jugador")
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(80.dp))
                Icon(imageVector = Icons.Filled.DateRange,
                    contentDescription = "Elige fecha",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clickable {
                            mDatePickerDialog.show()
                            verPrueba.value =
                                0  //Forzar la recarga de JugadorItem que salgan bien las nuevas asistencias si esta presente o ausente
                        })
                TextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    enabled = false
                )
                Spacer(modifier = Modifier.height(10.dp))
            if (jugadoresByGrupo.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No hay jugadores en este equipo")
                }
            } else {
                LazyColumn {
                    items(jugadoresByGrupo) { jugador ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { showDialogBorrado = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Jugador",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            if (showDialogBorrado) {
                                AlertDialog(
                                    onDismissRequest = { showDialogBorrado = false },
                                    title = {
                                        Text(
                                            text = "Confirmar Borrado de Jugador",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF6200EA)
                                        )
                                    },
                                    text = {
                                        Text(
                                            "¿Estás seguro de que deseas borrar este jugador ${jugador.name + " " + jugador.apellidos}?",
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                    },
                                    confirmButton = {
                                        Button(onClick = {
                                            viewModel.deleteJugador(jugador)
                                            showDialogBorrado = false
                                        }) {
                                            Text("Confirmar", color = Color.White)
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { showDialogBorrado = false }) {
                                            Text("Cancelar", color = Color.White)
                                        }
                                    }
                                )
                            }
                            var asistencias by remember { mutableStateOf(emptyList<Asistencia>()) }

                            LaunchedEffect(key1 = jugador.id) {
                                val asistenciasDeJugador = viewModel.getAsistencias(jugador.id)
                                asistencias = asistenciasDeJugador
                            }

                            JugadorItem(
                                jugador = jugador,
                                viewModel = viewModel,
                                verPrueba
                            )
                        }
                    }
                }
            }
        }
        }

        if (showDialog) {
            val context = LocalContext.current

            Dialog(onDismissRequest = { "Nada" }) {
                // Contenido del cuadro de diálogo para agregar jugador
                // Aquí puedes incluir campos de entrada para el nombre y el equipo del jugador
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Aquí puedes agregar campos de entrada para el nombre y el equipo del jugador
                    // Campo de entrada para el nombre del jugador
                    // Modifica este TextField según tus necesidades
                    Text(text = "Añade un nuevo jugador",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EA))
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = pasarListaScreenUiState.playerName.value,
                        onValueChange = { viewModel.onChanged(fechaSeleccionadaUi = pasarListaScreenUiState.fechaSeleccionada, playerNameUi = mutableStateOf(it), pasarListaScreenUiState.playerApellidos)},
                        label = { Text("Nombre del Jugador") }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // Campo de entrada para los apellidos del jugador
                    // Modifica este TextField según tus necesidades
                    OutlinedTextField(
                        value = pasarListaScreenUiState.playerApellidos.value,
                        onValueChange = {viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf("$anio-$mes-$dia"), playerNameUi = pasarListaScreenUiState.playerName, mutableStateOf(it))},
                        label = { Text("Apellidos del jugador") }
                    )
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    Row {
                        // Botón para no añadir el jugador
                        Button(onClick = {showDialog = false
                            viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf(viewModel.uiState.value.fechaSeleccionada.value), playerNameUi = mutableStateOf(""), mutableStateOf(""))}) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        // Botón para agregar el jugador
                        Button(
                            onClick = {
                                val jugador = Jugador(
                                    name = pasarListaScreenUiState.playerName.value,
                                    apellidos = pasarListaScreenUiState.playerApellidos.value,
                                    equipo = equipo
                                )
                                if (viewModel.anadirJugador() == 1) {
                                    Toast.makeText(
                                        context,
                                        "Debe rellenar ambos campos",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else {
                                    var date = dateFormat.parse(viewModel.uiState.value.fechaSeleccionada.value)
                                    dateFormat.parse(viewModel.uiState.value.fechaSeleccionada.value)
                                        ?.let { it1 -> viewModel.insertJugador(jugador, date) }
                                    showDialog = false
                                    viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf(viewModel.uiState.value.fechaSeleccionada.value), playerNameUi = mutableStateOf(""), mutableStateOf(""))
                                }
                            }
                        ) {
                            Text("Agregar")
                        }
                    }
                }
            }
        }
    }

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable

fun JugadorItem(jugador: Jugador, viewModel: PasarListaScreenViewModel, verPrueba: MutableState<Int>) {
    val formato = SimpleDateFormat("yyyy-MM-dd")
    val context = LocalContext.current

    // Estado de la asistencia del jugador para la fecha seleccionada
    var asistencia: Asistencia? by remember { mutableStateOf(null) }

    LaunchedEffect(verPrueba.value, viewModel.uiState.value.fechaSeleccionada.value) {
        try {
            withContext(Dispatchers.IO) {
                // Obtener la asistencia del jugador para la fecha seleccionada
                asistencia = viewModel.getAsistenciaPorFecha(
                    jugador.id,
                    formato.parse(viewModel.uiState.value.fechaSeleccionada.value)
                )

                // Si no hay asistencia para el jugador en la fecha seleccionada, crear una nueva asistencia
                if (asistencia == null) {
                    viewModel.registrarAsistencia(
                        jugadorId = jugador.id,
                        fecha = formato.parse(viewModel.uiState.value.fechaSeleccionada.value),
                        presente = false // Por defecto, la nueva asistencia se establece como ausente
                    )
                    // Actualizar el estado de la asistencia después de la creación
                    asistencia = Asistencia(jugadorId = jugador.id, fecha = formato.parse(viewModel.uiState.value.fechaSeleccionada.toString()), presente = false)
                }
            }
        } catch (e: Exception) {
            println("Error al obtener o crear la asistencia")
        }
    }

    Row(
        modifier = Modifier
            .clickable {
                // Si hay asistencia para el jugador en la fecha seleccionada, alternar el estado de presente
                asistencia?.let { currentAsistencia ->
                    val nuevoPresente = !currentAsistencia.presente
                    viewModel.registrarAsistencia(
                        jugadorId = jugador.id,
                        fecha = formato.parse(viewModel.uiState.value.fechaSeleccionada.value),
                        presente = nuevoPresente
                    )
                }
                verPrueba.value++
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        verPrueba.value++
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${jugador.name} ${jugador.apellidos}",
                style = MaterialTheme.typography.body1
            )
            // Si el jugador está ausente, mostrar un campo de entrada de texto para el comentario
            Text(
                text = if (asistencia?.presente == true) "Presente" else "Ausente",
                style = MaterialTheme.typography.body2,
                color = if (asistencia?.presente == true) Color.Green else Color.Red
            )
        }
        // Icono para representar el estado de asistencia
        Icon(
            imageVector = if (asistencia?.presente == true) Icons.Default.Check else Icons.Default.Close,
            contentDescription = if (asistencia?.presente == true) "Presente" else "Ausente",
            tint = if (asistencia?.presente == true) Color.Green else Color.Red
        )
    }
}
