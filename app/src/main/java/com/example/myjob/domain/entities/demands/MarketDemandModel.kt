package com.example.myjob.domain.entities.demands

import android.view.View
import kotlinx.serialization.Serializable

@Serializable
data class MarketDemandModel(
    var id: Int = View.generateViewId(),
    var date: String? = "",
    var idSender: Int = 0,
    var idCompany: Int = 0,
    var idCandidate: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var username: String = "",
    var title: String = "Dveloppeur Android"
)