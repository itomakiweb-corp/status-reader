package com.itomakiweb.android.statusReader

import org.junit.Test
import org.junit.Assert.*

class RankingTest {
    private val wordMap = mapOf("a" to 2, "b" to 2, "c" to 3, "d" to 1, "e" to 1)
    private val speed = 3

    private val ranking = Ranking(wordMap, speed)


    @Test
    fun getScore01() {
        val expected = 0
        val actual = ranking.getScore()
        assertEquals(expected, actual)
        println(wordMap)
    }

//いやん

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
    fun getSpeedScore01() {
        val speedScoreTest01 = Ranking(wordMap,37)
        val expected = 6
        val actual = speedScoreTest01.getSpeedScore(10)
        // 適切な引数の取り方がわからなかったので仮で10を代入しました。

        assertEquals(expected, actual)
    }

    @Test
    fun getSpeedScore02(){
        val speedScoreTest02 = Ranking(wordMap,4)
        val expected = 22
        val actual = speedScoreTest02.getSpeedScore(10)
        assertEquals(expected,actual)
        // テストパターンを複数用意する形としてはこれで良いのでしょうか？
    }

    @Test
    fun getSpeedScore03() {
        val speedScoreTest03 = Ranking(wordMap,3)
        val expected = 0
        val actual = speedScoreTest03.getSpeedScore(10)
        assertEquals(expected, actual)
    }

    @Test
    fun getPenaltyScore() {
    }
}