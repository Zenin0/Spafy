package com.isanz.spafy.common.utils

import com.isanz.spafy.common.entities.Cancion
import com.isanz.spafy.common.entities.PlayList

interface IOnItemClickListener {
    fun onItemClick(cancion: Cancion)
    fun onItemClick(playlist: PlayList)
    fun onLongItemClick(playlist: PlayList)

    fun onLongItemClick(cancion: Cancion)
}