package com.example.minitwitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.retrofit.MiniTwitterClient
import com.example.minitwitter.retrofit.model.request.RequestSignup
import com.example.minitwitter.retrofit.model.response.ResponseAuth
import com.example.minitwitter.retrofit.service.MiniTwitterService
import com.example.minitwitter.valid.Valid
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.hasnat.sweettoast.SweetToast

class SignUpActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    var buttonSignUp: MaterialButton? = null

    var TextNombre: TextInputLayout? = null
    var editTextNombre: TextInputEditText? = null

    var TextEmail: TextInputLayout? = null
    var editTextEmail: TextInputEditText? = null

    var TextPassword: TextInputLayout? = null
    var editTextPassword: TextInputEditText? = null

    var miniTwitterClient: MiniTwitterClient? = null
    var miniTwitterService: MiniTwitterService? = null

    var isValid: Valid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //supportActionBar!!.hide();
        isValid = Valid()

        miniTwitterClient = MiniTwitterClient.getInstance()
        miniTwitterService = miniTwitterClient!!.miniTwitterService

        TextNombre = findViewById<TextInputLayout>(R.id.TextNombre) as TextInputLayout
        editTextNombre = findViewById<TextInputEditText>(R.id.editTextNombre) as TextInputEditText
        editTextNombre?.addTextChangedListener(this)

        TextEmail = findViewById<TextInputLayout>(R.id.TextEmail) as TextInputLayout
        editTextEmail = findViewById<TextInputEditText>(R.id.editTextEmail) as TextInputEditText
        editTextEmail?.addTextChangedListener(this)

        TextPassword = findViewById<TextInputLayout>(R.id.TextPassword) as TextInputLayout
        editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword) as TextInputEditText
        editTextPassword?.addTextChangedListener(this)

        buttonSignUp = findViewById<MaterialButton>(R.id.buttonSignUp) as MaterialButton

        buttonSignUp!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.buttonSignUp -> {

                var nombre = editTextNombre!!.text.toString()
                var email = editTextEmail!!.text.toString()
                var password = editTextPassword!!.text.toString()

                if(nombre.isEmpty()){
                    TextNombre!!.error = "El nombre es requerido"
                    //SweetToast.info(this@SignUpActivity, "El nombre es requerido")
                } else if(email.isEmpty()){
                    TextEmail!!.error = "El email es requerido"
                    //SweetToast.info(this@SignUpActivity, "El email es requerido")
                }else if(!isValid!!.isValidEmail(email)){
                    TextEmail!!.error = "El formato del email incorrecto"
                }else if(password.isEmpty()){
                    TextPassword!!.error = "El contraseña es requerido"
                    //SweetToast.info(this@SignUpActivity, "La contraseña es requerida")
                }else if(password.length < 4){
                    TextPassword!!.error = "Debe tener al menos 4 caracteres"
                } else{

                    var requestSignup: RequestSignup = RequestSignup(nombre, email, password, "UDEMYANDROID")

                    var call: Call<ResponseAuth> = miniTwitterService!!.doSignUp(requestSignup)

                    call.enqueue(object : Callback<ResponseAuth> {
                        override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                            SweetToast.error(this@SignUpActivity, "Problemas de conexion, Intentelo de nuevo")
                        }

                        override fun onResponse(
                            call: Call<ResponseAuth>,
                            response: Response<ResponseAuth>
                        ) {
                            if(response.isSuccessful){
                                SweetToast.success(this@SignUpActivity, "Bienvenido a MiniTwitter")

                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body()!!.token);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body()!!.username);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body()!!.email);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body()!!.photoUrl);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body()!!.created);
                                SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body()!!.active!!);

                                var i : Intent = Intent(this@SignUpActivity, DashboardActivity::class.java)
                                startActivity(i)
                                finish()
                            }else{
                                SweetToast.warning(this@SignUpActivity, "La respuesta es invalida, estos datos ya existen y intentelo de nuevo")
                            }
                        }

                    })

                }

            }

        }

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if(TextNombre!!.isErrorEnabled()){
            TextNombre!!.setErrorEnabled(false);
        }

        if(TextEmail!!.isErrorEnabled()){
            TextEmail!!.setErrorEnabled(false);
        }

        if(TextPassword!!.isErrorEnabled()){
            TextPassword!!.setErrorEnabled(false);
        }

    }

}
