package com.example.minitwitter.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.minitwitter.retrofit.model.response.Tweet
import com.example.minitwitter.ui.deletetweet.BottomModalTweetFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.network.Resource


class TweetViewModel(application: Application) : AndroidViewModel(application) {

    var tweetRepository: TweetRepository? = null
    var tweetList: LiveData<Resource<List<Tweet>>>? = MutableLiveData()

    var favTweetList: MutableLiveData<List<Tweet>>? = MutableLiveData()

    var userName: String? = null

    init {

        tweetRepository = TweetRepository(application)
        tweetList = tweetRepository?.getAllTweet()

        userName = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME)

    }

    fun getNewTweets(): LiveData<Resource<List<Tweet>>>? {
        return tweetRepository?.getAllTweet()
    }

    fun getFavTweets(): LiveData<List<Tweet>>?{
        var newFavList: MutableList<Tweet> = ArrayList()

        val itTweets = tweetList?.value?.data?.iterator()

        if (itTweets != null) {
            while(itTweets.hasNext()) {

                val current = itTweets.next()
                val itLikes = current.likes?.iterator()

                var enc = false

                while (itLikes!!.hasNext() && !enc) {
                    if (itLikes.next().usernameLike == userName) {
                        enc = true
                        newFavList.add(current)
                    }
                }

            }
        }

        favTweetList?.value = newFavList

        return favTweetList
    }

    fun getNewFavTweets(): LiveData<List<Tweet>>?{
        var newFavList: MutableList<Tweet> = ArrayList()

        val itTweets = tweetList?.value?.data?.iterator()

        if (itTweets != null) {
            while(itTweets.hasNext()) {

                val current = itTweets.next()
                val itLikes = current.likes?.iterator()

                var enc = false

                while (itLikes!!.hasNext() && !enc) {
                    if (itLikes.next().usernameLike == userName) {
                        enc = true
                        newFavList.add(current)
                    }
                }

            }
        }

        favTweetList?.value = newFavList

        return favTweetList
    }

    fun insertTweet(mensaje: String){
        tweetRepository!!.createTweet(mensaje)
    }

    fun likeTweet(idTweet: Int){
        tweetRepository!!.likeTweet(idTweet)
    }

    fun openDialogTweetMenu(ctx: Context, idTweet: Int){
        var dialogTweet: BottomModalTweetFragment = BottomModalTweetFragment().newInstance(idTweet)
        dialogTweet.show((ctx as AppCompatActivity).supportFragmentManager, "BottomModalTweetFragment")
    }

    fun deleteTweet(idTweet: Int){
        tweetRepository!!.deleteTweet(idTweet)
    }

}