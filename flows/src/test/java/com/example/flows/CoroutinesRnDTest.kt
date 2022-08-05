package com.example.flows

import io.mockk.coVerifyOrder
import io.mockk.spyk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesRnDTest {

    lateinit var coroutinesRnD: CoroutinesRnD

    @Before
    fun setup() {
        coroutinesRnD = spyk(CoroutinesRnD())
    }

    @Test
    fun `test if coroutines are executing sequentially`() = runTest {

        coroutinesRnD.runInSequence()

        coVerifyOrder {
            coroutinesRnD.firstMethod()
            coroutinesRnD.secondMethod()
            coroutinesRnD.thirdMethod()
            coroutinesRnD.fourthMethod()
        }
    }
}