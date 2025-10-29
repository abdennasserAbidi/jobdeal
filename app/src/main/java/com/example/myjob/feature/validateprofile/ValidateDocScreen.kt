package com.example.myjob.feature.validateprofile

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.feature.profile.test.FormTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateDocScreen(
    navController: NavController,
    selectImage: ActivityResultLauncher<String>,
    viewModel: InterviewValidationViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.validate_profile_text),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.whatsapp),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        val user by viewModel.user.collectAsState()
        val interactionSource = remember { MutableInteractionSource() }

        var docs by remember { mutableStateOf(mutableListOf("")) }

        docs.mapIndexed { index, doc ->
            FormTextField(
                value = doc,
                onValueChange = {
                    docs[index] = it
                },
                label = stringResource(id = R.string.add_document_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp),
                isRequired = true,
                readOnly = true,
                focusChange = { isFocused ->
                    if (isFocused) {
                        selectImage.launch("image/*")
                    }
                }
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier
                    .padding(top = 5.dp, end = 20.dp)
                    .align(Alignment.CenterEnd)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        docs = (docs + "").toMutableList()
                    },
                color = colorResource(id = R.color.whatsapp),
                text = stringResource(id = R.string.add_document_text),
            )
        }

        Button(
            onClick = {

                viewModel.changeDocs(docs)
                GlobalEntries.stepShared = 0

                val validationProfileStatus = ValidationProfileStatus()
                validationProfileStatus.typeValidation = "doc"
                validationProfileStatus.status = VerificationStatus.PENDING_REVIEW.name
                viewModel.validate(validationProfileStatus)

                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 20.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.whatsapp)
            )
        ) {
            Text(
                stringResource(id = R.string.save_text),
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

    }
}