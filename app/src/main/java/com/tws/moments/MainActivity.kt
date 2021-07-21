package com.tws.moments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tws.moments.adapters.MomentsAdapter
import com.tws.moments.api.MomentRepository
import com.tws.moments.databinding.ActivityMainBinding
import com.tws.moments.utils.ScreenAdaptiveUtil
import com.tws.moments.utils.dip
import com.tws.moments.viewmodels.MainViewModel
import com.tws.moments.viewmodels.MainViewModelFactory
import com.tws.moments.views.LoadMoreListener
import com.tws.moments.views.itemdecoration.MomentDividerItemDecoration

private const val TAG = "MainActivity##"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        val repository = MomentRepository()
        MainViewModelFactory(repository)
    }

    private val adapter = MomentsAdapter()

    private var reqPageIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()

        ScreenAdaptiveUtil.adaptive(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        subscribe()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(
            MomentDividerItemDecoration(
                offsets = dip(10),
                dividerColor = Color.parseColor("#dddddd"),
                startPosition = 1
            )
        )

        binding.recyclerView.adapter = this.adapter

        binding.swipeRefreshLayout.isRefreshing = true
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshTweets()
        }

        binding.recyclerView.addOnScrollListener(object : LoadMoreListener() {
            override fun onLoadMore() {
                Log.i(TAG, "load more reqPageIndex:$reqPageIndex,pageCount:${viewModel.pageCount}")
                if (reqPageIndex <= viewModel.pageCount - 1) {
                    Log.i(TAG, "internal load more")
                    viewModel.loadMoreTweets(reqPageIndex) {
                        reqPageIndex++
                        adapter.addMoreTweet(it)
                    }
                }
            }
        })
    }

    private fun subscribe() {
        viewModel.userBean.observe(this, Observer {
            adapter.userBean = it
        })

        viewModel.tweets.observe(this, Observer {
            binding.swipeRefreshLayout.isRefreshing = false
            reqPageIndex = 1
            adapter.tweets = it.toMutableList()
        })
    }

    private fun initWindow() {
        val flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = flag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}
