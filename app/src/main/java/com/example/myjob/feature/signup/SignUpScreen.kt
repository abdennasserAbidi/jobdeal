package com.example.myjob.feature.signup

//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.base.MyApp
import com.example.myjob.common.CustomDialog
import com.example.myjob.common.GlobalEntries.emailGoogleAccount
import com.example.myjob.feature.demands.CategoryDialogItem
import com.example.myjob.feature.demands.NewFormTextField
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.login.gmail.GoogleAuthUiClient
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient
) {

    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp.toPx().toInt()
    }

    val isFirstNameValid by viewModel.isFirstNameValid.collectAsState()
    val isLastNameValid by viewModel.isLastNameValid.collectAsState()
    val isEmailValid by viewModel.isEmailValid.collectAsState()
    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    val user by viewModel.user.collectAsState()
    val isCompanyNameValid by viewModel.isCompanyNameValid.collectAsState()

    var selectedCategory by remember {
        mutableStateOf(
            if (user.category == ServiceCategory.IDLE) null
            else user.category
        )
    }
    var selectedCategoryText by remember { mutableStateOf(user.otherCategory) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(user.username) }
    var description by remember { mutableStateOf(user.bio) }

    var activatedCheckCategory by remember { mutableStateOf(false) }
    var textErrorCategory by remember { mutableStateOf("") }
    var activatedCheckUsername by remember { mutableStateOf(false) }
    var textErrorUsername by remember { mutableStateOf("") }

    var selectedIndex by remember { mutableStateOf(0) }

    var activatedCheckCompanyName by remember { mutableStateOf(false) }
    var activatedCheckEmail by remember { mutableStateOf(false) }
    var activatedCheckPassword by remember { mutableStateOf(false) }
    var activatedCheckFirstName by remember { mutableStateOf(false) }
    var activatedCheckLastName by remember { mutableStateOf(false) }
    var activatedCheckConfirmPassword by remember { mutableStateOf(false) }

    var userFirstName by remember { mutableStateOf(user.firstName ?: "") }
    val submitEnabled by remember { derivedStateOf { isFirstNameValid } }

    var userLastName by remember { mutableStateOf(user.lastName ?: "") }
    val lastNameVerified by remember { derivedStateOf { isLastNameValid } }

    var companyName by remember { mutableStateOf(user.companyName ?: "") }
    val companyNameVerified by remember { derivedStateOf { isCompanyNameValid } }

    var email by remember { mutableStateOf(emailGoogleAccount) }
    val emailVerified by remember { derivedStateOf { isEmailValid } }

    var password by remember { mutableStateOf(user.password ?: "") }
    val passwordVerified by remember { derivedStateOf { isPasswordValid } }

    var confirmPassword by remember { mutableStateOf("") }
    val isConfirmPasswordValid by viewModel.isConfirmPasswordValid.collectAsState()
    val confirmPasswordVerified by remember { derivedStateOf { isConfirmPasswordValid } }

    var isProgressing by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    val saveUserRes by viewModel.saveUserRes.collectAsState()

    var error by remember { mutableStateOf("") }

    val listCompanies by viewModel.listCompanies.collectAsState()

    val app = context.applicationContext as MyApp
    LaunchedEffect(listCompanies) {
        Log.i("jrkzgnrjzgbzrk", "SignUpScreen: $listCompanies")
        app.listCompanies.removeAt(app.listCompanies.lastIndex)
        if (!app.listCompanies.containsAll(listCompanies)) app.listCompanies.addAll(listCompanies)
    }

    LaunchedEffect(saveUserRes.token?.isNotEmpty()) {
        isProgressing = false
        if (saveUserRes.token?.isNotEmpty() == true) {
            if (selectedIndex == 1) {
                navController.navigate(Screen.SearchWordScreen.route)
            } else if (selectedIndex == 0) navController.navigate(Screen.CompanyProfileForm.route)
            else navController.navigate(Screen.DemandServiceScreen.route)
        }
    }

    if (showDialog) {
        isProgressing = false
        CustomDialog(isSuccess = false, message = error) {
            showDialog = false
            isProgressing = false
        }
    }

    LaunchedEffect(saveUserRes.messageError?.isNotEmpty()) {
        if (saveUserRes.messageError?.isNotEmpty() == true) {
            showDialog = true
            error = saveUserRes.messageError.toString()
        }
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if (googleAuthUiClient.getSignedInUser() != null) {
            Log.i("feklzhgzg", "SignUpScreen: ${googleAuthUiClient.getSignedInUser()}")
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            //onDirect()
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    viewModel.changeRole(stringResource(id = R.string.choose_companies_text))

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.create_account_text),
                        modifier = Modifier.padding(top = 20.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = FontFamily.Default,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    val listRole = listOf(
                        stringResource(id = R.string.choose_companies_text),
                        stringResource(id = R.string.choose_candidate_text),
                        stringResource(id = R.string.service_text)
                    )

                    RoleSection(
                        listRole = listRole,
                        interactionSource = interactionSource,
                        onClick = {
                            selectedIndex = it
                            viewModel.changeRole(listRole[it])
                            viewModel.changeRoleIndex(it)
                        }
                    )
                }

                if (selectedIndex == 2) {

                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Catégorie de service *",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val color =
                                if (activatedCheckCategory && (selectedCategory == ServiceCategory.IDLE || selectedCategory == null)) Red
                                else Transparent

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF9FAFB))
                                    .border(1.dp, color, RoundedCornerShape(12.dp))
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { showCategoryDialog = true }
                                    .padding(16.dp)
                            ) {
                                selectedCategory?.let { category ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(text = category.icon, fontSize = 24.sp)
                                        Text(
                                            text = category.displayName,
                                            color = Color(0xFF1F2937),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } ?: run {
                                    Text(
                                        text = "Sélectionner une catégorie",
                                        color = Color(0xFF9CA3AF),
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            if (activatedCheckCategory && (selectedCategory == ServiceCategory.IDLE || selectedCategory == null)) {
                                Text(
                                    modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                    text = "Vous devez choisir une categorie",
                                    color = Red
                                )
                            }

                        }
                    }

                    if (selectedCategory?.displayName == "Autre") {
                        com.example.myjob.feature.demands.FormTextField(
                            value = selectedCategoryText,
                            onValueChange = {
                                selectedCategoryText = it
                                viewModel.changeOtherCategory(it)
                            },
                            isCheckActivated = activatedCheckCategory,
                            isError = selectedCategoryText.isEmpty(),
                            errorText = "Vous devez remplir la categorie",
                            label = "Autre catégorie *",
                            modifier = Modifier.padding(top = 16.dp),
                            placeholder = "Ex: Forgeron"
                        )
                    }

                    // Title Field
                    com.example.myjob.feature.demands.FormTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            viewModel.changeServiceUserName(title)
                        },
                        isCheckActivated = activatedCheckUsername,
                        isError = title.isEmpty(),
                        errorText = "Vous devez remplir le nom",
                        label = "Titre de la demande *",
                        modifier = Modifier.padding(top = 16.dp),
                        placeholder = "Ex: Installation d'une porte en bois"
                    )

                    // Description Field
                    com.example.myjob.feature.demands.FormTextField(
                        value = description ?: "",
                        onValueChange = {
                            description = it
                            viewModel.changeDescriptions(description ?: "")
                        },
                        label = "Description détaillée *",
                        modifier = Modifier.padding(top = 16.dp),
                        placeholder = "Décrivez votre besoin en détail...",
                        minLines = 4,
                        maxLines = 6
                    )

                } else if (selectedIndex == 1) {
                    NewFormTextField(
                        value = userFirstName,
                        onValueChange = {
                            userFirstName = it
                            if (activatedCheckFirstName) viewModel.validateFirstName(it)
                            viewModel.changeUserFirstName(it)
                        },
                        label = stringResource(id = R.string.first_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.first_name_text)
                    )

                    if (activatedCheckFirstName) {
                        if (userFirstName.isEmpty() || !submitEnabled) {

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (submitEnabled) "Valid First name" else stringResource(id = R.string.first_name_error),
                                color = if (submitEnabled) Color.Green else Red
                            )
                        }
                    }

                    NewFormTextField(
                        value = userLastName,
                        onValueChange = {
                            userLastName = it
                            if (activatedCheckLastName) viewModel.validateLastName(it)
                            viewModel.changeUserLastName(it)
                        },
                        label = stringResource(id = R.string.last_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.last_name_text)
                    )

                    if (activatedCheckLastName) {
                        if (userLastName.isEmpty() || !lastNameVerified) {
                            Log.i("submitEnabled", "SignUpScreen: $lastNameVerified")

                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = stringResource(id = R.string.last_name_error),
                                color = Red
                            )
                        }
                    }

                } else if (selectedIndex == 0) {
                    //company
                    NewFormTextField(
                        value = companyName,
                        onValueChange = {
                            companyName = it
                            if (activatedCheckCompanyName) viewModel.validateCompanyName(it)
                            viewModel.changeCompanyName(it)
                        },
                        label = stringResource(id = R.string.company_name_text),
                        modifier = Modifier.padding(top = 16.dp),
                        isRequired = true,
                        placeholder = stringResource(id = R.string.company_name_text)
                    )

                    if (activatedCheckCompanyName) {
                        if (companyName.isEmpty() || !companyNameVerified) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                                text = if (companyNameVerified) "" else stringResource(id = R.string.company_name_error),
                                color = if (companyNameVerified) Color.Green else Red
                            )
                        }
                    }
                }

                NewFormTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (activatedCheckEmail) viewModel.validateEmail(it)
                        viewModel.changeUserEmail(it)
                    },
                    label = "Email",
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = "Email"
                )

                if (activatedCheckEmail) {
                    if (email.isEmpty() || !emailVerified) {
                        Log.i("submitEnabled", "SignUpScreen: $emailVerified")

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (emailVerified) "Valid First name" else stringResource(id = R.string.error_email),
                            color = if (emailVerified) Color.Green else Red
                        )
                    }
                }

                NewFormTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (activatedCheckPassword) viewModel.validatePassword(it)
                        viewModel.changeUserPassword(it)
                    },
                    isPassword = true,
                    label = stringResource(id = R.string.password_text),
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = stringResource(id = R.string.password_text)
                )

                if (activatedCheckPassword) {
                    if (password.isEmpty() || !passwordVerified) {

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = if (passwordVerified) "Valid First name" else stringResource(id = R.string.error_password),
                            color = if (passwordVerified) Color.Green else Red
                        )
                    }
                }

                NewFormTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        viewModel.confirmPassword(it)
                        if (activatedCheckConfirmPassword) viewModel.checkConfirmPassword(
                            password,
                            confirmPassword
                        )
                    },
                    label = stringResource(id = R.string.confirm_password_text),
                    modifier = Modifier.padding(top = 16.dp),
                    isRequired = true,
                    placeholder = stringResource(id = R.string.confirm_password_text),
                    isPassword = true
                )

                if (activatedCheckConfirmPassword) {
                    if (confirmPassword.isEmpty() || !confirmPasswordVerified) {
                        Log.i("confirmPasswordVerified", "SignUpScreen: $confirmPasswordVerified")

                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                            text = stringResource(id = R.string.confirm_password_error),
                            color = Red
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val checkAll = if (selectedIndex == 0) {
                                val companyNameValidator =
                                    viewModel.validateCompanyName(companyName)
                                if (!companyNameValidator) activatedCheckCompanyName = true
                                companyNameValidator
                            } else if (selectedIndex == 1) {
                                val firstNameValidator =
                                    viewModel.validateFirstName(userFirstName)
                                val lastNameValidator =
                                    viewModel.validateLastName(userLastName)

                                if (!firstNameValidator) activatedCheckFirstName = true
                                if (!lastNameValidator) activatedCheckLastName = true

                                firstNameValidator && lastNameValidator
                            } else {
                                val categoryValidator =
                                    (selectedCategory != null && selectedCategory != ServiceCategory.IDLE) || selectedCategoryText.isNotEmpty()

                                val usernameValidator = title.isNotEmpty()

                                if (!categoryValidator) activatedCheckCategory = true
                                if (!usernameValidator) activatedCheckUsername = true

                                categoryValidator && usernameValidator
                            }

                            val emailValidator = viewModel.validateEmail(email)
                            val passwordValidator = viewModel.validatePassword(password)
                            val confirmValidator =
                                viewModel.checkConfirmPassword(password, confirmPassword)

                            if (!emailValidator) activatedCheckEmail = true
                            if (!passwordValidator) activatedCheckPassword = true
                            if (!confirmValidator) activatedCheckConfirmPassword = true

                            if (checkAll && emailValidator && passwordValidator && confirmValidator) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    isProgressing = true
                                    delay(1000L)
                                    viewModel.saveUser(user)
                                }
                            }
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
                            stringResource(id = R.string.create_new_account_text),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(Screen.LoginScreen.route)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.have_account_text))
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(id = R.string.log_in_text),
                            textDecoration = TextDecoration.Underline,
                            color = Blue
                        )
                    }
                }
            }

            // Category Selection Dialog
            if (showCategoryDialog) {
                AlertDialog(
                    onDismissRequest = { showCategoryDialog = false },
                    title = { Text("Choisir une catégorie") },
                    text = {
                        LazyColumn {
                            items(ServiceCategory.entries.toTypedArray()) { category ->
                                CategoryDialogItem(
                                    category = category,
                                    onClick = {
                                        selectedCategory = category
                                        viewModel.changePostType(
                                            selectedCategory ?: ServiceCategory.IDLE
                                        )
                                        showCategoryDialog = false
                                    }
                                )
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {
                        TextButton(onClick = { showCategoryDialog = false }) {
                            Text("Annuler", color = Color(0xFF049344))
                        }
                    }
                )
            }

            if (isProgressing) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Card(
                        modifier = Modifier
                            .size(150.dp)
                            .background(shape = RoundedCornerShape(30.dp), color = Color.White),
                        elevation = 15.dp,
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(20.dp),
                                color = colorResource(id = R.color.whatsapp),
                                strokeWidth = 8.dp,
                                trackColor = Color.LightGray,
                                strokeCap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoleSection(
    listRole: List<String>,
    interactionSource: MutableInteractionSource,
    onClick: (Int) -> Unit = {}
) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.choose_role_text),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var selected by remember { mutableStateOf(0) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listRole.forEachIndexed { index, lang ->
                    val isSelected = index == selected
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    colorResource(id = R.color.whatsapp)
                                else
                                    Color(0xFFF3F4F6)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                selected = index
                                onClick(index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected)
                                Color(0xFFF3F4F6)
                            else
                                Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}