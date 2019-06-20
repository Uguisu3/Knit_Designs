package com.example.john.a436projectstart


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap

class ImageModel: ViewModel()
{
    var bitmap: Bitmap? = null //the bit map we are drawing on
    var width = 20      //the width of the bitmap
    var height = 20     //the height of the bitmap
    var name = ""       //the name of the bitmap
    //the location of the selection mutable so that when it changes here it changes everywhere
    var row = MutableLiveData<Int>()
    var col = MutableLiveData<Int>()
    //screen width and height
    var screenwidth = 1450
    var screenheight = 1450

}