package com.example.myjob.feature.posts

import android.util.Log
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
import com.example.myjob.domain.usecase.announcement.CheckUserLikeAllPostsUseCase
import com.example.myjob.domain.usecase.announcement.CheckUserLikeUseCase
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.GetCandidateAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.GetCommentPostCompanyUseCase
import com.example.myjob.domain.usecase.announcement.GetNumberCommentAllPostsUseCase
import com.example.myjob.domain.usecase.announcement.GetNumberLikeAllPostsUseCase
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
    private val getCandidateAnnouncementUseCase: GetCandidateAnnouncementUseCase,
    private val removeLikeUseCase: RemoveLikeUseCase,
    private val checkUserLikeUseCase: CheckUserLikeUseCase,
    private val checkUserLikeAllPostsUseCase: CheckUserLikeAllPostsUseCase,
    private val getNumberLikeAllPostsUseCase: GetNumberLikeAllPostsUseCase,
    private val getNumberCommentAllPostsUseCase: GetNumberCommentAllPostsUseCase,
    private val getCommentPostCompanyUseCase: GetCommentPostCompanyUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val saveAnnouncementUseCase: SaveAnnouncementUseCase
) : ViewModel() {

    init {
        getAnnouncementCandidate()
    }

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

    private val _announcementForCandidate: MutableStateFlow<PagingData<AnnouncementModel>> =
        MutableStateFlow(value = PagingData.empty())
    val announcementForCandidate: MutableStateFlow<PagingData<AnnouncementModel>> get() = _announcementForCandidate
    private fun getAnnouncementCandidate() {
        viewModelScope.launch {
            getCandidateAnnouncementUseCase.execute()
                .collectLatest { res ->
                    _announcementForCandidate.update {
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

    fun changePostType(name: String) {
        announcementModel.update {
            it.postType = name
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

    fun addComment(idAnnounce: Int, text: String, username: String) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val commentsPost = CommentsPost(
                idCandidate = idConnected,
                text = text,
                userName = username
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

    val isPostLiked = MutableStateFlow(false)
    private fun isPostLiked(idAnnounce: Int) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val pairParams = Pair(idAnnounce, idConnected)
            checkUserLikeUseCase.execute(pairParams).collectLatest { res ->
                isPostLiked.update { res.data ?: false }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CANDIDATE
    ///////////////////////////////////////////////////////////////////////////
    val isAllPostLiked = MutableStateFlow(emptyList<Boolean>())
    fun isAllPostLiked() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            checkUserLikeAllPostsUseCase.execute(idConnected).collectLatest { res ->
                isAllPostLiked.update { res.data ?: emptyList() }
            }
        }
    }

    val numberLikes = MutableStateFlow(emptyList<Int>())
    fun getPostNumberLikes() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            getNumberLikeAllPostsUseCase.execute(idConnected).collectLatest { res ->
                numberLikes.update { res.data ?: emptyList() }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // COMPANY
    ///////////////////////////////////////////////////////////////////////////
    val isAllPostLikedCompany = MutableStateFlow(emptyList<Boolean>())
    fun isAllPostLikedCompany() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            checkUserLikeAllPostsUseCase.execute(idConnected).collectLatest { res ->
                isAllPostLikedCompany.update { res.data ?: emptyList() }
            }
        }
    }

    val numberLikesCompany = MutableStateFlow(emptyList<Int>())
    fun getPostNumberLikesCompany() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            getNumberLikeAllPostsUseCase.execute(idConnected).collectLatest { res ->
                numberLikesCompany.update { res.data ?: emptyList() }
            }
        }
    }

    val numberCommentCompany = MutableStateFlow(emptyList<Int>())
    fun getPostNumberCommentCompany() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            getNumberCommentAllPostsUseCase.execute(idConnected).collectLatest { res ->
                numberCommentCompany.update { res.data ?: emptyList() }
            }
        }
    }

    val commentsCompany = MutableStateFlow(emptyList<CommentsPost>())
    fun getPostCommentsCompany(idAnnounce: Int) {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            val params = Pair(idAnnounce, idConnected)
            getCommentPostCompanyUseCase.execute(params).collectLatest { res ->
                commentsCompany.update { res.data ?: emptyList() }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // END
    ///////////////////////////////////////////////////////////////////////////
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
                    isPostLiked(idAnnounce)
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
                    isPostLiked(idAnnounce)
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