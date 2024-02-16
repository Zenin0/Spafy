package com.isanz.spafy.libraryModule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.isanz.spafy.R
import com.isanz.spafy.common.retrofit.library.LibraryService
import com.isanz.spafy.common.retrofit.library.PostPlaylist
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreatePlaylistFragment : Fragment() {

    private var userId: Int = 0
    private lateinit var mBinding: FragmentCreatePlaylistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        seUpButtons()
        return mBinding.root
    }

    companion object {
        fun newInstance(userId: Int) = CreatePlaylistFragment().apply {
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

    private fun seUpButtons() {
        mBinding.btnCreate.setOnClickListener {
            val name = mBinding.etName.text.toString()
            val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val homeService = retrofit.create(LibraryService::class.java)
            lifecycleScope.launch {
                try {
                    val response = homeService.createPlaylist(PostPlaylist(name, userId))
                    Log.i("CreatePlaylistFragment", "Response: $response")
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val fragment = LibraryFragment.newInstance(userId)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentCreatePlaylist, fragment).commit()
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
                                        "No se ha podido crear la playlist",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
        mBinding.ibBack.setOnClickListener(
            onBackPressed()
        )
    }

    private fun onBackPressed() = View.OnClickListener {
        requireActivity().supportFragmentManager.popBackStack()
    }
}
