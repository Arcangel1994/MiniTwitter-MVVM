package com.example.minitwitter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.data.ProfileViewModel
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile
import com.example.minitwitter.ui.newtweet.NewTweetDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import xyz.hasnat.sweettoast.SweetToast

class DashboardActivity : AppCompatActivity(), View.OnClickListener,
    NavController.OnDestinationChangedListener, PermissionListener {

    var viewModel: ProfileViewModel? = null

    var appBarConfiguration: AppBarConfiguration? = null
    var toolbar: Toolbar? = null

    var fabNewTweet: FloatingActionButton? = null

    var imageViewToolbar: ImageView? = null
    var photoUrl: String? = ""
    var navController: NavController? = null
    var navView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProviders.of(this@DashboardActivity).get(ProfileViewModel::class.java)

        toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)

        navView = findViewById(R.id.nav_view)

        imageViewToolbar = findViewById<ImageView>(R.id.imageViewToolbar) as ImageView

        fabNewTweet = findViewById<FloatingActionButton>(R.id.fabNewTweet) as FloatingActionButton
        fabNewTweet!!.setOnClickListener(this)

        photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL)

        if(!photoUrl.isNullOrEmpty()){

            Glide.with(this@DashboardActivity)
                .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .error(R.drawable.ic_logo_minituiter_mini)
                .into(imageViewToolbar!!)

        }

        viewModel?.userProfile?.observe(this@DashboardActivity, object : Observer<Resource<ResponseUserProfile>>{
            override fun onChanged(t: Resource<ResponseUserProfile>?) {

                if(t?.data != null){

                    if(t?.data?.photoUrl != null){

                        Glide.with(this@DashboardActivity)
                            .load(Constantes.API_MINITWITTER_FILES_URL + t?.data?.photoUrl)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .centerCrop()
                            .error(R.drawable.ic_logo_minituiter_mini)
                            .into(imageViewToolbar!!)

                    }
                }

            }
        })

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_tweets_like, R.id.navigation_profile
            )
        )

        navController!!.addOnDestinationChangedListener(this)

        setupActionBarWithNavController(navController!!, appBarConfiguration!!)
        navView!!.setupWithNavController(navController!!)

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        if(controller.graph.startDestination == destination.id) {
            fabNewTweet!!.show()
        }else{
            fabNewTweet!!.hide()
        }

    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.fabNewTweet -> {
                val fm = this@DashboardActivity.supportFragmentManager
                val dialogNuevaNota = NewTweetDialogFragment()
                dialogNuevaNota.show(fm, "NewTweetDialogFragment")
            }

        }

    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

        var seleccionarFoto: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(seleccionarFoto, Constantes.SELECT_PHOTO_GALLERY)

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        SweetToast.info(this@DashboardActivity, "Estos permisos son necesarios para realizar esta funcion")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_CANCELED){
            if(requestCode == Constantes.SELECT_PHOTO_GALLERY){
                if(data != null){

                    var imagenSeleccionada: Uri? = data.data

                    var filePathColumn: Array<String> = arrayOf(MediaStore.Images.Media.DATA);

                    var cursor = getContentResolver().query(imagenSeleccionada!!, filePathColumn, null, null, null);

                    if(cursor != null) {
                        cursor.moveToFirst();
                        var imagenIndex: Int = cursor.getColumnIndex(filePathColumn[0]);
                        var fotoPath: String = cursor.getString(imagenIndex);
                        viewModel!!.uploadPhoto(fotoPath);
                        cursor.close();
                    }

                }
            }
        }

    }

}
