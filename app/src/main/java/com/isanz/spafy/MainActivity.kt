package com.isanz.spafy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.isanz.spafy.databinding.ActivityMainBinding
import com.isanz.spafy.homeModule.HomeFragment
import com.isanz.spafy.libraryModule.LibraryFragment
import com.isanz.spafy.searchModule.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mActiveFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setUpBottomNav()
        setContentView(mBinding.root)
    }

    private fun setUpBottomNav() { // Setup bottom navigation

        mFragmentManager = supportFragmentManager
        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val libraryFragment = LibraryFragment()

        mActiveFragment = homeFragment

        mFragmentManager.beginTransaction().add(
            R.id.hostFragment, libraryFragment, LibraryFragment::class.java.name
        ).hide(libraryFragment).commit()
        mFragmentManager.beginTransaction().add(
            R.id.hostFragment, searchFragment, SearchFragment::class.java.name
        ).hide(searchFragment).commit()
        mFragmentManager.beginTransaction().add(
            R.id.hostFragment, homeFragment, HomeFragment::class.java.name
        ).commit()

        mBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment)
                        .commit()
                    mActiveFragment = homeFragment
                    true
                }

                R.id.searchFragment -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(searchFragment)
                        .commit()
                    mActiveFragment = searchFragment
                    true
                }

                R.id.libraryFragment -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(libraryFragment)
                        .commit()
                    mActiveFragment = libraryFragment
                    true
                }

                else -> false
            }
        }
    }
}