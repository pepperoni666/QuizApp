package com.dummy.quiz_app

import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.fragment.findNavController
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.dummy.quiz_app.views.quizRaw
import kotlinx.android.synthetic.main.fragment_quizzes.*

class QuizzesFragment : BaseMvRxFragment() {

    private val viewModel: QuizzesViewModel by activityViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        loadingAnimation.isVisible = state.quizzes is Loading
        quizzesRecyclerView.withModels {
            state.quizzes()?.forEach { quiz ->
                quizRaw {
                    id(quiz.id)
                    quiz(quiz)
                    clickListener { _ ->
                        findNavController().navigate(
                            R.id.action_quizzesFragment_to_solvingFragment,
                            SolvingFragment.arg(quiz.id)
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quizzes, container, false)
    }

    override fun onStart() {
        super.onStart()
        return_top_btn.setOnClickListener {
            quizzesRecyclerView.smoothScrollToPosition(0)
            return_top_btn.visibility = View.GONE
        }
        load_more_btn.setOnClickListener {
            viewModel.loadMore()
            load_more_btn.visibility = View.GONE
        }
        quizzesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //Show "return to top" if view scrolled down
                if ((recyclerView.layoutManager as LinearLayoutManager)
                        .findFirstVisibleItemPosition() < 2
                ) {
                    return_top_btn.visibility = View.GONE
                } else {
                    return_top_btn.visibility = View.VISIBLE
                }
                //Show "load more" if view scrolled to its end
                withState(viewModel) { state ->
                    if ((recyclerView.layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition() == state.quizzes()?.count()?.minus(1)
                    ) {
                        load_more_btn.visibility = View.VISIBLE
                    } else {
                        load_more_btn.visibility = View.GONE
                    }
                }
            }
        })
    }
}
