package com.example.myjob.feature.profile.test

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myjob.R
import com.example.myjob.common.tablayout.MyTabIndicatorRec
import com.example.myjob.common.tablayout.MyTabItemRect

@Composable
fun FormSectionHeader(
    title: String,
    subtitle: String
) {
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = whatsAppGreen
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FormTextField(
    value: String,
    borderColor: Color = colorResource(id = R.color.whatsapp),
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    minLines: Int = 1,
    isRequired: Boolean = false,
    isPassword: Boolean = false,
    readOnly: Boolean = false,
    focusChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {

    val whatsAppGreen = colorResource(id = R.color.whatsapp)
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        focusChange(isFocused)
    }
    val interactionSource = remember { MutableInteractionSource() }

    if (isPassword) {
        var showPassword by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label)
                    if (isRequired) {
                        Text(" *", color = Color.Red)
                    }
                }
            },
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = label) }
            },
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }, trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(imageVector = Icons.Filled.Visibility, contentDescription = "")
                    }
                } else {
                    IconButton(onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = ""
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                focusedLabelColor = whatsAppGreen,
                focusedLeadingIconColor = whatsAppGreen
            )
        )


    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(label)
                    if (isRequired) {
                        Text(" *", color = Color.Red)
                    }
                }
            },
            interactionSource = interactionSource,
            placeholder = { Text(placeholder) },
            leadingIcon = leadingIcon?.let { icon ->
                { Icon(icon, contentDescription = label) }
            },
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderColor,
                focusedBorderColor = borderColor,
                focusedLabelColor = whatsAppGreen,
                focusedLeadingIconColor = borderColor,
                unfocusedLeadingIconColor = borderColor
            )
        )

        // Collect press events from the interaction source
        if (interactionSource.collectIsPressedAsState().value) {
            LaunchedEffect(Unit) {
                onClick()
            }
        }
    }

}

@Composable
fun NavigationButtons(
    currentTab: Int,
    totalTabs: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onComplete: () -> Unit
) {
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (currentTab > 0) {
            OutlinedButton(
                onClick = onPrevious,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        if (currentTab < totalTabs - 1) {
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = whatsAppGreen
                )
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        } else {
            Button(
                onClick = onComplete,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = whatsAppGreen
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = "Complete")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Complete Profile")
            }
        }
    }
}

@Composable
fun SaveProfileDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val whatsAppGreen = colorResource(id = R.color.whatsapp)

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(32.dp),
                color = whatsAppGreen.copy(alpha = 0.1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Save",
                        modifier = Modifier.size(32.dp),
                        tint = whatsAppGreen
                    )
                }
            }
        },
        title = {
            Text(
                text = "Complete Profile?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you ready to save and complete your profile? You can always edit it later.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = whatsAppGreen
                )
            ) {
                Text("Complete Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Continue Editing")
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CustomOutlinedBox(
    modifier: Modifier,
    textLabel: String,
    placeholderLabel: String,
    withIcon: Boolean = false,
    onClick: () -> Unit,
    onFocus: (Boolean) -> Unit = {}
) {
    var text by remember { mutableStateOf(textLabel) }
    var placeholder by remember { mutableStateOf(placeholderLabel) }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isFocused) colorResource(id = R.color.whatsapp) else Color.Gray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusRequester.requestFocus()
                onClick()
            },
    ) {
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    isFocused = state.isFocused
                    onFocus(isFocused)
                }
        )

        if (text.isEmpty() && !isFocused) {
            Text(
                text = "Placeholder",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CustomRectangleTab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 140.dp,
    onClick: (index: Int) -> Unit,
) {

    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }

    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }

    val s = if (selectedItemIndex == 0) ((screenWidth/2) * selectedItemIndex)
    else (screenWidth/2) - 50.dp

    val indicatorOffset: Dp by animateDpAsState(
        targetValue = s,
        animationSpec = tween(easing = LinearEasing), label = "",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.whatsapp))
            .height(60.dp)
    ) {
        MyTabIndicatorRec(
            indicatorWidth = (screenWidth/2),
            indicatorOffset = indicatorOffset,
            indicatorColor = colorResource(id = R.color.ligt_white),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                MyTabItemRect(
                    isSelected = isSelected,
                    onClick = {
                        onClick(index)
                    },
                    tabWidth = tabWidth,
                    modifier = Modifier.weight(1f),
                    text = text,
                )
            }
        }
    }
}

@Composable
fun OutlinedBoxField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    placeholder: String? = null,
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
    },
) {
    val borderColor = if (enabled) Color.Gray else Color.LightGray
    val labelColor = if (value.isNotEmpty()) Color.Gray else Color.Gray.copy(alpha = 0.6f)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true),
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Floating label
        if (value.isNotEmpty()) {
            Text(
                text = label,
                color = labelColor,
                fontSize = 12.sp,
                modifier = Modifier
                    .background(Color.White)
                    .padding(horizontal = 4.dp)
                    .offset(y = (-10).dp)
                    .align(Alignment.TopStart)
            )
        }

        // Main content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (value.isEmpty()) (placeholder ?: label) else value,
                color = if (value.isEmpty()) Color.Gray.copy(alpha = 0.6f) else Color.Black,
                modifier = Modifier.weight(1f)
            )
            trailingIcon?.invoke()
        }
    }
}

@Composable
fun OutlinedBox(
    label: String,
    value: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    helperText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    content: @Composable (BoxScope.() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    // Animations
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFEF4444)
            isFocused -> Color(0xFF4F46E5)
            !enabled -> Color(0xFFE5E7EB)
            else -> Color(0xFFD1D5DB)
        },
        animationSpec = tween(durationMillis = 150),
        label = "border_color"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 2.dp else 1.dp,
        animationSpec = tween(durationMillis = 150),
        label = "border_width"
    )

    val labelColor by animateColorAsState(
        targetValue = when {
            isError -> Color(0xFFEF4444)
            isFocused -> Color(0xFF4F46E5)
            !enabled -> Color(0xFF9CA3AF)
            else -> Color(0xFF6B7280)
        },
        animationSpec = tween(durationMillis = 150),
        label = "label_color"
    )

    val labelSize by animateFloatAsState(
        targetValue = if (isFocused || value.isNotEmpty()) 12f else 16f,
        animationSpec = tween(durationMillis = 150),
        label = "label_size"
    )

    val labelOffset by animateDpAsState(
        targetValue = if (isFocused || value.isNotEmpty()) (-8).dp else 16.dp,
        animationSpec = tween(durationMillis = 150),
        label = "label_offset"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (enabled) Color.Transparent else Color(0xFFF9FAFB)
                )
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled
                ) {
                    isFocused = !isFocused
                    onClick()
                }
                .padding(horizontal = 16.dp)
        ) {
            // Label
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(y = labelOffset)
                    .background(Color.White)
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = labelSize.sp,
                    color = labelColor,
                    fontWeight = if (isFocused) FontWeight.Medium else FontWeight.Normal
                )
            }

            // Content Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterStart)
                    .padding(top = if (isFocused || value.isNotEmpty()) 8.dp else 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                leadingIcon?.invoke()

                if (content != null) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        content()
                    }
                } else if (value.isNotEmpty()) {
                    Text(
                        text = value,
                        fontSize = 16.sp,
                        color = if (enabled) Color(0xFF1F2937) else Color(0xFF9CA3AF),
                        modifier = Modifier.weight(1f)
                    )
                }

                trailingIcon?.invoke()
            }
        }

        // Helper or Error Text
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFEF4444),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        } else if (helperText != null) {
            Text(
                text = helperText,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}