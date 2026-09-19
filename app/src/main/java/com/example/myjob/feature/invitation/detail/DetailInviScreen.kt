package com.example.myjob.feature.invitation.detail

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.feature.navigation.Screen

@Composable
fun DetailInviScreen(
    navController: NavController,
    invitationDetailViewModel: InvitationDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {

    val scrollState = rememberScrollState()
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val deletedStatus by invitationDetailViewModel.deletedStatus.collectAsStateWithLifecycle()
    val c by invitationDetailViewModel.candidate.collectAsStateWithLifecycle()
    val invitationModel by invitationDetailViewModel.invitations.collectAsState()
    val language by invitationDetailViewModel.language.collectAsState()
    val invitation = invitationModel.invitationModel
    val userCompany = invitationModel.userCompany
    val userCandidate = invitationModel.userCandidate

    // Status Card
    val statusInvitations by invitationDetailViewModel.statusInvitations.collectAsState()
    val item = invitation.status ?: ""
    val statusCandidate = if (item != InvitationStatus.ON_HOLD.name)
        item else statusInvitations

    LaunchedEffect(deletedStatus) {
        if (deletedStatus == "removed successfully") {
            navController.popBackStack()
        }
    }

    InvitationDetailsScreen(
        status = statusCandidate,
        viewProfile = {
            GlobalEntries.userForCompany = it
            GlobalEntries.isFromDemand = false
            val route = if (it.role == "Candidat" || it.role == "Candidate") Screen.DetailScreen.route
            else Screen.DetailCompanyScreen.route
            navController.navigate(route)
        },
        onAcceptInvitation = {
            it.status = InvitationStatus.IN_PROCESS.name
            invitationDetailViewModel.acceptRejectInvitation(it)

            invitationDetailViewModel.clearToken()
            invitationDetailViewModel.getUserToken(it.idCompany)
            //acceptRejectInvitation = "accept"
        },
        onRejectInvitation = {
            it.status = InvitationStatus.NOT_INTERESTED.name
            invitationDetailViewModel.acceptRejectInvitation(it)

            invitationDetailViewModel.clearToken()
            invitationDetailViewModel.getUserToken(it.idCompany)
            //acceptRejectInvitation = "refuse"
        },
        onTerminateInvitation = {

        },
        invitation = invitation,
        onBack = { navController.popBackStack() },
        onAccept = {},
        onReject = {},
    )
}