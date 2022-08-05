package com.example.flows

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CallbackFlowRnDTest {

    lateinit var callbackFlowRnD: CallbackFlowRnD

    @Before
    fun setup() {
        callbackFlowRnD = CallbackFlowRnD()
    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun test() = runTest {
//
//        val job: Job = launch {
//            callbackFlowRnD.myCallbackFlow(0).collect {
//                Assert.assertEquals(0, it)
//            }
//        }
//        advanceUntilIdle()
//        job.cancel()
//
//    }


    @Test
    fun `an example guide to test callback flows`() = runTest {

        val job: Job = launch { callbackFlowRnD.firstCollect() }
        advanceUntilIdle()

        callbackFlowRnD.event.produce(10)
        advanceUntilIdle()

        job.cancel()

        Assert.assertEquals(10, callbackFlowRnD.state1)

    }


    @Test
    fun test() = runTest {
        val job1: Job = launch {
            callbackFlowRnD.firstCollect()
        }

        val job2: Job = launch {
            callbackFlowRnD.secondCollect()
        }

        advanceUntilIdle()
        callbackFlowRnD.event.produce(10)
        advanceUntilIdle()
        advanceUntilIdle()
        job1.cancel()
        job2.cancel()

        Assert.assertEquals(10, callbackFlowRnD.state1 )
        Assert.assertEquals(10, callbackFlowRnD.state2 )

    }

}