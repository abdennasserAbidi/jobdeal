package com.example.myjob.domain.entities.demands

import android.view.View
import com.example.myjob.domain.entities.User
import kotlinx.serialization.Serializable

@Serializable
data class MarketDemandModel(
    var id: Int = View.generateViewId(),
    var date: String? = "",
    var idSender: Int = 0,
    var userSender: User? = User(),
    var idCandidate: Int = 0,
    var paidUser: Boolean? = false,
    var countTrial: Int = 10,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var activitySector: String = "",
    var username: String = "",
    var title: String = "Dveloppeur Android"
)