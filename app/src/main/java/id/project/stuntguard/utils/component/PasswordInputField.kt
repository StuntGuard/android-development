package id.project.stuntguard.utils.component

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import id.project.stuntguard.R

class PasswordInputField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // DO Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Do Nothing
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.password_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun validatePassword(password: String) {
        // Condition to consider as Valid Password (Has Uppercase,Symbol, Number and the length min 8) :
        val hasUpperCase = Regex(".*[A-Z].*")
        val hasLowerCase = Regex(".*[a-z].*")
        val hasSymbol = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")
        val hasNumber = Regex(".*[0-9].*")

        when {
            password.length < 8 -> {
                setError(context.getString(R.string.password_error_message), null)
            }
            !hasUpperCase.containsMatchIn(password) -> {
                setError("Password must be contain capitalize character", null)
            }
            !hasLowerCase.containsMatchIn(password) -> {
                setError("Password must be contain regular character", null)
            }
            !hasSymbol.containsMatchIn(password) -> {
                setError("Password must be contain symbol", null)
            }
            !hasNumber.containsMatchIn(password) -> {
                setError("Password must be contain number", null)
            }
            else -> {
                error = null
            }
        }
    }
}