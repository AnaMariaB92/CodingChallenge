package com.example.proficiencyexercise.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.proficiencyexercise.R
import com.example.proficiencyexercise.ui.fact.FactListAdapter
import com.example.proficiencyexercise.ui.fact.FactListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mFactListViewModel: FactListViewModel

    private val mFactsAdapter = FactListAdapter()
    private lateinit var errorSnackBar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareSwipeRefreshLayout()
        prepareRecyclerView()
        recycler_view.adapter = mFactsAdapter

        mFactListViewModel = ViewModelProviders.of(this)[FactListViewModel::class.java]
        mFactListViewModel.about.observe(this, Observer { about ->
            progress_bar.visibility = View.GONE
            if (about.facts.isEmpty()) {
                showError(R.string.list_is_empty)
            } else {
                if (::errorSnackBar.isInitialized) hideError()
                mFactsAdapter.setDataSource(about.facts)
                title = about.title
            }
        })
        mFactListViewModel.loading.observe(this, Observer {
            swipe_layout.isRefreshing = it == true
        })
        mFactListViewModel.error.observe(this, Observer {
            if (it is IOException) {
                showError(R.string.connection_error)
            } else {
                showError(R.string.list_is_empty)
            }
        })
    }

    private fun prepareSwipeRefreshLayout() {
        swipe_layout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        swipe_layout.setOnRefreshListener(this)
    }

    private fun prepareRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackBar = Snackbar.make(main_content, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackBar.setAction("Retry", View.OnClickListener { onRefresh() })
        errorSnackBar.show()
    }

    private fun hideError() {
        errorSnackBar.dismiss()
    }

    override fun onRefresh() {
        mFactListViewModel.loadFacts()
    }
}