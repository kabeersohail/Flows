package com.example.flows

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SuspendingFunctionsTest {

    lateinit var suspendingFunctions: SuspendingFunctions

    @Before
    fun setup() {
        suspendingFunctions = SuspendingFunctions()
    }

    @Test
    fun `response of fetchData() must be Hello World`() = runTest {
        val response = suspendingFunctions.fetchData()
        Assert.assertEquals("Hello World", response)
    }

}