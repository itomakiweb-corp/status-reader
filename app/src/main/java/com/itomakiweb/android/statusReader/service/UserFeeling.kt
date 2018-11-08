package com.itomakiweb.android.statusReader.service

data class UserFeeling (
        var userName: String,
        var userId: String,
        var comment1: String,
        var comment2: String,
        var comment3: String,
        var elapsedMilliSec: Long,
        var issuedTime: String
)

data class UserData (
        var id: Long = -1,
        var userId: String,
        var authType: String,
        var userName: String
)