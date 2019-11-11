package com.example.minitwitter.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.MyApp
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.dao.UserDAO
import com.example.minitwitter.db.MiniTwitterRoomDatabase
import com.example.minitwitter.network.NetworkBoundResource
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.AuthTwitterClient
import com.example.minitwitter.retrofit.model.request.RequestUserProfile
import com.example.minitwitter.retrofit.model.response.ResponseUploadPhoto
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile
import com.example.minitwitter.retrofit.service.AuthTwitterService
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.hasnat.sweettoast.SweetToast
import java.io.File

class ProfileRepository(application: Application) {

    var authTwitterService: AuthTwitterService? = null
    var authTwitterClient: AuthTwitterClient? = null

    private val userDao: UserDAO
    private val db: MiniTwitterRoomDatabase

    init {

        authTwitterClient = AuthTwitterClient.getInstance()
        authTwitterService = authTwitterClient!!.authTwitterService

        db = MiniTwitterRoomDatabase.getDatabase(application)!!

        userDao = db!!.userDao()

    }

    fun getProfile(): LiveData<Resource<ResponseUserProfile>>{

        return object : NetworkBoundResource<ResponseUserProfile, ResponseUserProfile>(){
            override fun saveCallResult(item: ResponseUserProfile) {
                userDao.insert(item)
            }

            override fun loadFromDb(): LiveData<ResponseUserProfile> {
                return userDao.getUser()
            }

            override fun createCall(): Call<ResponseUserProfile> {
                return authTwitterService!!.getProfile()
            }
        }.asLiveData

    }

    fun updateProfile(requestUserProfile: RequestUserProfile){

        var call: Call<ResponseUserProfile> = authTwitterService!!.updateProfile(requestUserProfile)

        call.enqueue(object : Callback<ResponseUserProfile>{
            override fun onFailure(call: Call<ResponseUserProfile>, t: Throwable) {
                SweetToast.error(MyApp.getContext(), "Problemas de conexion, Intentelo de nuevo")
            }

            override fun onResponse(
                call: Call<ResponseUserProfile>,
                response: Response<ResponseUserProfile>
            ) {

                if(response.isSuccessful){

                    response.body()?.let { update(it) }

                    SweetToast.success(MyApp.getContext(), "Informacion actualizada")

                }else{
                    SweetToast.info(MyApp.getContext(), "No se pudo realizar la actualizacion de tus datos, checa tu informacion")
                }

            }

        })

    }

    fun uploadPhoto(photoPath: String){
        var file: File = File(photoPath)
        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), file)

        var call: Call<ResponseUploadPhoto> = authTwitterService!!.uploadProfilePhoto(requestBody)

        call.enqueue(object : Callback<ResponseUploadPhoto>{
            override fun onFailure(call: Call<ResponseUploadPhoto>, t: Throwable) {
                SweetToast.error(MyApp.getContext(), "Problemas de conexion, Intentelo de nuevo")
            }

            override fun onResponse(
                call: Call<ResponseUploadPhoto>,
                response: Response<ResponseUploadPhoto>
            ) {
                if(response.isSuccessful){

                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body()?.filename)

                    var email = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_EMAIL)

                    uploadPhotoRoom(response.body()?.filename, email)

                }else{
                    SweetToast.info(MyApp.getContext(), "No se pudo realizar la actualizacion de tu foto")
                }
            }
        })

    }

    //Room
    fun update(user: ResponseUserProfile){
        updateAsyncTask(userDao).execute(user)
    }

    private class updateAsyncTask internal constructor(private val useDatoAsyncTask: UserDAO) :
        AsyncTask<ResponseUserProfile, Void, Void>() {

        override fun doInBackground(vararg notaEntities: ResponseUserProfile): Void? {

            useDatoAsyncTask.update(notaEntities[0])

            return null

        }

    }

    private fun uploadPhotoRoom(filename: String?, email: String?) {
        updateRoomAsyncTask(userDao).execute(filename, email)
    }

    private class updateRoomAsyncTask internal constructor(private val useDatoAsyncTask: UserDAO) :
        AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String?): Void? {

            params[0]?.let { params[1]?.let { it1 -> useDatoAsyncTask.updatePhoto(it, it1) } }

            return null

        }
    }

    fun signOff(){

        signOffRoomAsyncTask(db).execute()

    }

    private class signOffRoomAsyncTask internal constructor(private val db: MiniTwitterRoomDatabase) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {

            db.clearAllTables()

            return null

        }
    }

}