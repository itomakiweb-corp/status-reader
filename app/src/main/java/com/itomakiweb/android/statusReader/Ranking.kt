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
        val speedMultipleScore: Double = when{
            (speed >= 37) -> 0.64 * moderateScore
            (speed == 36) -> 1.00 * moderateScore
            (speed == 35) -> 1.08 * moderateScore
            (speed == 34) -> 1.15 * moderateScore
            (speed == 33) -> 1.21 * moderateScore
            (speed == 32) -> 1.26 * moderateScore
            (speed == 31) -> 1.30 * moderateScore
            (speed == 30) -> 1.33 * moderateScore
            (speed == 29) -> 1.35 * moderateScore
            (speed == 28) -> 1.36 * moderateScore
            (speed == 27) -> 1.37 * moderateScore + 1.0
            (speed == 26) -> 1.38 * moderateScore + 1.1
            (speed == 25) -> 1.39 * moderateScore + 1.2
            (speed == 24) -> 1.40 * moderateScore + 1.3
            (speed == 23) -> 1.41 * moderateScore + 1.4
            (speed == 22) -> 1.42 * moderateScore + 1.6
            (speed == 21) -> 1.43 * moderateScore + 1.8
            (speed == 20) -> 1.44 * moderateScore + 2.0
            (speed == 19) -> 1.45 * moderateScore + 2.2
            (speed == 18) -> 1.46 * moderateScore + 3.0
            (speed == 17) -> 1.47 * moderateScore + 3.3
            (speed == 16) -> 1.48 * moderateScore + 3.6
            (speed == 15) -> 1.49 * moderateScore + 3.9
            (speed == 14) -> 1.50 * moderateScore + 4.2
            (speed == 13) -> 1.51 * moderateScore + 4.5
            (speed == 12) -> 1.52 * moderateScore + 4.8
            (speed == 11) -> 1.53 * moderateScore + 5.1
            (speed == 10) -> 1.54 * moderateScore + 5.4
            (speed == 9)  -> 1.55 * moderateScore + 5.5
            (speed == 8)  -> 1.56 * moderateScore + 5.6
            (speed == 7)  -> 1.57 * moderateScore + 5.7
            (speed == 6)  -> 1.58 * moderateScore + 5.8
            (speed == 5)  -> 1.59 * moderateScore + 5.9
            (speed == 4)  -> 1.60 * moderateScore + 6.0

            else  -> 0.0
            // とりあえず全部入力してみました。
        }
        return speedMultipleScore.toInt()
    }
    fun getPenaltyScore(speedScore: Int): Int {
        return speedScore
    }
}
