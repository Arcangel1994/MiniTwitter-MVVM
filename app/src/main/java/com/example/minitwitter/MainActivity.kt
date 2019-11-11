package com.example.minitwitter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.retrofit.MiniTwitterClient
import com.example.minitwitter.retrofit.model.request.RequestLogin
import com.example.minitwitter.retrofit.model.response.ResponseAuth
import com.example.minitwitter.retrofit.model.response.User
import com.example.minitwitter.retrofit.service.MiniTwitterService
import com.example.minitwitter.valid.Valid
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.hasnat.sweettoast.SweetToast

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    var textIrSignUp: TextView? = null
    var buttonLogin: MaterialButton? = null

    var TextEmail: TextInputLayout? = null
    var editTextEmail: TextInputEditText? = null
    var TextPassword: TextInputLayout? = null
    var editTextPassword: TextInputEditText? = null

    var miniTwitterClient: MiniTwitterClient? = null
    var miniTwitterService: MiniTwitterService? = null

    var isValid: Valid? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN).isNullOrEmpty()){
            var i : Intent = Intent(this@MainActivity, DashboardActivity::class.java)
            startActivity(i)
            finish()
        }

        //supportActionBar!!.hide();
        isValid = Valid()

        miniTwitterClient = MiniTwitterClient.getInstance()
        miniTwitterService = miniTwitterClient!!.miniTwitterService

        textIrSignUp = findViewById<TextView>(R.id.textIrSignUp) as TextView
        buttonLogin = findViewById<MaterialButton>(R.id.buttonLogin) as MaterialButton

        TextEmail = findViewById<TextInputLayout>(R.id.TextEmail) as TextInputLayout
        editTextEmail = findViewById<TextInputEditText>(R.id.editTextEmail) as TextInputEditText
        editTextEmail?.addTextChangedListener(this)

        TextPassword = findViewById<TextInputLayout>(R.id.TextPassword) as TextInputLayout
        editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword) as TextInputEditText
        editTextPassword?.addTextChangedListener(this)

        textIrSignUp!!.setOnClickListener(this)
        buttonLogin!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.textIrSignUp -> {

                var i : Intent = Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(i)

            }

            R.id.buttonLogin -> {

                var email = editTextEmail!!.text.toString()
                var password = editTextPassword!!.text.toString()

                if(email.isEmpty()){
                    TextEmail!!.error = "El email es requerido"
                    //SweetToast.info(this@MainActivity, "El email es requerido")
                    //editTextEmail!!.setError("El email es requerido")
                }else if(!isValid!!.isValidEmail(email)){
                    TextEmail!!.error = "El formato del email incorrecto"
                } else if(password.isEmpty()){
                    TextPassword!!.error = "La contraseña es requerida"
                    //SweetToast.info(this@MainActivity, "La contraseña es requerida")
                    //editTextPassword!!.setError("La contraseña es requerida")
                }else{

                    var requestLogin: RequestLogin = RequestLogin(email, password)

                    var call: Call<ResponseAuth> = miniTwitterService!!.doLogin(requestLogin)

                    call.enqueue(object : Callback<ResponseAuth>{
                        override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                            SweetToast.error(this@MainActivity, "Problemas de conexion, Intentelo de nuevo")
                        }

                        override fun onResponse(
                            call: Call<ResponseAuth>,
                            response: Response<ResponseAuth>
                        ) {
                            if(response.isSuccessful){
                                SweetToast.success(this@MainActivity, "Sesion iniciada correctamente")

                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body()!!.token);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body()!!.username);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body()!!.email);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body()!!.photoUrl);
                                SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body()!!.created);
                                SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body()!!.active!!);

                                var i : Intent = Intent(this@MainActivity, DashboardActivity::class.java)
                                startActivity(i)
                                finish()

                            }else{
                                SweetToast.warning(this@MainActivity, "La respuesta es invalida, Cheque sus datos y intentelo de nuevo")
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

        if(TextEmail!!.isErrorEnabled()){
            TextEmail!!.setErrorEnabled(false);
        }

        if(TextPassword!!.isErrorEnabled()){
            TextPassword!!.setErrorEnabled(false);
        }

    }

    override fun onBackPressed() {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

}
