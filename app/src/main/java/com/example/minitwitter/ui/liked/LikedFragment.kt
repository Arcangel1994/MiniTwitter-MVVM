package com.example.minitwitter.ui.liked


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.minitwitter.DashboardActivity

import com.example.minitwitter.R
import com.example.minitwitter.adapter.MyTweetRecyclerViewAdapter
import com.example.minitwitter.data.TweetViewModel
import com.example.minitwitter.retrofit.model.response.Tweet
import com.github.ybq.android.spinkit.style.Wave

class LikedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var adapterTweet: MyTweetRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var favTweetList: List<Tweet>? = ArrayList()

    private var spin_spin: ProgressBar? = null

    private var viewModel: TweetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(TweetViewModel::class.java)
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_liked, container, false)

        spin_spin = root.findViewById<ProgressBar>(R.id.spin_spin) as ProgressBar

        val wave = Wave()
        wave.color = ContextCompat.getColor(activity!!.applicationContext, R.color.colorAzul);
        spin_spin!!.setIndeterminateDrawable(wave)

        swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

        swipeRefreshLayout!!.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.colorAzul))
        swipeRefreshLayout!!.setOnRefreshListener(this)

        recyclerView!!.setLayoutManager(LinearLayoutManager(context));

        adapterTweet = activity?.let { favTweetList?.let { it1 -> MyTweetRecyclerViewAdapter(it1, it) } }
        recyclerView!!.setAdapter(adapterTweet)

        loadFavTweetData()

        return root
    }

    fun loadFavTweetData(){
        viewModel!!.getFavTweets()?.observe(activity!!, object : Observer<List<Tweet>> {
            override fun onChanged(t: List<Tweet>?) {
                favTweetList = t
                favTweetList?.let { adapterTweet!!.setNuevasTweet(it) }
                spin_spin!!.visibility = View.GONE
            }

        })
    }

    fun loadFavNewTweetData(){
        viewModel!!.getNewFavTweets()?.observe(activity!!, object : Observer<List<Tweet>> {
            override fun onChanged(t: List<Tweet>?) {
                favTweetList = t
                favTweetList?.let { adapterTweet!!.setNuevasTweet(it) }
                swipeRefreshLayout!!.isRefreshing = false;
                viewModel!!.getNewFavTweets()?.removeObserver(this)
            }

        })
    }

    override fun onRefresh() {

        swipeRefreshLayout!!.isRefreshing = true;
        loadFavNewTweetData()

    }

}
