package com.dummy.quiz_app.app

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MvRxState

open class MvRxViewModel <S: MvRxState>(state: S): BaseMvRxViewModel<S>(state, debugMode = BuildConfig.DEBUG)