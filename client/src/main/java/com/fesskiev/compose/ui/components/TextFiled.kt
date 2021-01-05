package com.fesskiev.compose.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AsciiTextField(
    label: String,
    textFieldState: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    isErrorValue: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isErrorValue = isErrorValue,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
    )
}

@Composable
fun EmailTextField(
    label: String,
    textFieldState: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    isErrorValue: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isErrorValue = isErrorValue,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}


@Composable
fun PasswordTextField(
    label: String,
    textFieldState: MutableState<TextFieldValue>,
    modifier: Modifier = Modifier,
    isErrorValue: Boolean = false
) {
    val showPassword = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        value = textFieldState.value,
        onValueChange = { textFieldState.value = it },
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isErrorValue = isErrorValue,
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(imageVector = Icons.Filled.Visibility)
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(imageVector = Icons.Filled.VisibilityOff)
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}