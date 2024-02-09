package com.isanz.spafy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AddToPlaylistFragment : Fragment() {


    companion object {
        fun newInstance(): AddToPlaylistFragment {
            return AddToPlaylistFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_playlist, container, false)
    }
}