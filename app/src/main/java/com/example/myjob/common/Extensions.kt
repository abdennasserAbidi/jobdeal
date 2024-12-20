package com.example.myjob.common

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject.NULL
import kotlin.coroutines.coroutineContext

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}

public suspend fun <T> Flow<T>.takeLast(size: Int): T {
    var result: Any? = NULL
    /*
    condition: Boolean? = null, size: Int, action: suspend (value: T) -> Int)
    take(size)
    takeWhile { condition == true }*/
    take(size)
    collect {
        result = it
    }
    if (result === NULL) throw NoSuchElementException("Expected at least one element")
    return result as T
}

fun <T> Flow<T>.takeUntilSignal(signal: Flow<Unit>): Flow<T> = flow {
    try {
        coroutineScope {
            launch {
                signal.take(1).collect()
                println("signalled")
                this@coroutineScope.cancel()
            }

            collect {
                emit(it)
            }
        }

    } catch (e: CancellationException) {
        //ignore
    }
}

public fun <T, R> Flow<T>.takeUntil(notifier: Flow<R>): Flow<T> = flow {
    try {
        coroutineScope {
            val job = launch(start = CoroutineStart.UNDISPATCHED) {
                notifier.take(1).collect()
                throw CancellationException()
            }

            collect { emit(it) }
            job.cancel()
        }
    } catch (e: CancellationException) {
       // e.checkOwnership(this@flow)
    }
}

suspend inline fun <T> Flow<T>.safeCollect(crossinline action: suspend (T) -> Unit) {
    collect {
        coroutineContext.ensureActive()
        action(it)
    }
}


inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}

inline fun AppCompatActivity.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}

inline fun <T> Flow<T>.collectLA(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collect { onCollect(it) } } }

inline fun <T> Flow<T>.collectLatestLA(
    owner: LifecycleOwner,
    crossinline onCollect: suspend (T) -> Unit
) = owner.lifecycleScope.launch { owner.repeatOnLifecycle(Lifecycle.State.STARTED) { collectLatest { onCollect(it) } } }
