package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModel

class ColorModel: ViewModel()
{
    var color = ArrayList<ColorData>() //keeps track of the colors for the image
    var size = 0 //keeps track of the size of the brush being used
    var chosen : ColorData?= ColorData(0,0,0) //keeps track of the currently chosen color
    var chosenid = 0;   //keeps track of the position of the color in the array adapter

    //adds a new item to the arraylist and makes sure they are unique
    fun add(R: Int,G:Int,B:Int) {
        color.add(ColorData(R,G,B))
        color = ArrayList(color.distinct())
    }
    //removes a color
    fun delete(colorData: ColorData?) {
        if(colorData != null && color.size > 0){
            color.remove(colorData)
        }}
}