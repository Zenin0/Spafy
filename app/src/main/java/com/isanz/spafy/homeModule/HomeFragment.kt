package com.isanz.spafy.homeModule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.databinding.FragmentHomeBinding
import com.isanz.spafy.homeModule.adapter.HomeListAdapter
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return mBinding.root
    }

    private fun setupRecyclerView() {
        mBinding.progressBar.visibility = View.VISIBLE
        val homeAdapter = HomeListAdapter(this.requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mBinding.rvRecent.layoutManager = layoutManager
        mBinding.rvRecent.adapter = homeAdapter
        getPlaylists()
    }

    private fun getPlaylists() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserPlaylists(1)
                val result = response.playlists
                if (result.isNotEmpty()) {
                    Toast.makeText(
                        requireActivity(), "Playlists fetched successfully", Toast.LENGTH_SHORT
                    ).show()
                    val homeAdapter = mBinding.rvRecent.adapter as HomeListAdapter
                    homeAdapter.submitList(result)
                    mBinding.progressBar.visibility = View.GONE

                }
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            Toast.makeText(requireActivity(), "Error 400", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}