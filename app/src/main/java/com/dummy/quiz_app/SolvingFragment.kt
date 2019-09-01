package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args

class SolvingFragment : BaseMvRxFragment() {

    private val viewModel: QuizesViewModel by activityViewModel()
    private val dogId: Long by args()

    override fun invalidate() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solving, container, false)
    }

    companion object {
        fun arg(dogId: Long): Bundle {
            val args = Bundle()
            args.putLong(MvRx.KEY_ARG, dogId)
            return args
        }
    }
}
