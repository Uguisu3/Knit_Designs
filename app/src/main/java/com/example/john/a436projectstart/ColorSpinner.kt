package com.example.john.a436projectstart

import android.content.Context
import android.graphics.Color.rgb
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

internal class SpinnerAdapter(var context: Context,var mycolors: ColorModel) : BaseAdapter() {

    override fun getCount(): Int {
        return mycolors.color.size
    }

    override fun getItem(arg0: Int): Any {
        val mycolor = ArrayList<ColorData>()
        mycolor.add(ColorData(0,0,0))
        return mycolors.color[arg0]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val mycolor = ArrayList<ColorData>()
        mycolor.add(ColorData(0,0,0))
        //var view = view
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null)
        val txv = view.findViewById(android.R.id.text1) as TextView
        val color = mycolors.color[position]
        val R = color.R
        val G = color.G
        val B = color.B
        txv.setBackgroundColor(rgb(R,G,B))//sets the color of the background for each item
        txv.textSize = 20f
        txv.text = "R: $R, G: $G, B: $B" //what text to display
        txv.setTextColor(rgb(255 - color.R,255 - color.G,255 - color.B)) // sets the color of the text
        return view
    }

}