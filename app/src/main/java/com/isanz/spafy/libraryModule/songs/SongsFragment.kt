package com.isanz.spafy.libraryModule.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isanz.spafy.R
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.search.SearchService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentSongsBinding
import com.isanz.spafy.libraryModule.songs.adapter.SongListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongsFragment : Fragment(), IOnItemClickListener {

    private lateinit var mBinding: FragmentSongsBinding
    private var userId: Int = 0
    private lateinit var playlist: PlayList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentSongsBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return mBinding.root
    }

    companion object {
        fun newInstance(userId: Int) = SongsFragment().apply {
            arguments = Bundle().apply {
                putInt("userId", userId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt("userId")
        }
    }

    private fun setUpRecyclerView() {
        mBinding.rvSongs.adapter = SongListAdapter(requireContext(), this)
        mBinding.rvSongs.layoutManager = LinearLayoutManager(requireContext())
        setUpCanciones()
        getPlaylist()
    }

    private fun setUpView() {
        mBinding.progressBar.visibility = View.VISIBLE
        setImage(mBinding.ivPlaylist)
        // Remove _ and first letter to uppercase
        val tittle = playlist.titulo.replace("_", " ").lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                java.util.Locale.getDefault()
            ) else it.toString()
        }
        mBinding.tvPlaylistTittle.text = tittle

    }

    private fun setUpCanciones() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val searchService = retrofit.create(SearchService::class.java)

        lifecycleScope.launch {
            try {
                val response = searchService.getCancionesPlaylist(userId)
                val result = response.body() ?: emptyList()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && result.isNotEmpty()) {
                        val cancionesSearchAdapter = mBinding.rvSongs.adapter as SongListAdapter
                        cancionesSearchAdapter.submitList(result)
                    } else if (response.isSuccessful && result.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.request_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        mBinding.tvNoSongs.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.request_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    mBinding.progressBar.visibility = View.GONE
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

    private fun getPlaylist() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val searchService = retrofit.create(SearchService::class.java)

        lifecycleScope.launch {
            try {
                val response = searchService.getPlaylist(userId)
                val result = response.body()
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        playlist = result
                        setUpView()
                    }
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
        TODO("Not yet implemented")
    }

    override fun onLongItemClick(playlist: PlayList) {
        TODO("Not yet implemented")
    }

    override fun onLongItemClick(cancion: Cancion) {
         MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.remove_song))
            .setMessage(getString(R.string.remove_song_confirmation))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.remove)) { dialog, _ ->
                val adapter = mBinding.rvSongs.adapter as SongListAdapter
                adapter.removeSong(cancion)
                dialog.dismiss()
            }
            .show()
    }

    private fun setImage(view: ImageView) {
        val uri = Constants.IMAGES.random()
        Glide.with(requireContext()).load(uri).transform(CircleCrop()).into(view)
    }

}