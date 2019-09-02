package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
            current = quiz.progress
            progress_number.text = "$current/${quiz.questions.size}"
            question.text = quiz.questions[current].text
            for(i in quiz.questions[current].answers.indices){
                radioButtons[i].visibility = View.VISIBLE
                radioButtons[i].text = quiz.questions[current].answers[i].text
            }

            if(quiz.questions[current].selected != null){
                next_btn.isEnabled = true
                for(i in radioButtons){
                    i.isChecked = quiz.questions[current].selected == radioButtons.indexOf(i)
                }
            }
            else
                next_btn.isEnabled = false
            prev_btn.isEnabled = current != 0
        }catch (e: IllegalStateException) {
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
            answer_1,answer_2,answer_3,answer_4,answer_5
        )
        answer_group.setOnCheckedChangeListener { group, checkedId ->
            withState(viewModel) { state ->
                state.quiz(quizId)?.let{
                    it.questions[current].selected = radioButtons.indexOf(group.findViewById(checkedId))
                }
            }
            next_btn.isEnabled = true
        }
        next_btn.setOnClickListener {
            withState(viewModel) { state ->
                state.quiz(quizId)?.questions?.let {
                    if(current!= it.size) {
                        state.changeProgress(quizId, current + 1)
                        invalidate()
                    }
                }
            }
        }
        prev_btn.setOnClickListener {
            if(current!=0){
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
