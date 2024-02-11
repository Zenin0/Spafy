package com.isanz.spafy.searchModule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.retrofit.search.SearchService
import com.isanz.spafy.common.retrofit.search.UserIdInfo
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentAddToPlaylistBinding
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
    private var idCancion: Int = 0
    private var idUsuario: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddToPlaylistBinding.inflate(inflater, container, false)
        addToPlayListAdapter = AddToPlaylistAdapter(requireContext(), this)
        setupRecyclerView()
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idCancion = it.getInt("idCancion")
            idUsuario = it.getInt("idUsuario")

        }
    }

    override fun onItemClick(cancion: Cancion) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(playlist: PlayList) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmacion")
            .setMessage("Seguro que quieres añadir la cancion a la playlisy?")
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
                            searchService.addSongToPlaylist(playlist.id, idCancion, UserIdInfo(idUsuario))
                        Log.i("AddToPlaylistFragment", "onItemClick: $response")
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Canción añadida a la playlist",
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
                                            "Error en la petición",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                404 -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "No se encontraron playlists",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                500 -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error en el servidor",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                else -> {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error desconocido",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            }
                        }
                    }
                }
            }
            .show()
    }
    companion object {
        fun newInstance(idCancion: Int, idUsuario: Int) = AddToPlaylistFragment().apply {
            arguments = Bundle().apply {
                putInt("idCancion", idCancion)
                putInt("idUsuario", idUsuario)
            }
        }
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
                val response = homeService.getUserPlaylists(idUsuario) // Replace id with idUsuario
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
                    addToPlayListAdapter.submitList(result)
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
}