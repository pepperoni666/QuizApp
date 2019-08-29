package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.dummy.quiz_app.views.quizRaw
import kotlinx.android.synthetic.main.fragment_quizes.*

class QuizesFragment : BaseMvRxFragment() {

    private val viewModel: QuizesViewModel by activityViewModel()

    override fun invalidate()  = withState(viewModel) { state ->
        loadingAnimation.isVisible = state.quizes is Loading

        quizesRecyclerView.withModels {
            state.quizes()?.forEach { quiz ->
                quizRaw {
                    id(quiz.id)
                    quiz(quiz)
                    //clickListener{ _ -> findNavController().navigate(R.id.)}
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quizes, container, false)
    }
}
