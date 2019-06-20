package com.example.john.a436projectstart

import android.graphics.Color.rgb
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.color_dialog_fragment.*
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.widget.AdapterView

class ColorDialogFragment : DialogFragment(), AdapterView.OnItemSelectedListener {
    private var mycolormodel : ColorModel? = null
    private var selected: Int = 0
    var r = 0
    var g = 0
    var b = 0

    interface ColorFragmentListener {
        fun refresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mycolormodel = ViewModelProviders.of(activity!!).get(ColorModel::class.java)

        return inflater.inflate(R.layout.color_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner2.onItemSelectedListener = this
        if(mycolormodel?.color!= null) {
            //set up the delete spinner
            spinner2.adapter = SpinnerAdapter(activity as FragmentActivity, mycolormodel!!)
        }

        add_color.setOnClickListener {
            mycolormodel?.add(r,g,b)
            if( mycolormodel?.color != null) {
                //refresh the delete spinner when an item is added.
                spinner2.adapter = SpinnerAdapter(activity as FragmentActivity, mycolormodel!!)
            }
        }

        //change r,g, and b with the 3 spinners
        red_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                r = progress
                Color_example.setBackgroundColor(rgb(r,g,b))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        green_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                g = progress
                Color_example.setBackgroundColor(rgb(r,g,b))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        blue_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                b = progress
                Color_example.setBackgroundColor(rgb(r,g,b))
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //delete selected color from the color view model
        delete_color.setOnClickListener {
            if(selected < mycolormodel?.color?.size?: -1) {
                mycolormodel?.delete((mycolormodel as ColorModel).color.get(selected))
            }
        }
        //go back to Edit activity
        Done.setOnClickListener {
            (activity as ColorFragmentListener).refresh()
            dismiss()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        selected = pos //get the selected position

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
