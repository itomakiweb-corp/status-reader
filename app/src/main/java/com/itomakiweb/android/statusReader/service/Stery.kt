package com.itomakiweb.android.statusReader.service

import java.io.Serializable

interface  Base{
    var createdTime: String
    var updatedTime: String
}

data class Stery(
        override var createdTime: String = "",
        override var updatedTime: String = "",
        var userName: String = "",
        var userId: String = "",
        var comment1: String = "",
        var comment2: String = "",
        var comment3: String = "",
        var elapsedMilliSec: Long = -1
):Base


data class Score (
        var userName: String,
        var userId: String,
        var currentScore: Int,
        var totalScore: Int,
        var rank: Int
)

data class UserData (
        var id: Long = -1,
        var userId: String,
        var authType: String,
        var userName: String
)

data class Seed (
        override var createdTime: String = "",
        override var updatedTime: String = "",
        var id: Long = -1,
        var seedType: String = "",
        var seedTitle: String = "",
        var seedUrl: String = "",
        var keySteries: String = "",
        var inputStartTime: Int = -1,
        var inputEndTime: Int = -1,
        var uploadUserId: String = "",
        var uploadUserName: String = ""
):Base, Serializable