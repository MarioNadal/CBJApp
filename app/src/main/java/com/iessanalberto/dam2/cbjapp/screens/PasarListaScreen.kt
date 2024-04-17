package com.iessanalberto.dam2.cbjapp.screens
//TODO ARREGLAR CAMPO QUE ESTA EN DISABLED Y NO CAMBIA LA FECHA PERO EN REALIDAD SI
//TODO CAMBIA DE PRESENTE A AUSENTE PERO NO RECARGA BIEN CON OTROS DATOS YA RECOGIDOS SI ESTA EN PRESEMTE UN DíA NO SE CAMBIA POR FECHA


import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.iessanalberto.dam2.cbjapp.data.Asistencia
import com.iessanalberto.dam2.cbjapp.viewmodels.PasarListaScreenViewModel
import kotlinx.coroutines.coroutineScope
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
    var showEliminar by remember { mutableStateOf(false) }
    val pasarListaScreenUiState by viewModel.uiState.collectAsState()
    val jugadoresByGrupo by viewModel.getJugadoresByGrupo(equipo).collectAsState(initial = emptyList())

    val anio: Int
    val mes: Int
    val dia: Int
    val mCalendar = Calendar.getInstance()
    anio = mCalendar.get(Calendar.YEAR)
    mes = mCalendar.get(Calendar.MONTH)+1
    dia = mCalendar.get(Calendar.DAY_OF_MONTH)
    val mDatePickerDialog =
        DatePickerDialog(LocalContext.current, { DatePicker, anio: Int, mes: Int, dia: Int -> // Crear un objeto Date
            if (mes<10){
                viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf("$anio-0$mes-$dia"))
            }else{
                viewModel.onChanged(fechaSeleccionadaUi = mutableStateOf("$anio-$mes-$dia"))
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = Color.Black)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
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
            Icon(imageVector = Icons.Filled.DateRange,
                contentDescription = "Elige fecha",
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clickable {
                        mDatePickerDialog.show()
                    })
            TextField(value = viewModel.uiState.value.fechaSeleccionada.value,
                onValueChange = {viewModel.uiState.value.fechaSeleccionada.value = it},
                enabled = false)
            LazyColumn {
                items(jugadoresByGrupo) { jugador ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (showEliminar) {
                            IconButton(onClick = { viewModel.deleteJugador(jugador) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Delete Jugador",
                                    tint = Color.Red,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                                var asistencias by remember { mutableStateOf(emptyList<Asistencia>()) }

                                LaunchedEffect(key1 = jugador.id) {
                                    val asistenciasDeJugador = viewModel.getAsistencias(jugador.id)
                                    asistencias = asistenciasDeJugador
                                }

                                JugadorItem(jugador = jugador, asistencias = asistencias, viewModel = viewModel)
                            }

                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { showEliminar = !showEliminar }) {
                Text(text = "Eliminar Jugadores")
            }
        }

        if (showDialog) {
            val playerName = remember { mutableStateOf("") }
            val playerApellidos = remember { mutableStateOf("") }

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
                    Text("Nombre:")
                    // Campo de entrada para el nombre del jugador
                    // Modifica este TextField según tus necesidades
                    TextField(
                        value = playerName.value,
                        onValueChange = { playerName.value = it },
                        label = { Text("Nombre del Jugador") }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    // Campo de entrada para el equipo del jugador
                    // Modifica este TextField según tus necesidades
                    Text("Apellidos:")
                    TextField(
                        value = playerApellidos.value,
                        onValueChange = { playerApellidos.value = it },
                        label = { Text("Apellidos del jugador") }
                    )
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    // Botón para agregar el jugador
                    Button(
                        onClick = {
                            val jugador = Jugador(
                                name = playerName.value,
                                apellidos = playerApellidos.value,
                                equipo = equipo
                            )
                            if (playerName.value.isNotEmpty() && playerApellidos.value.isNotEmpty()) {
                                var date = dateFormat.parse(viewModel.uiState.value.fechaSeleccionada.value)
                                dateFormat.parse(viewModel.uiState.value.fechaSeleccionada.value)
                                    ?.let { it1 -> viewModel.insertJugador(jugador, date) }
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Agregar")
                    }
                }
            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JugadorItem(jugador: Jugador, asistencias: List<Asistencia>, viewModel: PasarListaScreenViewModel) {
    // Obtener el estado de asistencia del jugador
    val presente = remember { mutableStateOf(asistencias.any { it.jugadorId == jugador.id && it.fecha.equals(LocalDate.now()) && it.presente }) }
    val formato = SimpleDateFormat("yyyy-MM-dd")

    // Composable para mostrar el nombre del jugador y su estado de asistencia
    Row(
        modifier = Modifier
            .clickable {
                presente.value = !presente.value
                viewModel.registrarAsistencia(jugador.id,formato.parse(viewModel.uiState.value.fechaSeleccionada.value), presente.value )}
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${jugador.name} ${jugador.apellidos}",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = if (presente.value) "Presente" else "Ausente",
                style = MaterialTheme.typography.body2,
                color = if (presente.value) Color.Green else Color.Red
            )
        }
        // Agregar un icono de check o cross para representar el estado de asistencia
        Icon(
            imageVector = if (presente.value) Icons.Default.Check else Icons.Default.Close,
            contentDescription = if (presente.value) "Presente" else "Ausente",
            tint = if (presente.value) Color.Green else Color.Red
        )
    }
}



