package id.project.stuntguard.utils.component

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import id.project.stuntguard.R

class CustomInputField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    private var customHint: String? = null
    private var customErrorMessage: String? = null

    init {
        // to apply custom hint and message that declared from xml code
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomInputField,
            0,
            0
        ).apply {
            try {
                customHint = getString(R.styleable.CustomInputField_customHint)
                customErrorMessage = getString(R.styleable.CustomInputField_customErrorMessage)
            } finally {
                recycle()
            }
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    setError(customErrorMessage, null)
                } else {
                    error = null
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = customHint
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}