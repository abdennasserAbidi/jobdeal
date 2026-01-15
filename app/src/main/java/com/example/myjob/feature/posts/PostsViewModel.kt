package com.example.myjob.feature.posts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.common.GlobalEntries
import com.example.myjob.domain.entities.InvitationFilter
import com.example.myjob.domain.entities.User
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.entities.announcement.AnnouncementParams
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.entities.announcement.LikesPost
import com.example.myjob.domain.entities.announcement.PostType
import com.example.myjob.domain.entities.invitation.InvitationModel
import com.example.myjob.domain.usecase.announcement.AddCommentUseCase
import com.example.myjob.domain.usecase.announcement.AddLikeUseCase
import com.example.myjob.domain.usecase.announcement.CheckUserLikeAllPostsUseCase
import com.example.myjob.domain.usecase.announcement.CheckUserLikeUseCase
import com.example.myjob.domain.usecase.announcement.DeleteAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.FindAnnounceCandidateUseCase
import com.example.myjob.domain.usecase.announcement.FindAnnounceCompanyUseCase
import com.example.myjob.domain.usecase.announcement.GetAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.GetCandidateAnnouncementUseCase
import com.example.myjob.domain.usecase.announcement.GetCommentPostCompanyUseCase
import com.example.myjob.domain.usecase.announcement.GetNumberCommentAllPostsUseCase
import com.example.myjob.domain.usecase.announcement.GetNumberCommentCompanyUseCase
import com.example.myjob.domain.usecase.announcement.GetNumberLikeAllPostsUseCase
import com.example.myjob.domain.usecase.announcement.GetPostUseCase
import com.example.myjob.domain.usecase.announcement.RemoveLikeUseCase
import com.example.myjob.domain.usecase.announcement.SaveAnnouncementUseCase
import com.example.myjob.domain.usecase.home.GetUserUseCase
import com.example.myjob.local.database.SharedPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    private val getNumberCommentCompanyUseCase: GetNumberCommentCompanyUseCase,
    private val getCommentPostCompanyUseCase: GetCommentPostCompanyUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val saveAnnouncementUseCase: SaveAnnouncementUseCase,
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase,
    private val findAnnounceCandidateUseCase: FindAnnounceCandidateUseCase,
    private val findAnnounceCompanyUseCase: FindAnnounceCompanyUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {

    init {
        getAnnouncementCandidate()
    }

    val post = MutableStateFlow(AnnouncementModel())

    fun getPostById(idPost: Int, idCompany: Int) {
        viewModelScope.launch {
            getPostUseCase.execute(Pair(idPost, idCompany))
                .collectLatest { res ->
                    post.update {
                        res.data ?: AnnouncementModel()
                    }

                }
        }
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

    val countList = MutableStateFlow(0)

    private val _announcementForCandidate: MutableStateFlow<PagingData<AnnouncementModel>> =
        MutableStateFlow(value = PagingData.empty())
    val announcementForCandidate: MutableStateFlow<PagingData<AnnouncementModel>> get() = _announcementForCandidate
    private fun getAnnouncementCandidate() {
        viewModelScope.launch {
            getCandidateAnnouncementUseCase.execute()
                .collectLatest { res ->

                    val count = sharedPreference.getInt("jsonCandidateAnnounceSize", 0)

                    countList.update { count }

                    _announcementForCandidate.update {
                        res.data ?: PagingData.empty()
                    }

                }
        }
    }

    fun getFilteredAnnounceCandidate(type: String) {
        when(type) {
            PostType.ALL.name -> getAnnouncementCandidate()
            else -> getAnnounceSearchCandidate(type)
        }
    }
    private fun getAnnounceSearchCandidate(type: String) {
        viewModelScope.launch {
            findAnnounceCandidateUseCase.execute(type).collectLatest { res ->
                if (res.status == ResourceState.SUCCESS) {
                    _announcementForCandidate.update {
                        res.data ?: PagingData.empty()
                    }
                }
            }
        }
    }

    fun getFilteredAnnounceCompany(type: String) {
        when(type) {
            PostType.ALL.name -> getCurrent()
            else -> getAnnounceSearchCompany(type)
        }
    }

    private fun getAnnounceSearchCompany(type: String) {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", -1)
            val params = Pair(type, idUser)
            findAnnounceCompanyUseCase.execute(params).collectLatest { res ->
                if (res.status == ResourceState.SUCCESS) {
                    _posts.update {
                        res.data?.announceModel ?: emptyList()
                    }
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
    val addedPost = MutableStateFlow(AnnouncementModel())

    fun saveCompanyAnnouncement() {
        val id = sharedPreference.getInt("idUser", 0)

        val currentDate = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)

        val announcementModels = announcementModel.value
        announcementModels.date = formattedDate

        announcementModel.update {
            announcementModels
        }

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
    val posts: StateFlow<List<AnnouncementModel>> get() = _posts.asStateFlow()

    private val _localItemRemoves = MutableStateFlow(-1)
    val deleteStatus = MutableStateFlow("")
    fun deleteCompanyAnnouncement(idAnnounce: Int) {
        viewModelScope.launch {
            val idUser = sharedPreference.getInt("idUser", 0)
            val param = Pair(idAnnounce, idUser)
            deleteAnnouncementUseCase.execute(param).collect { res ->
                /*if (res.status == ResourceState.SUCCESS) {
                    _localItemRemoves.update { idAnnounce }

                    _listSavedSearch.combine(_localItemRemoves) { pagingData, updates ->
                        pagingData.filter { item ->
                            item.id != updates
                        }
                    }.collect { data ->
                        combinedDataFlow.update {
                            data
                        }
                    }
                }*/
                deleteStatus.update {
                    res.data?.message ?: ""
                }
            }
        }
    }

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

    val companyDetail = MutableStateFlow(User())

    fun getCompanyDetail(idCompany: Int) {
        viewModelScope.launch {
            getUserUseCase.execute(idCompany).collect {
                it.data?.let { u ->
                    companyDetail.update { u }
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
    //Candidate
    val numberComment = MutableStateFlow(emptyList<Int>())
    fun getNumberCommentAllPosts() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            getNumberCommentAllPostsUseCase.execute(idConnected).collectLatest { res ->
                numberComment.update { res.data ?: emptyList() }
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
                Log.i("fklzghllzgrjljgzr", "getPostNumberLikesCompany: ${res.data}")
                numberLikesCompany.update { res.data ?: emptyList() }
            }
        }
    }

    val numberCommentCompany = MutableStateFlow(emptyList<Int>())

    fun getPostNumberCommentCompany() {
        viewModelScope.launch {
            val idConnected = sharedPreference.getInt("idUser", 0)
            getNumberCommentCompanyUseCase.execute(idConnected).collectLatest { res ->
                numberCommentCompany.update { res.data ?: emptyList() }
            }
        }
    }

    val commentsCompany = MutableStateFlow(emptyList<CommentsPost>())
    fun getPostCommentsCompany(idAnnounce: Int) {
        viewModelScope.launch {
            getCommentPostCompanyUseCase.execute(idAnnounce).collectLatest { res ->
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