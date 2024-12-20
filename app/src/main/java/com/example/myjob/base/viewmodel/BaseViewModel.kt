package com.example.myjob.base.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Base class for [ViewModel] instances
 */
abstract class BaseViewModel : ViewModel() {

    protected var disposable: CompositeDisposable = CompositeDisposable()
    private val lifecycleSubject:
            BehaviorSubject<ViewModelLifeCycleEvent> = BehaviorSubject.create()

}