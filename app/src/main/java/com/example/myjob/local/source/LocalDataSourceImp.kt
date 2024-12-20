package com.example.myapplication.local.source

import com.example.myjob.local.dao.PostDAO
import javax.inject.Inject

/**
 * Implementation of [LocalDataSource] Source
 */
class LocalDataSourceImp @Inject constructor(
    private val postDAO: PostDAO
) : LocalDataSource {

    /*override suspend fun addPostItem(post: PostDataModel): Long {
        val postLocalModel = postMapper.to(post)
        return postDAO.addPostItem(post = postLocalModel)
    }

    override suspend fun getPostItem(id: Int): PostDataModel {
        val postLocalModel = postDAO.getPostItem(id = id)
        return postMapper.from(postLocalModel)
    }

    override suspend fun addPostItems(posts: List<PostDataModel>) : List<Long> {
        val postLocalList = postMapper.toList(posts)
        return postDAO.addPostItems(posts = postLocalList)
    }

    override suspend fun getPostItems(): List<PostDataModel> {
        val postLocalList = postDAO.getPostItems()
        return postMapper.fromList(postLocalList)
    }

    override suspend fun updatePostItem(post: PostDataModel): Int {
        val postLocalModel = postMapper.to(post)
        return postDAO.updatePostItem(post = postLocalModel)
    }

    override suspend fun deletePostItemById(id: Int): Int {
        return postDAO.deletePostItemById(id = id)
    }

    override suspend fun deletePostItem(post: PostDataModel): Int {
        val postLocalModel = postMapper.to(post)
        return postDAO.deletePostItem(post = postLocalModel)
    }

    override suspend fun clearCachedPostItems(): Int {
        return postDAO.clearCachedPostItems()
    }*/
}