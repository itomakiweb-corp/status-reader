package com.itomakiweb.statusReader

data class UserFeeling (var userName: String,
                        var comment1: String,
                        var comment2: String,
                        var comment3: String,
                        var elapsedMilliSec: Long,
                        var issuedTime: String)