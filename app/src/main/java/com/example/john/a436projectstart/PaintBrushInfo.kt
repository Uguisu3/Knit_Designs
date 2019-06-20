package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_paint_brush_info.*

class PaintBrushInfo : Fragment(), AdapterView.OnItemSelectedListener {

    var mycolormodel : ColorModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    interface pbiListener {
        fun onSelected()
        fun onSeekBar(state: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mycolormodel = ViewModelProviders.of(activity!!).get(ColorModel::class.java)
        return inflater.inflate(R.layout.fragment_paint_brush_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seekBar.progress = mycolormodel?.size?:0
        //set up the spinner
        if(mycolormodel?.color != null) {
            spinnerbrush.adapter = SpinnerAdapter(activity as FragmentActivity, mycolormodel!!!!)
            spinnerbrush.onItemSelectedListener = this
            spinnerbrush.setSelection(mycolormodel?.chosenid?:0)
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                mycolormodel?.size = progress //change the brush size
                (activity as pbiListener).onSeekBar(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PaintBrushInfo()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        val selected = parent.getItemAtPosition(pos) as ColorData
        mycolormodel?.chosen = selected    //set the new color in the view model
        mycolormodel?.chosenid = pos       //set the position of the new color for other spinners
        (activity as pbiListener).onSelected()

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
