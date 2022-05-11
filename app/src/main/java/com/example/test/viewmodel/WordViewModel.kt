package com.example.test.viewmodel

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.test.R
import com.example.test.databaseTopic.TopicDatabase
import com.example.test.hilt.RetroRepository
import com.example.test.model.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(private val retroRepository: RetroRepository) : ViewModel() {

    fun getAllTopic() : LiveData<List<Success>> {
        return retroRepository.getAllRecords()
    }

    fun makeApiCall() {
        retroRepository.makeApiCall(1)
    }

    fun customToast(toast: Toast) {
        val toastView = toast.view
        val toastMessage = toastView!!.findViewById<View>(android.R.id.message) as TextView
        toastMessage.textSize = 13f
        toastMessage.setTextColor(Color.YELLOW)
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        toastMessage.gravity = Gravity.CENTER
        toastMessage.compoundDrawablePadding = 4
        toastView.setBackgroundColor(Color.BLACK)
        toastView.setBackgroundResource(R.drawable.bg_toast)
        toast.show()
    }

    fun openDialogInsertTopic(gravity: Int, context: Context, postsList: MutableList<Success>?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)
        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes
        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true)
        } else {
            dialog.setCancelable(false)
        }
        val edtName = dialog.findViewById<EditText>(R.id.edt_name)
        val edtAmount = dialog.findViewById<EditText>(R.id.edt_amount)
        val btnCancel: ConstraintLayout = dialog.findViewById(R.id.btn_cancel)
        val btnConfirm: ConstraintLayout = dialog.findViewById(R.id.btn_confirm)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            val name = edtName.text.toString().trim { it <= ' ' }
            val amount = edtAmount.text.toString().trim { it <= ' ' }
            addTopic(name, amount, edtName, edtAmount, context, postsList)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun addTopic(name: String, amount: String, edtName: EditText, edtAmount: EditText, context: Context, postsList: MutableList<Success>?) {
        if (name.isEmpty() || amount.isEmpty()) {
            return
        }
        var success: Success = Success(name, amount)
        TopicDatabase.getAppDBInstance(context).topicDAO().insertTopic(success)
        var toast: Toast =
            Toast.makeText(context, "INSERT SUCCESS", Toast.LENGTH_LONG)
        customToast(toast)
        edtName.setText("")
        edtAmount.setText("")

//        if (postsList != null) {
//            mutableLiveData.postValue(postsList!!)
//        }
    }
}