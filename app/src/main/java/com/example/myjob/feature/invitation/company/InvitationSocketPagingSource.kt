package com.example.myjob.feature.invitation.company

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myjob.domain.entities.invitation.InvitationModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume

class InvitationSocketPagingSource(
    private val socket: Socket,
    private val idUser: Int
) : PagingSource<Int, InvitationModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InvitationModel> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        return try {
            val response = requestInvitationPageViaSocket(page, pageSize)
            Log.i("dlmfgtnjro", "load: ${response.data}")
            LoadResult.Page(
                data = response.data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (page < response.totalPages - 1) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun parseInvitationPage(json: JSONObject): Invitation {
        val page = json.getInt("page")
        val totalPages = json.getInt("totalPages")

        val dataArray = json.getJSONArray("data")
        val invitations = mutableListOf<InvitationModel>()

        for (i in 0 until dataArray.length()) {
            val obj = dataArray.getJSONObject(i)
            val idTo = obj.getInt("idTo")
            val idCompany = obj.getInt("idCompany")
            val date = obj.getString("date")
            val fullName = obj.getString("fullName")
            val description = obj.getString("description")
            val companyName = obj.getString("companyName")
            val message = obj.getString("message")
            val descriptionContract = obj.getString("descriptionContract")
            val disponibility = obj.getString("disponibility")
            val tgm = obj.getString("tgm")
            val salary = obj.getString("salary")
            val status = obj.getString("status")
            val reason = obj.getString("reason")
            val dateEnd = obj.getString("dateEnd")

            invitations.add(
                InvitationModel(
                    idTo = idTo,
                    idCompany = idCompany,
                    date = date,
                    fullName = fullName,
                    description = description,
                    companyName = companyName,
                    message = message,
                    descriptionContract = descriptionContract,
                    disponibility = disponibility,
                    tgm = tgm,
                    salary = salary,
                    status = status,
                    reason = reason,
                    dateEnd = dateEnd
                )
            )
        }

        return Invitation(
            data = invitations,
            page = page,
            totalPages = totalPages
        )
    }

    private suspend fun requestInvitationPageViaSocket(page: Int, size: Int): Invitation =
        suspendCancellableCoroutine { cont ->
            val listener = Emitter.Listener { args ->
                val json = args[0] as JSONObject
                val parsed = parseInvitationPage(json)
                cont.resume(parsed)
            }
            Log.i("dlmfgtnjro", "requestInvitationPageViaSocket: ${socket.id()}")
            socket.once("invitations_page", listener)
            socket.emit("get_invitations", JSONObject().apply {
                put("id", idUser)
                put("page", page)
                put("size", size)
            })
        }

    override fun getRefreshKey(state: PagingState<Int, InvitationModel>): Int? = null
}