package com.example.o4get.myapplication32

import org.junit.Test
import org.junit.Assert.*

class RankingTest {
    @Test
    fun getResults() {
        val ranking = Ranking()
        val results = ranking.getResults()
        assertEquals(1, results)
    }
}