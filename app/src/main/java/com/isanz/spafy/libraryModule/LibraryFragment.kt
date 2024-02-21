package com.isanz.spafy.libraryModule

import android.content.Intent
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isanz.spafy.R
import com.isanz.spafy.SpafyApplication
import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList
import com.isanz.spafy.common.retrofit.home.HomeService
import com.isanz.spafy.common.retrofit.library.LibraryService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.common.utils.IOnItemClickListener
import com.isanz.spafy.databinding.FragmentLibraryBinding
import com.isanz.spafy.homeModule.HomeFragmentDirections
import com.isanz.spafy.libraryModule.adapter.LibraryPlaylistAdapter
import com.isanz.spafy.libraryModule.songs.SongsFragment
import com.isanz.spafy.loginModule.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LibraryFragment : Fragment(), IOnItemClickListener {

    private lateinit var mBinding: FragmentLibraryBinding
    private lateinit var libraryPlaylistAdapter: LibraryPlaylistAdapter
    private var playlists: List<PlayList> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        setUpButtons()
        setHasOptionsMenu(true)
        return mBinding.root
    }

    private fun setUpRecyclerView() {
        libraryPlaylistAdapter = LibraryPlaylistAdapter(requireContext(), this)
        mBinding.recyclerView.adapter = libraryPlaylistAdapter
        mBinding.progressBar.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerView.layoutManager = layoutManager
        setUpPlaylists()
        setUpNavDaw()
    }

    private fun setUpNavDaw() {

        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val libraryService = retrofit.create(LibraryService::class.java)
        lifecycleScope.launch {
            try {
                val response = libraryService.getUser(SpafyApplication.idUsuario)
                val user = response.body()
                val headerView = mBinding.navView.getHeaderView(0)
                val tvUsername = headerView.findViewById<TextView>(R.id.tvUsername)
                val tvEmail = headerView.findViewById<TextView>(R.id.tvEmail)
                val ivProfile = headerView.findViewById<ImageView>(R.id.ivProfile)
                tvUsername.text = user?.username
                tvEmail.text = user?.email
                setImage(ivProfile)
            } catch (
                e: Exception
            ) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.request_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        500 -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.server_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
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

    private fun setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList =
                    playlists.filter { it.titulo.contains(newText ?: "", ignoreCase = true) }
                libraryPlaylistAdapter.submitList(filteredList)

                // Assuming mBinding.textView is the TextView you want to hide
                if (newText.isNullOrEmpty()) {
                    mBinding.title.visibility = View.VISIBLE
                } else {
                    mBinding.title.visibility = View.INVISIBLE
                }

                return true
            }
        })
        mBinding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.title.visibility = View.INVISIBLE
            } else {
                mBinding.title.visibility = View.VISIBLE
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
                    playlists = result
                    val playlistLibrary = mBinding.recyclerView.adapter as LibraryPlaylistAdapter
                    playlistLibrary.submitList(result)
                    setupSearchView()
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


    private fun setUpButtons() {
        mBinding.fabCreatePlaylist.setOnClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionNavigationLibraryToCreatePlaylistFragment()
            )

        }
        mBinding.menu.setOnClickListener {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        mBinding.navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            true
        }
        mBinding.navView.menu.findItem(R.id.navigation_home).setOnMenuItemClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToHomeFragment()
            )
            true
        }
        mBinding.navView.menu.findItem(R.id.navigation_search).setOnMenuItemClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToSearchFragment()
            )
            true
        }



    }


    override fun onItemClick(playlist: PlayList) {
        findNavController().navigate(
            LibraryFragmentDirections.actionNavigationLibraryToSongsFragment(playlist.id)
        )
    }

    override fun onLongItemClick(playlist: PlayList) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.remove_playlist))
            .setMessage(getString(R.string.remove_playlisy_confirm))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.remove)) { dialog, _ ->
                val adapter = mBinding.recyclerView.adapter as LibraryPlaylistAdapter
                adapter.removePlaylist(playlist)
                dialog.dismiss()
            }
            .show()
    }

    override fun onItemClick(cancion: Cancion) {
        // Not used
    }

    override fun onLongItemClick(cancion: Cancion) {
        // Not used
    }

    private fun setImage(view: ImageView) {
        val uri = Constants.IMAGES.random()
        Glide.with(requireContext()).load(uri).circleCrop().into(view)
        Glide.with(requireContext()).load(uri).circleCrop().into(mBinding.menu)
    }
}