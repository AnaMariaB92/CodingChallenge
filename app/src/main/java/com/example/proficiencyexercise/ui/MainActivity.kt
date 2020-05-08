package com.example.proficiencyexercise.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.proficiencyexercise.R
import com.example.proficiencyexercise.ui.fact.FactListAdapter
import com.example.proficiencyexercise.ui.fact.FactListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mFactListViewModel: FactListViewModel

    private val mFactsAdapter = FactListAdapter()

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
                status_text_view.visibility = View.VISIBLE
                status_text_view.setText(R.string.list_is_empty)
            } else {
                status_text_view.visibility = View.GONE
                status_text_view.text = null
                mFactsAdapter.setDataSource(about.facts)
                title = about.title
            }
        })
        mFactListViewModel.loading.observe(this, Observer {
            swipe_layout.isRefreshing = it == true
        })
        mFactListViewModel.error.observe(this, Observer {
            status_text_view.visibility = View.VISIBLE
            if (it is IOException) {
                status_text_view.setText(R.string.connection_error)
            } else {
                status_text_view.setText(R.string.list_is_empty)
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

    override fun onRefresh() {
        mFactListViewModel.loadFacts()
    }
}