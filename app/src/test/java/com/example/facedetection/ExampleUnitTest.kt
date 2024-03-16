package com.example.facedetection

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun emptyCalc(){
        val emb1 : FloatArray? = null
        val emb2 : FloatArray? = null
        val result = calculateCosineSimilarity(emb1,emb2)

        assertEquals(0f,result)
    }

    @Test
    fun semiEmptyCalc(){
        val emb1: FloatArray = floatArrayOf(0.123465F, 1.1516F, 2.23163F)
        val emb2: FloatArray = floatArrayOf(0.123465F, 1.1516F, 2.23163F)

        val result = calculateCosineSimilarity(emb1,emb2)

        assertEquals(0.99999994f,result)
    }

    @Test
    fun diffSizeCalc(){
        val emb1: FloatArray = floatArrayOf(01321f,02.15156f,12f)
        val emb2: FloatArray = floatArrayOf(01321f,02.15156f,12f,.3516f,.2565f)
        val result = calculateCosineSimilarity(emb1,emb2)

        assertEquals(1.0f,result)

    }
}