package com.example.minitwitter.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.MyApp
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.dao.TweetDao
import com.example.minitwitter.db.MiniTwitterRoomDatabase
import com.example.minitwitter.network.NetworkBoundResource
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.AuthTwitterClient
import com.example.minitwitter.retrofit.model.request.RequestCreateTweet
import com.example.minitwitter.retrofit.model.response.Tweet
import com.example.minitwitter.retrofit.model.response.TweetDeleted
import com.example.minitwitter.retrofit.service.AuthTwitterService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.hasnat.sweettoast.SweetToast

class TweetRepository(application: Application) {

    var authTwitterService: AuthTwitterService? = null
    var authTwitterClient: AuthTwitterClient? = null
    /*
    var tweetList: MutableLiveData<List<Tweet>>? = MutableLiveData()
    var favTweetList: MutableLiveData<List<Tweet>>? = MutableLiveData()
    */

    private val tweetDao: TweetDao

    var favTweetList: MutableLiveData<List<Tweet>>? = MutableLiveData()
    var userName: String? = null

    init {

        authTwitterClient = AuthTwitterClient.getInstance()
        authTwitterService = authTwitterClient!!.authTwitterService

        /*
        tweetList = getAllTweet()
        */
        userName = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME)

        var db = MiniTwitterRoomDatabase.getDatabase(application)

        tweetDao = db!!.tweetDao()

    }

    fun getAllTweet(): LiveData<Resource<List<Tweet>>>{

        return object : NetworkBoundResource<List<Tweet>, List<Tweet>>(){
            override fun saveCallResult(item: List<Tweet>) {
                tweetDao.insert(item)
            }

            override fun loadFromDb(): LiveData<List<Tweet>> {
                return tweetDao.getTweet()
            }

            override fun createCall(): Call<List<Tweet>> {
                return authTwitterService!!.getAllTweets()
            }

        }.asLiveData

    }

    fun createTweet(mensaje: String){

        var requestCreateTweet: RequestCreateTweet = RequestCreateTweet(mensaje);

        var call: Call<Tweet> = authTwitterService!!.createTweet(requestCreateTweet);

        call.enqueue(object : Callback<Tweet> {
            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                SweetToast.error(MyApp.getContext(), "Problemas de conexion, Intentelo de nuevo")
            }

            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if(response.isSuccessful){

                    response.body()?.let { insertOne(it) }

                }else{
                    SweetToast.info(MyApp.getContext(), "No se pudo realizar el nuevo tweet, trate de nuevo")
                }
            }
        })

    }

    fun likeTweet(idTweet: Int){

        var call: Call<Tweet> = authTwitterService!!.likeTweet(idTweet);

        call.enqueue(object : Callback<Tweet> {
            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                SweetToast.error(MyApp.getContext(), "Problemas de conexion, Intentelo de nuevo")
            }

            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if(response.isSuccessful){

                    response.body()?.let { update(it) }

                }else{
                    SweetToast.info(MyApp.getContext(), "No se pudo realizar tu like, trate de nuevo")
                }
            }
        })

    }

    fun deleteTweet(idTweet: Int) {
        var call : Call<TweetDeleted> = authTwitterService!!.deleteTweet(idTweet);

        call.enqueue(object: Callback<TweetDeleted>{
            override fun onFailure(call: Call<TweetDeleted>, t: Throwable) {
                SweetToast.error(MyApp.getContext(), "Problemas de conexion, Intentelo de nuevo")
            }

            override fun onResponse(call: Call<TweetDeleted>, response: Response<TweetDeleted>) {
                if(response.isSuccessful){

                    delete(idTweet)

                }else{
                    SweetToast.info(MyApp.getContext(), "No se pudo realizar la eliminacion de ese tweet, trate de nuevo")
                }
            }

        })

    }

    //Room
    fun insertOne(tweet: Tweet){
        insertAsyncTask(tweetDao).execute(tweet)
    }

    private class insertAsyncTask internal constructor(private val tweetDatoAsyncTask: TweetDao) :
        AsyncTask<Tweet, Void, Void>() {

        override fun doInBackground(vararg tweetEntities: Tweet): Void? {

            tweetDatoAsyncTask.insertOne(tweetEntities[0])

            return null

        }

    }

    fun update(tweet: Tweet){
        updateAsyncTask(tweetDao).execute(tweet)
    }

    private class updateAsyncTask internal constructor(private val tweetDatoAsyncTask: TweetDao) :
        AsyncTask<Tweet, Void, Void>() {

        override fun doInBackground(vararg tweetEntities: Tweet): Void? {

            tweetDatoAsyncTask.update(tweetEntities[0])

            return null

        }

    }

    fun delete(idTweet: Int){
        deleteAsyncTask(tweetDao).execute(idTweet)
    }

    private class deleteAsyncTask internal constructor(private val tweetDatoAsyncTask: TweetDao) :
        AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg params: Int?): Void? {

            params[0]?.let { tweetDatoAsyncTask.deleteOne(it) }

            return null
        }
    }

}