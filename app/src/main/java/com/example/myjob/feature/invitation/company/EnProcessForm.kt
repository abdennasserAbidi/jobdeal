package com.example.myjob.feature.invitation.company

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myjob.R
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.entities.invitation.InvitationStatus
import com.example.myjob.domain.entities.invitation.SituationCandidate
import com.example.myjob.ui.theme.WhatsAppLightGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EnProcessForm(
    invitationModel: InvitationModel,
    onDismissRequest: () -> Unit,
    onConfirmation: (InvitationModel) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    var situationCandidate by remember { mutableStateOf(SituationCandidate.IDLE) }
    var reason by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {

        Card(shape = RoundedCornerShape(22.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val whatsAppGreen = colorResource(id = R.color.whatsapp)

                Text(
                    text = stringResource(id = R.string.opinion_text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Accept Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                situationCandidate = SituationCandidate.ACCEPTED
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (situationCandidate == SituationCandidate.ACCEPTED)
                                WhatsAppLightGreen else MaterialTheme.colorScheme.surface
                        ),
                        border = if (situationCandidate == SituationCandidate.ACCEPTED)
                            BorderStroke(2.dp, whatsAppGreen) else null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(id = R.string.accept_text),
                                tint = if (situationCandidate == SituationCandidate.ACCEPTED)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.accept_text),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (situationCandidate == SituationCandidate.ACCEPTED)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = if (situationCandidate == SituationCandidate.ACCEPTED)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Rejected Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                situationCandidate = SituationCandidate.REJECTED
                            },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (situationCandidate == SituationCandidate.REJECTED)
                                WhatsAppLightGreen else MaterialTheme.colorScheme.surface
                        ),
                        border = if (situationCandidate == SituationCandidate.REJECTED)
                            BorderStroke(2.dp, whatsAppGreen) else null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.reject_text),
                                tint = if (situationCandidate == SituationCandidate.REJECTED)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.reject_text),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (situationCandidate == SituationCandidate.REJECTED)
                                    FontWeight.Bold else FontWeight.Normal,
                                color = if (situationCandidate == SituationCandidate.REJECTED)
                                    whatsAppGreen else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                val text = when (situationCandidate) {
                    SituationCandidate.REJECTED -> stringResource(id = R.string.message_rejected_offer_text, invitationModel.fullName.toString())
                    SituationCandidate.ACCEPTED -> stringResource(id = R.string.message_accepted_offer_text, invitationModel.fullName.toString())
                    else -> ""
                }

                val color = if (situationCandidate == SituationCandidate.REJECTED) Color.Red
                else whatsAppGreen

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            tint = color,
                            contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                    }

                    Text(
                        text = text,
                        fontSize = 14.sp,
                        color = color
                    )
                }

                if (situationCandidate == SituationCandidate.REJECTED) {
                    OutlinedTextField(
                        value = reason,
                        onValueChange = {
                            reason = it
                        },
                        label = { Text("Message") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Message,
                                contentDescription = "Message",
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    )
                }

                Button(
                    onClick = {

                        val currentDate = Date()
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val formattedDate = formatter.format(currentDate)

                        val situation = if (situationCandidate == SituationCandidate.REJECTED) InvitationStatus.REJECTED
                        else InvitationStatus.HIRED

                        invitationModel.status = situation.name
                        invitationModel.reason = reason
                        invitationModel.dateEnd = formattedDate
                        onConfirmation(invitationModel)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.whatsapp),
                        contentColor = Color.White
                    )
                ) {

                    Text(stringResource(id = R.string.send_text), color = Color.White)
                }
            }
        }

    }


}