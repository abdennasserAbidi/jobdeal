package com.example.myjob.feature.profile

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myjob.R
import com.example.myjob.common.GlobalEntries
import com.example.myjob.common.getFileNameFromUri
import com.example.myjob.common.pdf.PdfViewer
import com.example.myjob.common.rememberLifecycleEvent
import com.example.myjob.domain.entities.Educations
import com.example.myjob.domain.entities.Experience
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.navigation.Screen
import com.example.myjob.feature.setting.SettingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CandidateProfile(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val expanded = remember { mutableStateListOf(true, false, false, false) }
    val enableNextCard = remember { mutableStateListOf(true, false, false) }

    val interactionSource = remember { MutableInteractionSource() }
    val user by profileViewModel.user.collectAsState()
    //val pageIndex by profileViewModel.pageIndex.collectAsState()

    var showPdf by remember { mutableStateOf(false) }

    val allExp by profileViewModel.allExp.collectAsState()
    profileViewModel.getAllExp(user.id ?: 0)

    val allEduc by profileViewModel.allEduc.collectAsState()
    profileViewModel.getAllEduc(user.id ?: 0)

    val scrollState = rememberScrollState()
    var positionTitle by remember { mutableStateOf(Offset.Zero) }

    var changeToolBar by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        changeToolBar = scrollState.value > 53f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (!expanded[0]) Modifier.verticalScroll(scrollState)
                    else Modifier.padding(end = 0.dp)
                )
                .verticalScroll(scrollState),
        ) {

            //First card
            Card(
                modifier = Modifier
                    .animateContentSize()
                    .padding(top = 85.dp)
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
                            expanded[3] = false
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
                                expanded[3] = false
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


            //third card
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
                                expanded[3] = false
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

            //resume card
            Card(
                modifier = Modifier
                    .animateContentSize()
                    .padding(start = 10.dp, top = 10.dp, bottom = 5.dp, end = 5.dp)
                    .fillMaxWidth(0.98f)
                    .height(80.dp),
                shape = RoundedCornerShape(20.dp),
                contentColor = Color.Blue,
                elevation = 5.dp
            ) {
                ResumeCard(
                    profileViewModel,
                    next = { withDetail ->
                        /*val destination = if (withDetail) Screen.CareerScreen.route
                        else {
                            profileViewModel.changeDestinationForm("profile")
                            Screen.CareerFormScreen.route
                        }

                        GlobalEntries.idExp = View.generateViewId()
                        navController.navigate(destination)*/
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
                        tint = White,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp),
                        contentDescription = ""
                    )

                    Text(
                        text = stringResource(id = R.string.exported_text),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        style = TextStyle(
                            color = White,
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
                            val file = createPdf(
                                profileViewModel,
                                allExp,
                                allEduc,
                                langState,
                                user,
                                0
                            )
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )
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
                        tint = White,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 20.dp),
                        contentDescription = ""
                    )

                    Text(
                        text = stringResource(id = R.string.share_text),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                        style = TextStyle(
                            color = White,
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }

        val shapeInit =  RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = colorResource(id = R.color.whatsapp)),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 15.dp)
                    .onGloballyPositioned { positionTitle = it.positionInRoot() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = White,
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
                    color = White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        if (showPdf) {
            val langState by profileViewModel.langState.collectAsState()

            val pageIndex by profileViewModel.pageIndex.collectAsState()

            val file = createPdf(profileViewModel, allExp, allEduc, langState, user, pageIndex)

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

fun calculateTextSize(text: String, textSize: Float): Pair<Float, Float> {
    val paint = Paint()
    paint.textSize = textSize // Set the desired text size in pixels

    // Calculate the width of the text
    val textWidth = paint.measureText(text)

    // Calculate the height of the text
    val fontMetrics = paint.fontMetrics
    val textHeight = fontMetrics.descent - fontMetrics.ascent

    return Pair(textWidth, textHeight)
}

fun createPdf(
    profileViewModel: ProfileViewModel,
    allExp: List<Experience>,
    allEduc: List<Educations>,
    lang: String,
    user: User,
    pageIndex: Int
): File {

    val pdfDocument = PdfDocument()
    val paint = Paint()
    val paintTitle = Paint()
    val pageWidth = 595 // A4 size in points (72 PPI)
    val pageHeight = 842
    val lineHeight = 30
    val maxItemsPerPage = 3

    var currentPage: PdfDocument.Page? = null
    var canvas: Canvas? = null
    var itemCount = 0

    var currentPageEducation: PdfDocument.Page? = null
    var canvasEducation: Canvas? = null
    var itemCountEducation = 0

    val padding = 50
    var yPosition = padding
    val xPosition = 250f

    val paddingEducation = yPosition + 50
    var yPositionEducation = paddingEducation

    // Create a new page
    val pageInfo1 = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val currentPage1 = pdfDocument.startPage(pageInfo1)
    val canvas1 = currentPage1?.canvas

    var startY = 25f

    paintTitle.textSize = 20f
    paintTitle.isFakeBoldText = true
    canvas1?.drawText("Your profile", (pageWidth / 2).toFloat(), startY, paintTitle)

    startY += 70f

    user.showUserList(lang).map {
        paintTitle.textSize = 20f
        paintTitle.isFakeBoldText = true
        paintTitle.textAlign = Paint.Align.LEFT
        canvas1?.drawText("${it.first} :  ", 10f, startY, paintTitle)
        canvas1?.drawText(it.second, 250f, startY, paint)
        startY += lineHeight // Move to the next line
    }

    allExp.mapIndexed { index, experience ->
        // Start a new page every 3 items or on the first item
        if (startY >= pageHeight - 100) {
            if (itemCount % maxItemsPerPage == 0) {
                pdfDocument.finishPage(currentPage1)
                // Finish the current page if it exists
                currentPage?.let { pdfDocument.finishPage(it) }

                // Create a new page
                val pageInfo = PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight,
                    (index / maxItemsPerPage) + 1
                ).create()
                currentPage = pdfDocument.startPage(pageInfo)
                canvas = currentPage?.canvas
                yPosition = padding // Reset Y position
            }

            // Draw content
            canvas?.apply {
                paintTitle.textSize = 20f
                paintTitle.isFakeBoldText = true
                drawText(
                    "Experience ${index + 1}:",
                    padding.toFloat(),
                    yPosition.toFloat(),
                    paintTitle
                )

                paint.textSize = 14f
                paint.isFakeBoldText = false
                yPosition += lineHeight
                experience.showUser1(lang).map {
                    drawText("${it.first} :  ", 10f, yPosition.toFloat(), paintTitle)
                    drawText(it.second, xPosition, yPosition.toFloat(), paint)
                    yPosition += 40
                }

                yPosition += 2 * lineHeight // Add spacing between items
            }

            itemCount++

        } else {
            // Draw content
            paintTitle.textSize = 20f
            paintTitle.isFakeBoldText = true
            paintTitle.textAlign = Paint.Align.LEFT
            startY += lineHeight // Move to the next line
            canvas1?.drawText("Experience ${index + 1}:", padding.toFloat(), startY, paintTitle)
            startY += lineHeight // Move to the next line

            paint.textSize = 14f
            paint.isFakeBoldText = false
            experience.showUser1(lang).map {
                canvas1?.drawText("${it.first} :  ", 10f, startY, paintTitle)
                canvas1?.drawText(it.second, xPosition, startY, paint)
                startY += lineHeight // Move to the next line
            }
        }
    }


    allEduc.mapIndexed { index, educations ->
        // Start a new page every 3 items or on the first item

        if (yPosition >= pageHeight - 100) {
            if (itemCountEducation % maxItemsPerPage == 0) {
                currentPage?.let { pdfDocument.finishPage(it) }
                // Finish the current page if it exists
                currentPageEducation?.let { pdfDocument.finishPage(it) }

                // Create a new page
                val pageInfo = PdfDocument.PageInfo.Builder(
                    pageWidth,
                    pageHeight,
                    (index / maxItemsPerPage) + 1
                ).create()
                currentPageEducation = pdfDocument.startPage(pageInfo)
                canvasEducation = currentPageEducation?.canvas
                yPositionEducation = paddingEducation // Reset Y position
            }

            // Draw content
            canvasEducation?.apply {
                paintTitle.textSize = 20f
                paintTitle.isFakeBoldText = true
                paintTitle.textAlign = Paint.Align.LEFT
                drawText(
                    "Education ${index + 1}:",
                    padding.toFloat(),
                    yPositionEducation.toFloat(),
                    paintTitle
                )

                paint.textSize = 14f
                paint.isFakeBoldText = false
                yPositionEducation += lineHeight
                educations.showEducationList(lang).map {
                    drawText("${it.first} :  ", 10f, yPositionEducation.toFloat(), paintTitle)
                    drawText(it.second, xPosition, yPositionEducation.toFloat(), paint)
                    yPositionEducation += 40
                }

                yPositionEducation += 2 * lineHeight // Add spacing between items
            }

            itemCountEducation++
        } else {
            paintTitle.textSize = 20f
            paintTitle.isFakeBoldText = true
            paintTitle.textAlign = Paint.Align.LEFT
            canvas?.drawText(
                "Education ${index + 1}:",
                padding.toFloat(),
                yPosition.toFloat(),
                paintTitle
            )

            paint.textSize = 14f
            paint.isFakeBoldText = false
            yPosition += lineHeight
            educations.showEducationList(lang).map {
                canvas?.drawText("${it.first} :  ", 10f, yPosition.toFloat(), paintTitle)
                canvas?.drawText(it.second, xPosition, yPosition.toFloat(), paint)
                yPosition += 40
            }

            yPosition += 2 * lineHeight // Add spacing between items
        }
    }

    currentPageEducation?.let { pdfDocument.finishPage(it) }

    val expectedName = user.fullName?.replace(" ", "")

    // Save the PDF to a file
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "${expectedName?.trim()}Detail.pdf"
    )

    try {
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        println("PDF saved to ${file.absolutePath}")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Error creating PDF: ${e.message}")
    } finally {
        pdfDocument.close()
    }

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
            .background(White),
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

fun uploadFile(
    profileViewModel: ProfileViewModel,
    context: Context,
    fileUri: Uri,
    pdfName: String
) {
    profileViewModel.uploadCV(context, fileUri, pdfName)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResumeCard(
    profileViewModel: ProfileViewModel,
    next: (withDetail: Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var pdfName by remember { mutableStateOf<String?>(null) }
    var expectedName by remember { mutableStateOf<String?>(null) }
    var isUploaded by remember { mutableStateOf(false) }
    var expend by remember { mutableStateOf(false) }
    var showPdf by remember { mutableStateOf(false) }

    val isExisting by profileViewModel.isExisting.collectAsState()
    val uploadMessage by profileViewModel.uploadMessage.collectAsState()
    val context = LocalContext.current

    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(lifecycleEvent) {
        Log.i("lifecycleExp", "login: $lifecycleEvent")

        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            pdfName = profileViewModel.getPDFName()
        }
    }

    // Launcher for opening a file picker
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            // Handle the selected PDF URI
            selectedPdfUri = uri
            pdfName = uri?.let { context.getFileNameFromUri(it) } // Get file name

            expectedName = if (pdfName?.isNotEmpty() == true) {
                if (pdfName?.contains("(") == true) "${
                    pdfName?.split(" ")?.get(0)
                }.pdf" else (pdfName ?: "")
            } else ""
            Log.i("fileUri", "createPdf: $expectedName")

            uri?.let {
                uploadFile(profileViewModel, context, it, expectedName ?: "")
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val text = if (isExisting) "View resume" else "Add resume"

        Text(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 10.dp),
            text = text,
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
        if (!isExisting) {
            Icon(
                modifier = Modifier
                    .weight(0.2f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        next(false)
                        pdfPickerLauncher.launch(arrayOf("application/pdf"))
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
                text = pdfName ?: "",
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
            .background(White),
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