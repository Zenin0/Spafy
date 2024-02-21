package com.isanz.spafy.searchModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isanz.spafy.R
import com.isanz.spafy.SpafyApplication
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.retrofit.search.SearchService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentAddToPlaylistBinding
import com.isanz.spafy.homeModule.adapter.HomePlaylistAdapter
import com.isanz.spafy.searchModule.adapter.AddToPlaylistAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddToPlaylistFragment : Fragment(), IOnItemClickListener {
    private lateinit var mBinding: FragmentAddToPlaylistBinding
    private lateinit var addToPlayListAdapter: AddToPlaylistAdapter
    private lateinit var homePlaylistAdapter: HomePlaylistAdapter

    private var idCancion: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddToPlaylistBinding.inflate(inflater, container, false)
        addToPlayListAdapter = AddToPlaylistAdapter(requireContext(), this)
        homePlaylistAdapter = HomePlaylistAdapter(requireContext(), this)
        setupRecyclerView()
        setUpButtons()
        idCancion = arguments?.getInt("songId") ?: 0
        return mBinding.root
    }

    private fun setUpButtons() {
        mBinding.ibBack.setOnClickListener(
            onBackPressed()
        )
    }

    private fun onBackPressed() = View.OnClickListener {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onItemClick(playlist: PlayList) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.add_to_playlist_message))
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val searchService = retrofit.create(SearchService::class.java)

                lifecycleScope.launch {
                    try {
                        val response =
                            searchService.addSongToPlaylist(
                                playlist.id,
                                idCancion,
                                SpafyApplication.idUsuario
                            )
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.add_to_playlist_success),
                                    Toast.LENGTH_SHORT
                                ).show()
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
            }.show()
    }

    override fun onLongItemClick(playlist: PlayList) {
        // Not used
    }

    override fun onItemClick(cancion: Cancion) {
        // Not used
    }


    override fun onLongItemClick(cancion: Cancion) {
        // Not used
    }

    private fun setupRecyclerView() {
        mBinding.progressBar.visibility = View.VISIBLE
        val cancionesLayoutManager = GridLayoutManager(requireContext(), 2)
        mBinding.rvAddToPlaylist.layoutManager = cancionesLayoutManager
        mBinding.rvAddToPlaylist.adapter = addToPlayListAdapter
        setUpPlaylists()
    }


    private fun setUpPlaylists() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response =
                    homeService.getUserPlaylists(SpafyApplication.idUsuario) // Replace id with idUsuario
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
                    addToPlayListAdapter.submitList(result)
                    homePlaylistAdapter.submitList(result)
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
}