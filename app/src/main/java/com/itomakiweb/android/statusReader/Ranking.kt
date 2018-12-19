package com.itomakiweb.android.statusReader

class Ranking(private val wordMap: Map<String, Int>, private val speed: Int) {
    val SINGLE_WORD_SCORE = 3
    val MOST_WORD_SCORE = 9
    val MODERATE_WORD_SCORE = 27


    fun getScore(): Int {
        val moderateScore = getModerateScore()
        val speedScore = getSpeedScore(moderateScore)
        val penaltyScore = getPenaltyScore(speedScore)

        return penaltyScore
    }

    fun getModerateScore(): Int {
        if (isSingleWord()) {
            return SINGLE_WORD_SCORE
        } else if (isMostWord()) {
            return MOST_WORD_SCORE
        } else {
            return getModerateDetailScore()
        }
    }

    fun isModerateWord(): Boolean {
        if (isSingleWord()) {
            return false
        } else return !isMostWord()
    }

    fun isSingleWord(): Boolean {
        return true
    }

    fun isMostWord(): Boolean {
        return true
    }

    fun getModerateDetailScore(): Int {
        return 1
    }


    fun getSpeedScore(moderateScore: Int): Int {
        if(speed<=3) return 0
        else return moderateScore * this.speed
    }   //3秒以内で点数0になる部分だけ。速度に応じた倍率で算出するにはここも変える必要ありそう。
    

    fun getPenaltyScore(speedScore: Int): Int {
        return speedScore
    }
}
