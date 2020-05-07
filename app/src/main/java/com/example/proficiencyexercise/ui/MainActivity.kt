package com.example.proficiencyexercise.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.proficiencyexercise.R
import com.example.proficiencyexercise.ui.fact.FactListAdapter
import com.example.proficiencyexercise.ui.fact.FactListViewModel
import java.io.IOException

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var mFactListViewModel: FactListViewModel

    private val mProgressBar: ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.progress_bar)
    }
    private val mStatusTextView: TextView by lazy {
        findViewById<TextView>(R.id.status_text_view)
    }
    private val mRecyclerView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view)
    }
    private val mSwipeRefreshLayout: SwipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
    }

    private val mFactsAdapter = FactListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareSwipeRefreshLayout()
        prepareRecyclerView()
        mRecyclerView.adapter = mFactsAdapter

        mFactListViewModel = ViewModelProviders.of(this)[FactListViewModel::class.java]
        mFactListViewModel.about.observe(this, Observer { about ->
            mProgressBar.visibility = View.GONE
            if (about.facts.isEmpty()) {
                mStatusTextView.visibility = View.VISIBLE
                mStatusTextView.setText(R.string.list_is_empty)
            } else {
                mStatusTextView.visibility = View.GONE
                mStatusTextView.text = null
                mFactsAdapter.setDataSource(about.facts)
                title = about.title
            }
        })
        mFactListViewModel.loading.observe(this, Observer {
            mSwipeRefreshLayout.isRefreshing = it == true
        })
        mFactListViewModel.error.observe(this, Observer {
            mStatusTextView.visibility = View.VISIBLE
            if (it is IOException) {
                mStatusTextView.setText(R.string.connection_error)
            } else {
                mStatusTextView.setText(R.string.list_is_empty)
            }
        })
    }

    private fun prepareSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        mSwipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun prepareRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
    }

    override fun onRefresh() {
        mFactListViewModel.loadFacts()
    }
}