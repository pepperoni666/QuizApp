package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import com.dummy.quiz_app.app.QuizApplication
import com.dummy.quiz_app.data.Question
import com.dummy.quiz_app.data.Rate
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : BaseMvRxFragment() {

    private val viewModel: QuizesViewModel by activityViewModel()
    private val quizId: Long by args()

    override fun invalidate() = withState(viewModel) { state ->
        try {
            val quiz = state.quiz(quizId) ?: throw IllegalStateException("Cannot find quiz with id $quizId")
            var score = 0
            for(i: Question in quiz.questions){
                if (i.answers[i.selected!!].isCorrect)
                    score += 1
            }
            score = (score.toDouble() / quiz.questions.size.toDouble() * 100).toInt()
            var rate = ""
            for (i: Rate in quiz.rates){
                if(score <= i.to && score >= i.from)
                    rate = i.content
            }
            score_sign.text = "$score%"
            rate_sign.text = rate
        } catch (e: IllegalStateException) {
            e.message?.let {
                QuizApplication.showToast(it, true)
            }
        }
        return@withState
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onStart() {
        super.onStart()
        main_page_btn.setOnClickListener {
            findNavController().popBackStack()
        }
        try_again_btn.setOnClickListener {
            withState(viewModel) { state ->
                state.clearProgress(quizId)
            }
            findNavController().navigate(
                R.id.action_resultFragment_to_solvingFragment3,
                SolvingFragment.arg(quizId),
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.quizesFragment,
                        false
                    ).build()
            )
        }
    }

    companion object {
        fun arg(quizId: Long): Bundle {
            val args = Bundle()
            args.putLong(MvRx.KEY_ARG, quizId)
            return args
        }
    }
}
