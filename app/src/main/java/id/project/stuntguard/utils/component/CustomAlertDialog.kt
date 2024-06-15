package id.project.stuntguard.utils.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import id.project.stuntguard.databinding.CustomAlertDialogBinding

class CustomAlertDialog(private val context: Context) {
    private lateinit var binding: CustomAlertDialogBinding
    private lateinit var alertDialog: AlertDialog

    fun create(title: String, message: String, onPositiveButtonClick: () -> Unit) {
        binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(context))

        // Set Alert Dialog Component :
        binding.title.text = title
        binding.message.text = message
        binding.positiveButton.setOnClickListener {
            onPositiveButtonClick()
            alertDialog.dismiss()
        }

        // Set AlertDialog Builder :
        alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show() {
        alertDialog.show()
    }
}