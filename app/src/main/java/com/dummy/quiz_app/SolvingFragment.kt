package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.airbnb.mvrx.*
import com.dummy.quiz_app.app.QuizApplication
import com.dummy.quiz_app.data.Answer
import kotlinx.android.synthetic.main.fragment_solving.*

class SolvingFragment : BaseMvRxFragment() {

    private val viewModel: QuizesViewModel by activityViewModel()
    private val quizId: Long by args()
    private var current: Int = 0
    private lateinit var radioButtons: List<RadioButton>

    override fun invalidate() = withState(viewModel) { state ->
        try {
            val quiz = state.quiz(quizId) ?: throw IllegalStateException("Cannot find quiz with id $quizId")
            for (i in radioButtons)
                i.visibility = View.GONE
            answer_group.clearCheck()
            current = quiz.progress

            //if progress is greater than questions number, redirect to result
            if (current == quiz.questions.size) {
                findNavController().navigate(
                    R.id.action_solvingFragment_to_resultFragment,
                    ResultFragment.arg(quizId),
                    NavOptions.Builder()
                        .setPopUpTo(
                            R.id.quizesFragment,
                            false
                        ).build()
                )
                return@withState
            }
            progress_number.text = "$current/${quiz.questions.size}"
            progress_bar.progress = (current.toDouble() / quiz.questions.size.toDouble() * 100).toInt()
            next_btn.text =
                if (current == quiz.questions.size - 1) getString(R.string.finish) else getString(R.string.next)
            question.text = quiz.questions[current].text
            for (i in quiz.questions[current].answers.indices) {
                radioButtons[i].visibility = View.VISIBLE
                radioButtons[i].text = quiz.questions[current].answers[i].text
                quiz.questions[current].selected.let {
                    if (i == it)
                        answer_group.check(radioButtons[i].id)
                }
            }

            next_btn.isEnabled = quiz.questions[current].selected != null
            prev_btn.isEnabled = current != 0
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
        return inflater.inflate(R.layout.fragment_solving, container, false)
    }

    override fun onStart() {
        super.onStart()
        radioButtons = listOf<RadioButton>(
            answer_1, answer_2, answer_3, answer_4, answer_5
        )
        answer_group.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                withState(viewModel) { state ->
                    state.changeSelectedAnswer(quizId, current, radioButtons.indexOf(group.findViewById(checkedId)))
                }
                next_btn.isEnabled = true
            }
        }
        next_btn.setOnClickListener {
            withState(viewModel) { state ->
                state.quiz(quizId)?.questions?.let {
                    state.changeProgress(quizId, current + 1)
                    if (current != it.size - 1) {
                        invalidate()
                    } else {
                        findNavController().navigate(
                            R.id.action_solvingFragment_to_resultFragment,
                            ResultFragment.arg(quizId),
                            NavOptions.Builder()
                                .setPopUpTo(R.id.quizesFragment,
                                    false).build()
                        )
                    }
                }
            }
        }
        prev_btn.setOnClickListener {
            if (current != 0) {
                withState(viewModel) { state ->
                    state.changeProgress(quizId, current - 1)
                    invalidate()
                }
            }
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
