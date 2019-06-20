package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.canvas_dialog_fragment.*

class CanvasDialogFragment : DialogFragment() {
    var myimagemodel: ImageModel? = null

    interface canvassizeListener {
        fun changecanvas()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        myimagemodel = ViewModelProviders.of(activity!!).get(ImageModel::class.java)
        return inflater.inflate(R.layout.canvas_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set the text boxes with default values
        canvas_height.setText(myimagemodel?.height.toString())
        canvas_width.setText(myimagemodel?.width.toString())
        save_canvas_size.setOnClickListener {
            super.onViewCreated(view, savedInstanceState)
            ChangeCanvas().execute() //go to async task
            dismiss()
        }
        cancel_canvas_set.setOnClickListener {
            dismiss()
        }
    }

    private inner class ChangeCanvas : AsyncTask<Unit,Unit,Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            //change the canvas size and update the image view model
            var bitmap = Bitmap.createBitmap(
                    canvas_width.text.toString().toInt(),
                    canvas_height.text.toString().toInt(), Bitmap.Config.ARGB_8888)
            val width = if (myimagemodel?.width?:0 > canvas_width.text.toString().toInt()) canvas_width.text.toString().toInt() else myimagemodel?.width
            val height = if (myimagemodel?.height?:0 > canvas_height.text.toString().toInt()) canvas_height.text.toString().toInt() else myimagemodel?.height
            for (x in 0 until width!!) {
                for (y in 0 until height!!) {
                    bitmap.setPixel(x, y, myimagemodel?.bitmap?.getPixel(x,y)?: Color.BLACK)
                }
            }
            myimagemodel?.bitmap = bitmap
            myimagemodel?.width = myimagemodel?.bitmap?.width?: 1
            myimagemodel?.height = myimagemodel?.bitmap?.height?:1

        }

        override fun onPostExecute(result: Unit) {
            (activity as canvassizeListener).changecanvas()
        }
    }

}
