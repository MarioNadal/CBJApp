package com.iessanalberto.dam2.cbjapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.test.assertIsDisplayed

import androidx.compose.ui.test.assertTextEquals


import androidx.compose.ui.test.junit4.createComposeRule

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText

import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import org.junit.Rule
import org.junit.Test



class IntegratedTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun passwordVisibleFunciona() {
        // Este test verifica el comportamiento de la visibilidad de la contraseña en un campo de contraseña.
        // Se verifica que al hacer clic en un botón de visibilidad, la contraseña se muestre o se oculte correctamente.

        rule.setContent {
            var passwordVisible by remember { mutableStateOf(false) }
            var otfValue by remember { mutableStateOf("") }

            OutlinedTextField(
                modifier = Modifier.testTag("otf"),
                value = otfValue,
                onValueChange = { otfValue = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.testTag("iconButton"),
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            modifier = Modifier.testTag("icon"),
                            painter = painterResource(R.drawable.ojocontrasena),
                            contentDescription = "Toggle password visibility"

                        )
                    }
                }
            )
        }
        rule.onNodeWithTag("iconButton").assertIsDisplayed()
        rule.onNodeWithTag("otf").performTextInput("password123")
        rule.onNodeWithTag("otf").assertTextEquals("•••••••••••")
        rule.onNodeWithTag("iconButton").performClick()
        rule.onNodeWithTag("otf").assertTextEquals("password123")
    }

    @Test
    fun buttonYTextFunciona(){
        // Este test verifica la interacción entre un botón y un texto.
        // Se verifica que el texto inicial sea correcto.
        // Luego, se hace clic en el botón y se verifica que el texto se actualice correctamente.

        rule.setContent{
            var value by remember { mutableStateOf(1) }
            val context = LocalContext.current
            Button(  modifier = Modifier.testTag("button"),onClick = {
                    value ++
            }){
                Text(text = "Mostrar Toast")

            }
            Text(modifier = Modifier.testTag("text"), text = value.toString())

        }
        rule.onNodeWithTag("text").assertIsDisplayed()
        rule.onNodeWithTag("text").assertTextEquals("1")
        rule.onNodeWithTag("button").assertIsDisplayed().performClick()
        rule.onNodeWithTag("text").assertTextEquals("2")

    }
}