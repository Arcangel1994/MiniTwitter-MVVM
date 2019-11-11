package com.example.minitwitter.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.minitwitter.R
import com.example.minitwitter.adapter.MyTweetRecyclerViewAdapter
import com.example.minitwitter.data.TweetViewModel
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.model.response.Tweet
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.os.Handler


class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var adapterTweet: MyTweetRecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var tweetList: List<Tweet>? = ArrayList()

    //private var spin_spin: ProgressBar? = null

    private var viewModel: TweetViewModel? = null

    //Sin MVVM
    //var authTwitterService: AuthTwitterService? = null
    //var authTwitterClient: AuthTwitterClient? = null

    var builder: MaterialAlertDialogBuilder? = null
    var customLayout: View? = null
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(TweetViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        //authTwitterClient = AuthTwitterClient.getInstance()
        //authTwitterService = authTwitterClient!!.authTwitterService

        //spin_spin = root.findViewById<ProgressBar>(R.id.spin_spin) as ProgressBar
        //val wave = Wave()
        //wave.color = ContextCompat.getColor(activity!!.applicationContext, R.color.colorAzul);
        //spin_spin!!.setIndeterminateDrawable(wave)

        builder = MaterialAlertDialogBuilder(activity)
        customLayout = layoutInflater.inflate(R.layout.dialog_loading, null);
        builder!!.setCancelable(false)
        builder!!.setView(customLayout);
        dialog = builder!!.create()
        dialog!!.show()

        swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView

        swipeRefreshLayout!!.setColorSchemeColors(ContextCompat.getColor(activity!!, R.color.colorAzul))
        swipeRefreshLayout!!.setOnRefreshListener(this)

        recyclerView!!.setLayoutManager(GridLayoutManager(context, 1));

        adapterTweet = activity?.let { tweetList?.let { it1 -> MyTweetRecyclerViewAdapter(it1, it) } }
        recyclerView!!.setAdapter(adapterTweet)

        loadTweetData()

        return root
    }

    fun loadTweetData(){
        viewModel!!.tweetList?.observe(activity!!, object : Observer<Resource<List<Tweet>>>{
            override fun onChanged(t: Resource<List<Tweet>>?) {
                try{
                    if(t?.data?.size != 0) {
                        tweetList = t!!.data
                        tweetList?.let { adapterTweet!!.setNuevasTweet(it) }
                        //spin_spin!!.visibility = View.GONE
                        dialog!!.dismiss()
                        /*
                        Handler().postDelayed(Runnable {
                            dialog!!.dismiss()
                        }, 4000)
                        */
                    }
                }catch (e: Exception){

                }
            }

        })
    }

    fun loadNewTweetData(){
        viewModel!!.getNewTweets()?.observe(activity!!, object : Observer<Resource<List<Tweet>>>{
            override fun onChanged(t: Resource<List<Tweet>>?) {
                try {
                    tweetList = t?.data
                    tweetList?.let { adapterTweet!!.setNuevasTweet(it) }
                    swipeRefreshLayout!!.isRefreshing = false;
                    viewModel!!.getNewTweets()?.removeObserver(this);
                }catch (e: Exception){

                }
            }

        })
    }

    override fun onRefresh() {

        swipeRefreshLayout!!.isRefreshing = true;
        loadNewTweetData()

    }

}