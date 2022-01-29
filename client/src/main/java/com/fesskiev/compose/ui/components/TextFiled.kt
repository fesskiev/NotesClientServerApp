package com.fesskiev.compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.fesskiev.compose.R
import com.fesskiev.compose.ui.theme.Red300

@Composable
fun AsciiTextField(
    label: String,
    value: String,
    textStyle: TextStyle = TextStyle.Default,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    Box {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = textStyle.copy(color = MaterialTheme.colors.onSurface),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
        )
        if (value.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = label,
                style = if (isError) {
                    textStyle.copy(color = Red300)
                } else {
                    textStyle
                }
            )
        }
    }
}

@Composable
fun AsciiOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
    )
}

@Composable
fun EmailOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    val showPassword = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        textStyle = MaterialTheme.typography.body2,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.body2) },
        isError = isError,
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            if (showPassword.value) {
                IconButton(onClick = { showPassword.value = false }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_visibility_on),
                        contentDescription = ""
                    )
                }
            } else {
                IconButton(onClick = { showPassword.value = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_visibility_off),
                        contentDescription = ""
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}