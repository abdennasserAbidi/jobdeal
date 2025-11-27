package com.example.myjob.domain.entities.announcement

import android.view.View
import kotlinx.serialization.Serializable

enum class PostType {
    ALL, EVENT, INTERNSHIP, WORKSHOP
}

@Serializable
data class AnnouncementModel(
    var idAnnounce: Int = View.generateViewId(),
    var date: String? = "",
    var idCompany: Int = 0,
    var description: String = "Creer une application pour connecter les entreprises avec les candidats facilement.",
    var companyName: String = "",
    var title: String = "Dveloppeur Android",
    var postType: String = PostType.ALL.name,
    var status: StatusPost = StatusPost(),
    var comments: MutableList<CommentsPost>? = mutableListOf(),
    var likes: MutableList<LikesPost>? = mutableListOf()
)

@Serializable
data class StatusPost(
    var idStatus: Int = View.generateViewId(),
    var idCandidate: Int? = 0,
    var userName: String? = "",
)

@Serializable
data class CommentsPost(
    var idComment: Int = View.generateViewId(),
    var idCandidate: Int? = 0,
    var text: String? = "",
    var userName: String? = "",
)

@Serializable
data class LikesPost(
    var idLike: Int = View.generateViewId(),
    var idCandidate: Int? = 0,
    var userName: String? = "",
)
