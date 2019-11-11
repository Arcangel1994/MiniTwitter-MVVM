package com.example.minitwitter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minitwitter.retrofit.model.response.Tweet
import android.widget.TextView
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.minitwitter.R
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.common.SharedPreferencesManager
import com.example.minitwitter.data.TweetViewModel
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.load.engine.DiskCacheStrategy


class MyTweetRecyclerViewAdapter(private var mValues: List<Tweet>,
                                 private val ctx: Context) :
      RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder>(){

    var username: String? = null
    var viewModel: TweetViewModel? = null

    init {
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USERNAME)
        viewModel = ViewModelProviders.of(ctx as FragmentActivity).get(TweetViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.setIsRecyclable(false)
        holder.mItem = mValues[position]

        holder.tvUsername!!.text = "@${holder.mItem!!.user!!.usernameUser}"
        holder.tvMessage!!.text = holder.mItem!!.mensaje
        holder.tvLikesCount!!.text = "${holder.mItem!!.likes?.size}"

        Glide.with(ctx)
            .load(R.drawable.ic_logo_minituiter_mini)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .into(holder.ivAvatar!!)

        val photo = holder.mItem!!.user!!.photoUrlUser
        if (photo != "") {
            Glide.with(ctx)
                .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo!!)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .error(R.drawable.ic_logo_minituiter_mini)
                .into(holder.ivAvatar!!)
        }

        Glide.with(ctx)
            .load(R.drawable.ic_like)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .into(holder.ivLike!!)
        holder.tvLikesCount!!.setTextColor(ctx.getResources().getColor(android.R.color.darker_gray))
        holder.tvLikesCount!!.setTypeface(null, Typeface.NORMAL)

        holder.ivShowMenu!!.visibility = View.GONE
        if(holder.mItem!!.user?.usernameUser?.equals(username)!!){
            holder.ivShowMenu!!.visibility = View.VISIBLE
        }

        holder.ivShowMenu!!.setOnClickListener {
            holder.mItem?.id?.let { it1 -> viewModel!!.openDialogTweetMenu(ctx, it1) }
        }

        holder.ivLike!!.setOnClickListener {
            holder.mItem?.id?.let { it1 -> viewModel!!.likeTweet(it1) }
        }

        for (like in holder.mItem!!.likes!!) {
            if (like.usernameLike == username) {
                Glide.with(ctx)
                    .load(R.drawable.ic_like_pink)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(holder.ivLike!!)
                holder.tvLikesCount!!.setTextColor(ctx.getResources().getColor(R.color.pink))
                holder.tvLikesCount!!.setTypeface(null, Typeface.BOLD)
                break
            }
        }
    }


    fun setNuevasTweet(nuevosTweet: List<Tweet>){
        mValues = nuevosTweet;
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

        var ivAvatar: ImageView? = null
        var ivLike: ImageView? = null
        var ivShowMenu: ImageView? = null
        var tvUsername: TextView? = null
        var tvMessage: TextView? = null
        var tvLikesCount: TextView? = null
        var mItem: Tweet? = null

        init {
            ivAvatar = mView.findViewById(R.id.imageViewAvatar);
            ivLike = mView.findViewById(R.id.imageViewLike);
            ivShowMenu = mView.findViewById(R.id.imageViewShowMenu);
            tvUsername = mView.findViewById(R.id.textViewUsername);
            tvMessage = mView.findViewById(R.id.textViewMessage);
            tvLikesCount = mView.findViewById(R.id.textViewLikes);
        }

    }


}