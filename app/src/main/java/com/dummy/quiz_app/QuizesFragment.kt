package com.dummy.quiz_app

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                    clickListener{ _ -> findNavController().navigate(R.id.action_quizesFragment_to_solvingFragment, SolvingFragment.arg(quiz.id))}
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

    override fun onStart() {
        super.onStart()
        return_top_btn.setOnClickListener {
            quizesRecyclerView.smoothScrollToPosition(0)
            return_top_btn.visibility = View.GONE
        }
        quizesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition() < 2) {
                    return_top_btn.visibility = View.GONE
                }
                else{
                    return_top_btn.visibility = View.VISIBLE
                }
            }
        })
    }
}
