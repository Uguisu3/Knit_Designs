package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_paint_bucket_info.*


class PaintBucketInfo : Fragment(), AdapterView.OnItemSelectedListener {
    var mycolormodel : ColorModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mycolormodel = ViewModelProviders.of(activity!!).get(ColorModel::class.java) //get the color view model
        return inflater.inflate(R.layout.fragment_paint_bucket_info, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PaintBucketInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up the spinner
        if (mycolormodel?.color != null) {
            spinnerbucket.adapter = SpinnerAdapter(activity as FragmentActivity, mycolormodel!!)
            spinnerbucket.onItemSelectedListener = this
            spinnerbucket.setSelection(mycolormodel?.chosenid?:0)
        }
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        val selected = parent.getItemAtPosition(pos) as ColorData
        mycolormodel?.chosen = selected    //set the new color in the view model
        mycolormodel?.chosen = selected    //set the new color in the view model
        mycolormodel?.chosenid = pos       //set the position of the new color for other spinners
        (activity as PaintBrushInfo.pbiListener).onSelected()

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}


}
