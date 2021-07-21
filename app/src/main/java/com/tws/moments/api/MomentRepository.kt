package com.tws.moments.api

import com.tws.moments.api.entry.TweetBean
import com.tws.moments.api.entry.UserBean

class MomentRepository {
    suspend fun fetchUser(): UserBean {
        return reqApi.user("jsmith")
    }

    suspend fun fetchTweets(): List<TweetBean> {
        return reqApi.tweets("jsmith")
    }
}
