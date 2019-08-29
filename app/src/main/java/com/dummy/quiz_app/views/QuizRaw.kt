package com.dummy.quiz_app.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.dummy.quiz_app.R
import com.dummy.quiz_app.data.Quiz
import kotlinx.android.synthetic.main.quiz_raw.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class QuizRaw @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.quiz_raw, this, true)
    }

    @ModelProp
    fun setQuiz(quiz: Quiz){
        quizNameView.text = quiz.name
        quizInfoView.text = quiz.info
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}