package com.isanz.spafy.libraryModule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.isanz.spafy.R
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.retrofit.search.SearchService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentLibraryBinding
import com.isanz.spafy.libraryModule.adapter.LibraryPlaylistAdapter
import com.isanz.spafy.libraryModule.songs.adapter.SongListAdapter
import com.isanz.spafy.libraryModule.songs.SongsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LibraryFragment : Fragment(), IOnItemClickListener {

    private var id: Int = 0
    private lateinit var mBinding: FragmentLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return mBinding.root
    }

    private fun setUpRecyclerView() {
        mBinding.recyclerView.adapter = LibraryPlaylistAdapter(requireContext(), this)
        mBinding.progressBar.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerView.layoutManager = layoutManager

        setUpPlaylists()
    }

    companion object {
        fun newInstance(id: Int) = LibraryFragment().apply {
            arguments = Bundle().apply {
                putInt("id", id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt("id")
        }
    }


    private fun setUpPlaylists() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val homeService = retrofit.create(HomeService::class.java)

        lifecycleScope.launch {
            try {
                val response = homeService.getUserPlaylists(id)
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
                    val playlistsSearchAdapter = mBinding.recyclerView.adapter as LibraryPlaylistAdapter
                    playlistsSearchAdapter.submitList(result)
                }
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error en la petición",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        404 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "No se encontraron playlists",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        500 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error en el servidor",
                                    Toast.LENGTH_SHORT
                                ).show()
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        else -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error desconocido",
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

    override fun onItemClick(cancion: Cancion) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(playlist: PlayList) {
        val songsFragment = SongsFragment.newInstance(playlist.id)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentLibrary, songsFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}