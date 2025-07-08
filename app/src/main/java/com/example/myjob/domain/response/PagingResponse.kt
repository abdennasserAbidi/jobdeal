package com.example.myjob.domain.response

import com.example.myjob.domain.entities.invitation.InvitationModel

data class Sort(
    val empty: Boolean = true,
    val sorted: Boolean = false,
    val unsorted: Boolean = true
)

data class Pageable(
    val pageNumber: Int = 1,
    val pageSize: Int = 10,
    val sort: Sort = Sort(),
    val offset: Int = 10,
    val paged: Boolean = true,
    val unpaged: Boolean = false
)

data class PagingResponse(
    val content: List<InvitationModel>,
    val pageable: Pageable = Pageable(),
    val last: Boolean = false,
    val totalPages: Int = 20,
    val totalElements: Int = 20,
    val first: Boolean = false,
    val size: Int = 20,
    val number: Int = 20,
    val sort: Sort = Sort(),
    val numberOfElements: Int = 20,
    val empty: Boolean = false
)