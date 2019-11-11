package com.example.minitwitter.ui.newtweet

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.minitwitter.R
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.data.TweetViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NewTweetDialogFragment : DialogFragment(), View.OnClickListener, TextWatcher {

    var root: View? = null
    var close: ImageView? = null
    var buttonTwittear: MaterialButton? = null
    var imageViewAvatar: ImageView? = null
    var editTextMensaje: EditText? = null
    var photoUrl: String? = ""

    private var viewModel: TweetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        root = inflater.inflate(R.layout.fragment_new_tweet_dialog, container, false)

        close = root!!.findViewById<ImageView>(R.id.imageViewClose) as ImageView
        buttonTwittear = root!!.findViewById<MaterialButton>(R.id.buttonTwittear) as MaterialButton
        imageViewAvatar = root!!.findViewById<ImageView>(R.id.imageViewAvatar) as ImageView
        editTextMensaje = root!!.findViewById<EditText>(R.id.editTextMensaje) as EditText
        editTextMensaje?.addTextChangedListener(this)

        close!!.setOnClickListener(this)
        buttonTwittear!!.setOnClickListener(this)


        photoUrl = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL)

        if(!photoUrl.isNullOrEmpty()){

            Glide.with(activity!!)
                .load(Constantes.API_MINITWITTER_FILES_URL + photoUrl)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .error(R.drawable.ic_logo_minituiter_mini)
                .into(imageViewAvatar!!)

        }

        return root
    }


    override fun onClick(v: View?) {

        var mensaje: String = editTextMensaje!!.text.toString()

        when(v!!.id){

            R.id.imageViewClose -> {

                if(mensaje.isNullOrEmpty()) {
                    dialog!!.dismiss()
                }else{
                    showDialogConfirm()
                }

            }

            R.id.buttonTwittear -> {

                if(mensaje.isNullOrEmpty()) {
                    editTextMensaje!!.setError("Debe escribir un texto en el mensaje")
                }else{
                    viewModel = ViewModelProviders.of(activity!!).get(TweetViewModel::class.java)
                    viewModel!!.insertTweet(mensaje)

                    dialog!!.dismiss()
                }

            }

        }

    }

    fun showDialogConfirm(){

        val builder = MaterialAlertDialogBuilder(activity)

        builder.setMessage("¿Desea realmente eliminar el tweet? El mensaje se borrara")
            .setTitle("Cancelar tweet")

        builder.setPositiveButton("Eliminar", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
                getDialog()!!.dismiss()
            }
        })

        builder.setNegativeButton("Cancelar", object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog!!.dismiss()
            }
        })

        var dialog = builder.create()
        dialog.show()

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        editTextMensaje!!.setError(null)

    }

}
