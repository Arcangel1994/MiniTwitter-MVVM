package com.example.minitwitter.ui.deletetweet

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialogFragment
import com.example.minitwitter.R
import com.example.minitwitter.common.Constantes
import com.example.minitwitter.data.TweetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class BottomModalTweetFragment : RoundedBottomSheetDialogFragment(), NavigationView.OnNavigationItemSelectedListener {

    fun newInstance(idTweet: Int): BottomModalTweetFragment {
        val fragment = BottomModalTweetFragment()
        val args = Bundle()
        args.putInt(Constantes.ARG_TWEET_ID, idTweet)
        fragment.arguments = args
        return fragment
    }

    private lateinit var viewModel: TweetViewModel

    var root: View? = null
    var nav: NavigationView? = null

    var idTweetEliminar: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(getArguments() != null) {

            idTweetEliminar = arguments?.getInt(Constantes.ARG_TWEET_ID)!!;

        }
    }

    /*override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false)

        nav = root!!.findViewById<NavigationView>(R.id.navigation_view_bottom_tweet) as NavigationView

        nav!!.setNavigationItemSelectedListener(this)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(TweetViewModel::class.java)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.action_delete_tweet -> {

                viewModel.deleteTweet(idTweetEliminar)
                dialog!!.dismiss()
                return true

            }

        }

        return false

    }

}
