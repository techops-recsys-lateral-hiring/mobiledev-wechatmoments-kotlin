package com.tws.moments.api.entry

import com.google.gson.annotations.SerializedName

data class UserBean(
    @SerializedName("profile-image")
    var profileImage: String? = null,
    var avatar: String? = null,
    var nick: String? = null,
    var username: String? = null
)
