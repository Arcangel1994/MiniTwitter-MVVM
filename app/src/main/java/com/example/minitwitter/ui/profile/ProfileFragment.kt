package com.example.minitwitter.ui.profile


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.minitwitter.R
import com.example.minitwitter.data.ProfileViewModel
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.minitwitter.DashboardActivity
import com.example.minitwitter.MainActivity
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.data.TweetViewModel
import com.example.minitwitter.network.Resource
import com.example.minitwitter.retrofit.model.request.RequestUserProfile
import com.example.minitwitter.retrofit.model.response.ResponseUserProfile
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener

class ProfileFragment : Fragment(), View.OnClickListener, TextWatcher {

    private var viewModel: ProfileViewModel? = null
    private var root: View? = null

    var ivAvatar: ImageView? = null

    var Username: TextInputLayout? = null
    var etUsername: TextInputEditText? = null
    var Email:TextInputLayout? = null
    var etEmail:TextInputEditText? = null
    var Password:TextInputLayout? = null
    var etPassword:TextInputEditText? = null
    var Website:TextInputLayout? = null
    var etWebsite:TextInputEditText? = null
    var Descripcion:TextInputLayout? = null
    var etDescripcion:TextInputEditText? = null

    var btnSave: MaterialButton? = null

    var allPermissionsListener: PermissionListener? = null

    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(activity!!).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_profile, container, false)

        ivAvatar = root!!.findViewById<ImageView>(R.id.imageViewAvatar) as ImageView
        ivAvatar!!.setOnClickListener(this)

        Username = root!!.findViewById<TextInputLayout>(R.id.TextUsername) as TextInputLayout
        Email = root!!.findViewById<TextInputLayout>(R.id.TextEmail) as TextInputLayout
        Password = root!!.findViewById<TextInputLayout>(R.id.TextCurrentPassword) as TextInputLayout
        Website = root!!.findViewById<TextInputLayout>(R.id.TextWebsite) as TextInputLayout
        Descripcion = root!!.findViewById<TextInputLayout>(R.id.TextDescripcion) as TextInputLayout

        etUsername = root!!.findViewById<TextInputEditText>(R.id.editTextUsername) as TextInputEditText
        etUsername!!.addTextChangedListener(this)
        etEmail = root!!.findViewById<TextInputEditText>(R.id.editTextEmail) as TextInputEditText
        etEmail!!.addTextChangedListener(this)
        etPassword = root!!.findViewById<TextInputEditText>(R.id.editTextCurrentPassword) as TextInputEditText
        etPassword!!.addTextChangedListener(this)
        etWebsite = root!!.findViewById<TextInputEditText>(R.id.editTextWebsite) as TextInputEditText
        etWebsite!!.addTextChangedListener(this)
        etDescripcion = root!!.findViewById<TextInputEditText>(R.id.editTextDescripcion) as TextInputEditText
        etDescripcion!!.addTextChangedListener(this)

        btnSave = root!!.findViewById<MaterialButton>(R.id.buttonSave) as MaterialButton
        btnSave!!.setOnClickListener(this)

        viewModel?.userProfile?.observe(activity!!, object : Observer<Resource<ResponseUserProfile>>{
            override fun onChanged(t: Resource<ResponseUserProfile>?) {

                if(t?.data != null){
                    etUsername!!.setText("${t?.data?.username}")
                    etEmail!!.setText("${t?.data?.email}")
                    etWebsite!!.setText("${t?.data?.website}")
                    etDescripcion!!.setText("${t?.data?.descripcion}")

                    try {
                        if (t?.data?.photoUrl != null) {
                            Glide.with(activity!!)
                                .load(Constantes.API_MINITWITTER_FILES_URL + t.data.photoUrl)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .centerCrop()
                                .error(R.drawable.ic_logo_minituiter_mini)
                                .into(ivAvatar!!)
                        }
                    }catch (e: Exception){

                    }
                }

            }
        })

        return root
    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.buttonSave -> {

                var username: String = etUsername!!.text.toString()
                var email: String = etEmail!!.text.toString()
                var descripcion: String = etDescripcion!!.text.toString()
                var website: String = etWebsite!!.text.toString()
                var password: String = etPassword!!.text.toString()

                if (username.isNullOrEmpty()){
                    Username!!.error = "El nombre es requerido"
                }else if (email.isNullOrEmpty()){
                    Email!!.error = "El email es requerido"
                }else if (password.isNullOrEmpty()){
                    Password!!.error = "La contraseña actual es requerido"
                }else{

                    var requestUserProfile: RequestUserProfile = RequestUserProfile(
                        username,
                        email,
                        descripcion,
                        website,
                        password
                    )
                    viewModel!!.updateProfile(requestUserProfile)

                }

            }

            R.id.imageViewAvatar -> {

                checkPermissions()

            }

        }

    }

    fun checkPermissions(){

       var dialogOnDeniedPermissionListener: PermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(activity!!)
           .withTitle("Permisos")
           .withMessage("Los permisos solicitados son necesarios para poder seleccionar una foto de perfil")
           .withButtonText("Aceptar")
           .withIcon(R.mipmap.ic_launcher)
           .build()

        allPermissionsListener = CompositePermissionListener(
            activity!! as PermissionListener,
            dialogOnDeniedPermissionListener
        )

        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(allPermissionsListener)
            .check()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sign_off, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_off -> {

                pref = SharedPreferencesManager.getSharedPreferences()
                pref!!.edit().clear().apply()

                viewModel!!.signOff()

                var i : Intent = Intent(activity!!, MainActivity::class.java)
                startActivity(i)
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if(Username!!.isErrorEnabled()){
            Username!!.setErrorEnabled(false);
        }

        if(Email!!.isErrorEnabled()){
            Email!!.setErrorEnabled(false);
        }

        if(Password!!.isErrorEnabled()){
            Password!!.setErrorEnabled(false);
        }

    }


}
