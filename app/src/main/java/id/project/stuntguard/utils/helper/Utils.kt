package id.project.stuntguard.utils.helper

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import id.project.stuntguard.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

// Convert Image Uri to Image File :
fun uriToFile(imageUri: Uri, context: Context): File {
    val file = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(file)
    val buffer = ByteArray(1024)
    var length: Int

    while (inputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }
    outputStream.close()
    inputStream.close()

    return file
}

// to make double value have specific decimal number :
fun Double.formatDigit(digit: Int) = "%.${digit}f".format(this)

// to format Date :
@SuppressLint("SimpleDateFormat")
fun formatDate(inputDate: String): String {
    val dateAPIFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(inputDate) as Date
    val formattedDate = SimpleDateFormat("dd/MMM/yyyy").format(dateAPIFormat)

    return formattedDate.toString()
}

// to set ResultTextColor :
/*
    -> Short Documentation :
    TextView.setTextColor(setResultTextColor(context, ResultPrediction)[0])

    index 0 -> The Prediction (Stunted, etc)
    index 1 -> The Sub Prediction (Optimal Growth, etc)
*/
fun setResultTextColor(context: Context, resultPrediction: String): ArrayList<Int> {
    val predictionColor = arrayListOf<Int>()

    predictionColor.apply {
        if (resultPrediction == "Stunted" || resultPrediction == "Severely Stunted") {
            add(context.resources.getColor(R.color.soft_red, null))
            add(context.resources.getColor(R.color.orange, null))
        } else {
            add(context.resources.getColor(R.color.soft_green, null))
            add(context.resources.getColor(R.color.soft_yellow, null))
        }
    }
    return predictionColor
}