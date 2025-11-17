package com.example.myjob.feature.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.AnnouncementParams
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.usecase.announcement.AddCommentUseCase
import com.example.myjob.domain.usecase.announcement.AddLikeUseCase
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.RemoveLikeUseCase
import com.example.myjob.domain.usecase.announcement.SaveAnnouncementUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.local.database.SharedPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val sharedPreference: SharedPreference,
    private val getUserUseCase: GetUserUseCase,
    private val getAnnouncementUseCase: GetAnnouncementUseCase,
    private val removeLikeUseCase: RemoveLikeUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val saveAnnouncementUseCase: SaveAnnouncementUseCase
) : ViewModel() {

    private val _announcement: MutableStateFlow<PagingData<AnnouncementModel>> =
        MutableStateFlow(value = PagingData.empty())
    val announcement: MutableStateFlow<PagingData<AnnouncementModel>> get() = _announcement
    private fun getCompanyAnnouncement(idUser: Int) {
        viewModelScope.launch {
            getAnnouncementUseCase.execute(idUser)
                .collectLatest { res ->
                    _announcement.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    val announcementModel = MutableStateFlow(AnnouncementModel())
    fun changePostName(name: String) {
        announcementModel.update {
            it.title = name
            it
        }
    }

    fun changeDescriptions(name: String) {
        announcementModel.update {
            it.description = name
            it
        }
    }

    val annoucementStatus = MutableStateFlow("")

    fun saveCompanyAnnouncement() {
        val id = sharedPreference.getInt("idUser", 0)
        val announcementParams = AnnouncementParams(
            idUserConnected = id,
            announcementModel = announcementModel.value
        )
        viewModelScope.launch {
            saveAnnouncementUseCase.execute(announcementParams)
                .collectLatest { res ->
                    if (res.status == ResourceState.SUCCESS) {
                        annoucementStatus.update { res.data?.message ?: "" }
                        getCompanyAnnouncement(id)
                    }
                }
        }
    }


    private var _posts = MutableStateFlow(emptyList<AnnouncementModel>())
    val posts: StateFlow<List<AnnouncementModel>> get() =  _posts.asStateFlow()

    val userConnectedId = MutableStateFlow(-1)

    init {
        getCurrent()
        userConnectedId.update {
            sharedPreference.getInt("idUser", 0)
        }
    }

    val commentStatus = MutableStateFlow("")

    fun addComment(idAnnounce: Int, text: String) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val commentsPost = CommentsPost(
                idCandidate = idConnected,
                text = text
            )
            val pairParams = Pair(idAnnounce, commentsPost)
            addCommentUseCase.execute(pairParams).collectLatest { res ->
                commentStatus.update {
                    res.message ?: ""
                }
                if (res.message == "saved successfully") {
                    getCurrent()
                }
            }
        }
    }

    private fun getCurrent() {
        viewModelScope.launch {
            getUserUseCase.execute(sharedPreference.getInt("idUser", 0)).collect {
                it.data?.let { u ->
                    GlobalEntries.user = u
                    _posts.update {
                        u.announces ?: emptyList()
                    }
                }
            }
        }
    }

    val likesPostUser = MutableStateFlow(false)

    fun likePost(idAnnounce: Int) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val likesPost = LikesPost(
                idCandidate = idConnected
            )
            val pairParams = Pair(idAnnounce, likesPost)
            addLikeUseCase.execute(pairParams).collectLatest {
                if (it.message == "saved successfully") {
                    likesPostUser.update { true }
                    disLikesPostUser.update { false }
                    getCurrent()
                }
            }
        }
    }

    val disLikesPostUser = MutableStateFlow(false)

    fun disLikePost(idAnnounce: Int) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val pairParams = Pair(idAnnounce, idConnected)
            removeLikeUseCase.execute(pairParams).collectLatest {
                if (it.message == "saved successfully") {
                    disLikesPostUser.update { true }
                    likesPostUser.update { false }
                    getCurrent()
                }
            }
        }
    }

    val isLiked = MutableStateFlow(false)
    fun isNotLikedCandidate(listLike: List<LikesPost>): Boolean {
         val isNotLike = listLike.none {
            it.idCandidate == sharedPreference.getInt("idUser", 0)
        }

        isLiked.update { isNotLike }
        return isNotLike
    }

}