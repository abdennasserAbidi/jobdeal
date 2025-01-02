package com.example.myjob.feature.profile

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.pdf.PdfViewer
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CandidateProfile(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val expanded = remember { mutableStateListOf(true, false, false) }
    val enableNextCard = remember { mutableStateListOf(true, false, false) }

    val interactionSource = remember { MutableInteractionSource() }
    val user by profileViewModel.user.collectAsState()

    var showPdf by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (!expanded[0]) Modifier.verticalScroll(rememberScrollState())
                    else Modifier.padding(end = 5.dp)
                ),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            profileViewModel.isFromLogin(false)
                            navController.popBackStack(
                                Screen.SettingScreen.route, false
                            )
                        },
                    contentDescription = ""
                )

                Text(
                    text = stringResource(id = R.string.profile_candidate_type_text),
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            //First card
            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .animateContentSize()
                    .fillMaxWidth(0.98f)
                    .then(
                        if (expanded[0]) Modifier.wrapContentHeight()
                        else Modifier.height(80.dp)
                    )
                    .padding(start = 10.dp, top = 10.dp, bottom = 5.dp, end = 5.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        expanded[0] = true
                        if (expanded[0]) {
                            expanded[1] = false
                            expanded[2] = false
                        }
                    },
                shape = RoundedCornerShape(20.dp),
                contentColor = Color.Blue,
                elevation = 5.dp
            ) {
                PersonalCard(
                    profileViewModel,
                    updateUser = {
                        //GlobalEntries.user = it
                        navController.navigate(Screen.PersonalFormScreen.route)
                    })
            }

            //Second card
            Card(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth(0.98f)
                    .height(80.dp)
                    .padding(start = 10.dp, top = 10.dp, bottom = 5.dp, end = 5.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (enableNextCard[1]) {
                            expanded[1] = true
                            if (expanded[1]) {
                                expanded[0] = false
                                expanded[2] = false
                            }
                        }
                    },
                shape = RoundedCornerShape(20.dp),
                contentColor = Color.LightGray,
                elevation = 5.dp
            ) {
                EducationCard(
                    profileViewModel,
                    expanded[1],
                    enableNextCard[1],
                    expandDetail = {
                        expanded[1] = true
                    },
                    skipp = {
                        expanded[1] = false
                    },
                    next = { withDetail ->
                        enableNextCard[2] = true
                        expanded[1] = false

                        val destination = if (withDetail) Screen.EducationScreen.route
                        else {
                            profileViewModel.changeDestinationExpForm("profile")
                            Screen.EducationFormScreen.route
                        }

                        GlobalEntries.idStudy = View.generateViewId()
                        navController.navigate(destination)
                    })
            }


            //Second card
            Card(
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth(0.98f)
                    .height(80.dp)
                    .padding(start = 10.dp, top = 10.dp, bottom = 5.dp, end = 5.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (enableNextCard[2]) {
                            expanded[2] = true
                            if (expanded[2]) {
                                expanded[0] = false
                                expanded[1] = false
                            }
                        }
                    },
                shape = RoundedCornerShape(20.dp),
                contentColor = Color.LightGray,
                elevation = 5.dp
            ) {
                CareerFilterCard(
                    profileViewModel,
                    next = { withDetail ->
                        val destination = if (withDetail) Screen.CareerScreen.route
                        else {
                            profileViewModel.changeDestinationForm("profile")
                            Screen.CareerFormScreen.route
                        }

                        GlobalEntries.idExp = View.generateViewId()
                        navController.navigate(destination)
                    })
            }

            val context = LocalContext.current

            val langState by profileViewModel.langState.collectAsState()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            showPdf = true
                        }
                        .background(
                            color = colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Print,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp),
                        contentDescription = ""
                    )

                    Text(
                        text = stringResource(id = R.string.exported_text),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val file = createPdf(langState, user)
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            context.startActivity(Intent.createChooser(intent, "Share PDF"))
                        }
                        .background(
                            color = colorResource(id = R.color.whatsapp),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Share,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp),
                        contentDescription = ""
                    )

                    Text(
                        text = stringResource(id = R.string.share_text),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }


        if (showPdf) {
            val langState by profileViewModel.langState.collectAsState()
            val file = createPdf(langState, user)

            var isLoading by remember { mutableStateOf(false) }
            var currentLoadingPage by remember { mutableStateOf<Int?>(null) }
            var pageCount by remember { mutableStateOf<Int?>(null) }

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PdfViewer(
                    modifier = Modifier.fillMaxSize(),
                    pdfResId = file,
                    loadingListener = { loading, currentPage, maxPage ->
                        isLoading = loading
                        if (currentPage != null) currentLoadingPage = currentPage
                        if (maxPage != null) pageCount = maxPage
                    }
                )
                if (isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            progress = if (currentLoadingPage == null || pageCount == null) 0f
                            else currentLoadingPage!!.toFloat() / pageCount!!.toFloat()
                        )
                        Text(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 5.dp)
                                .padding(horizontal = 30.dp),
                            text = "${currentLoadingPage ?: "-"} pages loaded/${pageCount ?: "-"} total pages"
                        )
                    }
                }
            }
        }
    }


}


fun createPdf(lang: String, user: User): File {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas: Canvas = page.canvas

    val paint = Paint()
    paint.textSize = 12f

    val paintTitle = Paint()
    paintTitle.textSize = 14f
    paintTitle.isFakeBoldText = true

    user.showUser(lang).mapValues {
        canvas.drawText("${it.key}: ${it.value}", 10f, 25f, paint)
    }

    user.experience?.mapIndexed { index, experience ->
        canvas.drawText("Experience $index  :", 10f, 25f, paintTitle)
        experience.showExperience(lang).mapValues {
            canvas.drawText("${it.key}: ${it.value}", 15f, 25f, paint)
        }
    }

    pdfDocument.finishPage(page)

    // Save the PDF to external storage
    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "form_data.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return file
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalCard(
    profileViewModel: ProfileViewModel,
    updateUser: (user: User) -> Unit
) {

    val langState by profileViewModel.langState.collectAsState()
    val user by profileViewModel.user.collectAsState()
    val showUser by profileViewModel.showUser.collectAsState()
    val withDetail by profileViewModel.withPersonalDetail.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.personal_info_text),
                color = colorResource(id = R.color.whatsapp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.rubik_medium,
                            weight = FontWeight.Medium
                        )
                    )
                )
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        updateUser(user)
                    },
                imageVector = Icons.Filled.Create,
                tint = Color.Black,
                contentDescription = "update"
            )
        }

        profileViewModel.getMapUser().mapValues {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp)
            ) {

                Text(
                    text = "${it.key} :",
                    modifier = Modifier.weight(0.3f),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = it.value,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(0.5f),
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EducationCard(
    profileViewModel: ProfileViewModel,
    isExpanded: Boolean,
    enableCard: Boolean,
    expandDetail: () -> Unit,
    skipp: () -> Unit,
    next: (withDetail: Boolean) -> Unit
) {

    val user by profileViewModel.user.collectAsState()
    val education by profileViewModel.educations.collectAsState()
    val educations: LazyPagingItems<Educations> =
        profileViewModel.education.collectAsLazyPagingItems()

    val educationView = remember {
        derivedStateOf { educations.itemCount != 0 }
    }

    val interactionSource = remember { MutableInteractionSource() }

    profileViewModel.getAllEducations(user.id ?: 0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 10.dp),
            text = "Educations",
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.rubik_medium,
                        weight = FontWeight.Medium
                    )
                )
            )
        )
        if (educationView.value) {
            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(200)
                            next(true)
                        }
                    },
                text = stringResource(id = R.string.view_detail_text),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.rubik_medium,
                            weight = FontWeight.Medium
                        )
                    )
                )
            )
        } else {
            Icon(
                modifier = Modifier
                    .weight(0.2f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        next(false)
                    },
                imageVector = Icons.Filled.Add,
                tint = Color.Black,
                contentDescription = "add"
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CareerFilterCard(
    profileViewModel: ProfileViewModel,
    next: (withDetail: Boolean) -> Unit
) {

    val user by profileViewModel.user.collectAsState()
    //val experience by profileViewModel.experience.collectAsState()
    val experience: LazyPagingItems<Experience> =
        profileViewModel.experience.collectAsLazyPagingItems()

    profileViewModel.getAllExperience(user.id ?: 0)
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 10.dp),
            text = "Experience",
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(
                    Font(
                        R.font.rubik_medium,
                        weight = FontWeight.Medium
                    )
                )
            )
        )
        //withDetail
        if (experience.itemCount == 0) {
            Icon(
                modifier = Modifier
                    .weight(0.2f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        next(false)
                    },
                imageVector = Icons.Filled.Add,
                tint = Color.Black,
                contentDescription = "add"
            )
        } else {
            Text(
                modifier = Modifier
                    .weight(0.3f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(200)
                            next(true)
                        }

                    },
                text = stringResource(id = R.string.view_detail_text),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(
                            R.font.rubik_medium,
                            weight = FontWeight.Medium
                        )
                    )
                )
            )
        }
    }
}