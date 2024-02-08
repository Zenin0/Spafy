package com.isanz.spafy.searchModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.databinding.FragmentSearchBinding
import com.isanz.spafy.searchModule.adapter.SearchListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {
    private lateinit var mBinding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
    private var playlists: List<PlayList> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchListAdapter = SearchListAdapter(this.requireContext())
        setupRecyclerView()
        setUpPlaylists()
        setupSearchView()
        return mBinding.root
    }

    private fun setupRecyclerView() {
        mBinding.progressBar.visibility = View.VISIBLE
        val playlistLayoutManager = LinearLayoutManager(requireContext())
        playlistLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBinding.rvSearch.layoutManager = playlistLayoutManager
        mBinding.rvSearch.adapter = searchListAdapter
    }

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList =
                    playlists.filter { it.titulo.contains(newText ?: "", ignoreCase = true) }
                searchListAdapter.submitList(filteredList)
                return true
            }
        })
    }

    private fun setUpPlaylists() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserPlaylists(1)
                withContext(Dispatchers.Main) {
                    mBinding.progressBar.visibility = View.GONE
                }
                val result = response.playlists
                // Replace _ with space and first letter to upper case
                result.forEach {
                    it.titulo = it.titulo.replace("_", " ").replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(
                            java.util.Locale.getDefault()
                        ) else char.toString()
                    }
                }
                if (result.isNotEmpty()) {
                    playlists = result
                    searchListAdapter.submitList(result)
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