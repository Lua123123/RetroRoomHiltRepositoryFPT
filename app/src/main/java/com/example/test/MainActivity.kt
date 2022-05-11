package com.example.test

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.ListTopicAdapter
import com.example.test.databaseTopic.TopicDatabase
import com.example.test.hilt.RetroRepository
import com.example.test.viewmodel.WordViewModel
import com.example.test.model.Success
import dagger.hilt.android.AndroidEntryPoint

//////
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var topicRecyclerView: RecyclerView
    private var addTopicFab: ConstraintLayout? = null
    private lateinit var listTopicAdapter: ListTopicAdapter
    private var context: Context = this@MainActivity
    private var retroRepository: RetroRepository? = null
    private var postsList: MutableList<Success> = ArrayList()
    private lateinit var layoutManager: LinearLayoutManager

    //    private val topicViewModel: WordViewModel by viewModels()
    private var viewModel: WordViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topicRecyclerView = findViewById(R.id.topicRecyclerView)
        addTopicFab = findViewById(R.id.addTopicFab)
        addTopicFab?.setOnClickListener {
//            viewModel?.openDialogInsertTopic(Gravity.CENTER, context, postsList)
            openDialogInsertTopic(Gravity.CENTER, context, postsList)
        }

        initViewModel()
        initMainViewModel()
    }

    private fun initViewModel() {
        topicRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            val decoration =
                DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            listTopicAdapter = ListTopicAdapter()
            adapter = listTopicAdapter
        }
    }

    private fun initMainViewModel() {
        val viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        viewModel.getAllTopic().observe(this, Observer<List<Success>> {
            listTopicAdapter.setData(it)
            listTopicAdapter.notifyDataSetChanged()
        })

        if (haveNetwork()) {
            val toast: Toast = Toast.makeText(this@MainActivity, "CONNECT INTERNET SUCCESS", Toast.LENGTH_LONG)
            viewModel.customToast(toast)
            viewModel.makeApiCall()
        }
        if (!haveNetwork()) {
            val toast: Toast = Toast.makeText(this@MainActivity, "DON'T HAVE INTERNET", Toast.LENGTH_LONG)
            viewModel.customToast(toast)
        }
    }

    private fun haveNetwork(): Boolean {
        var have_WIFI = false
        var have_MobileData = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName.equals("WIFI", ignoreCase = true)) if (info.isConnected) have_WIFI =
                true
            if (info.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (info.isConnected) have_MobileData = true
        }
        return have_WIFI || have_MobileData
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
        viewModel?.customToast(toast)
        edtName.setText("")
        edtAmount.setText("")

//        if (postsList != null) {
//            mutableLiveData.postValue(postsList!!)
//        }
    }
}