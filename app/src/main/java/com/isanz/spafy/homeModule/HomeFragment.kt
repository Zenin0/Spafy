package com.isanz.spafy.homeModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isanz.spafy.R
import com.isanz.spafy.SpafyApplication
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentHomeBinding
import com.isanz.spafy.homeModule.adapter.HomeAlbumAdapter
import com.isanz.spafy.homeModule.adapter.HomePlaylistAdapter
import com.isanz.spafy.homeModule.adapter.HomePodcastAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(), IOnItemClickListener {

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

        val playlistHomeAdapter = HomePlaylistAdapter(this.requireContext(), this)
        val podcastHomeAdapter = HomePodcastAdapter(this.requireContext())
        val albumHomeAdapter = HomeAlbumAdapter(this.requireContext())

        val podcastLayoutManager = LinearLayoutManager(requireContext())
        val playlistLayoutManager = LinearLayoutManager(requireContext())
        val albumLayoutManager = LinearLayoutManager(requireContext())

        podcastLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        playlistLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        albumLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        mBinding.rvPodcast.layoutManager = podcastLayoutManager
        mBinding.rvPodcast.adapter = podcastHomeAdapter


        mBinding.rvPlaylist.layoutManager = playlistLayoutManager
        mBinding.rvPlaylist.adapter = playlistHomeAdapter

        mBinding.rvAlbums.layoutManager = albumLayoutManager
        mBinding.rvAlbums.adapter = albumHomeAdapter
        setUpPlaylists()
        setUpPodcasts()
        setUpAlbums()
    }

    private fun setUpAlbums() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserAlbums(SpafyApplication.idUsuario)
                withContext(Dispatchers.Main) {
                    mBinding.progressBar.visibility = View.GONE
                }
                val result = response.albums
                result.forEach {
                    it.titulo = it.titulo.replace("_", " ").replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(
                            java.util.Locale.getDefault()
                        ) else char.toString()
                    }
                }
                if (result.isNotEmpty()) {
                    val albumHomeAdapter = mBinding.rvAlbums.adapter as HomeAlbumAdapter
                    albumHomeAdapter.submitList(result)
                    mBinding.tusAlbumes.visibility = View.VISIBLE
                } else {
                    mBinding.tusAlbumes.visibility = View.GONE
                }
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.request_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        500 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.server_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.unknown_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setUpPlaylists() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserPlaylists(SpafyApplication.idUsuario)
                withContext(Dispatchers.Main) {
                    mBinding.progressBar.visibility = View.GONE
                }
                val result = response.body() ?: listOf()
                result.forEach {
                    it.titulo = it.titulo.replace("_", " ").replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(
                            java.util.Locale.getDefault()
                        ) else char.toString()
                    }
                }
                if (result.isNotEmpty()) {
                    val playlistHomeAdapter = mBinding.rvPlaylist.adapter as HomePlaylistAdapter
                    playlistHomeAdapter.submitList(result)
                    mBinding.tusPlaylist.visibility = View.VISIBLE
                } else {
                    mBinding.tusPlaylist.visibility = View.GONE
                }
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.request_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        500 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.server_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.unknown_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                    }
                }
            }
        }
    }

    private fun setUpPodcasts() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserPodcast(SpafyApplication.idUsuario)
                withContext(Dispatchers.Main) {
                    mBinding.progressBar.visibility = View.GONE
                }
                val result = response.podcast
                result.forEach {
                    it.titulo = it.titulo.replace("_", " ").replaceFirstChar { char ->
                        if (char.isLowerCase()) char.titlecase(
                            java.util.Locale.getDefault()
                        ) else char.toString()
                    }
                }
                if (result.isNotEmpty()) {
                    val podcastHomeAdapter = mBinding.rvPodcast.adapter as HomePodcastAdapter
                    podcastHomeAdapter.submitList(result)
                    mBinding.tusPodcasts.visibility = View.VISIBLE
                } else {
                    mBinding.tusPodcasts.visibility = View.GONE
                }
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.unknown_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        500 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.server_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.unknown_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onItemClick(cancion: Cancion) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(playlist: PlayList) {
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToSongsFragment(playlist.id)
        )
    }

    override fun onLongItemClick(playlist: PlayList) {
        TODO("Not yet implemented")
    }

    override fun onLongItemClick(cancion: Cancion) {
        TODO("Not yet implemented")
    }
}