package com.itomakiweb.android.statusReader

import org.junit.Test
import org.junit.Assert.*

class RankingTest {
    private val wordMap = mapOf("a" to 2, "b" to 2, "c" to 3, "d" to 1, "e" to 1)
    private val speed = 12

    private val ranking = Ranking(wordMap, speed)


    @Test
    fun getScore01() {
        val expected = 36
        val actual = ranking.getScore()
        assertEquals(expected, actual)
        println(wordMap)
    }

    @Test
    fun getModerateScore() {
    }

    @Test
    fun isModerateWord() {
    }

    @Test
    fun isSingleWord01() {
        val expected = true
        val actual = ranking.isSingleWord()
        assertEquals(expected, actual)
    }

    @Test
    fun isSingleWord02() {
    }

    @Test
    fun isMostWord() {
    }

    @Test
    fun getModerateDetailScore() {
    }


    @Test
    fun getSpeedScore() {
    }


    @Test
    fun getPenaltyScore() {
    }
}