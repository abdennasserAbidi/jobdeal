package com.example.myjob.domain.entities.demands

import android.view.View
import com.example.myjob.domain.entities.User
import com.example.myjob.feature.demands.DemandStatus
import com.example.myjob.feature.demands.ServiceCategory
import com.example.myjob.feature.demands.ToolCategory
import com.example.myjob.feature.demands.Urgency
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class MarketDemandModel(
    var id: Int = View.generateViewId(),
    var date: String? = "",
    var idSender: Int = 0,
    var userSender: User? = User(),
    var paidUser: Boolean? = false,
    var countTrial: Int = 10,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var title: String = "Dveloppeur Android",
    var category: ServiceCategory = ServiceCategory.IDLE,
    var otherCategory: String = "",
    var tools: ToolCategory? = ToolCategory.IDLE,
    var otherTools: String = "",
    var location: String = "",
    var budget: String = "",
    var urgency: String = Urgency.URGENT.displayName,
    var status: String = DemandStatus.OUVERT.displayName,
    var deadline: String = "",
    var images: List<String> = emptyList()
)