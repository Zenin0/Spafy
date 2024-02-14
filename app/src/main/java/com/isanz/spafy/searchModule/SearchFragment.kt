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
import com.isanz.spafy.R
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.search.SearchService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentSearchBinding
import com.isanz.spafy.searchModule.adapter.SearchListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment(), IOnItemClickListener {
    private lateinit var mBinding: FragmentSearchBinding
    private lateinit var searchListAdapter: SearchListAdapter
    private var canciones: List<Cancion> = listOf()
    private var idUsuario: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchListAdapter = SearchListAdapter(this.requireContext(), this)
        setupRecyclerView()
        setupSearchView()
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idUsuario = it.getInt("userId")
        }
    }

    companion object {
        fun newInstance(id: Int) = SearchFragment().apply {
            arguments = Bundle().apply {
                putInt("userId", id)
            }
        }
    }


    override fun onItemClick(cancion: Cancion) {
        val addPlaylist = AddToPlaylistFragment.newInstance(cancion.id, idUsuario)
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, addPlaylist)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onItemClick(playlist: PlayList) {
        // Not used
    }

    override fun onLongItemClick(playlist: PlayList) {
        // Not used
    }

    override fun onLongItemClick(cancion: Cancion) {
        // Not used
    }


    private fun setupRecyclerView() {
        mBinding.progressBar.visibility = View.VISIBLE
        val cancionesLayoutManager = LinearLayoutManager(requireContext())
        cancionesLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBinding.rvSearch.layoutManager = cancionesLayoutManager
        mBinding.rvSearch.adapter = searchListAdapter
        setUpCanciones()
    }

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList =
                    canciones.filter { it.titulo.contains(newText ?: "", ignoreCase = true) }
                searchListAdapter.submitList(filteredList)
                return true
            }
        })
    }

    private fun setUpCanciones() {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val searchService = retrofit.create(SearchService::class.java)

        lifecycleScope.launch {
            try {
                val response = searchService.getCanciones()
                val result = response.body() ?: emptyList()
                withContext(Dispatchers.Main) {
                    mBinding.progressBar.visibility = View.GONE
                    if (result.isNotEmpty()) {
                        canciones = result
                        val cancionesSearchAdapter = mBinding.rvSearch.adapter as SearchListAdapter
                        cancionesSearchAdapter.submitList(result)
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
                                mBinding.progressBar.visibility = View.GONE
                            }
                        }

                        404 -> {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "No se encontraron canciones",
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