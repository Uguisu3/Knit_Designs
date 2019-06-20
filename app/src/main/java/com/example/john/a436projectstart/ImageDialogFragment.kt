package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_edit.*
import kotlinx.android.synthetic.main.image_dialog_fragment.*


class ImageDialogFragment : DialogFragment() {
        var myimagemodel: ImageModel? = null
    interface imagesizeListener {
       fun changeimage()
    }
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            myimagemodel = ViewModelProviders.of(activity!!).get(ImageModel::class.java)
            return inflater.inflate(R.layout.image_dialog_fragment, container)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            //set the default values
            image_height.setText(myimagemodel?.height.toString())
            image_width.setText(myimagemodel?.width.toString())
            save_image_size.setOnClickListener {
                Changeimage().execute()
                dismiss()
            }
                cancel_image_set.setOnClickListener {
                    dismiss()
                }
            }

    //scaling image
    private inner class Changeimage : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg params: Unit?) {
            myimagemodel?.bitmap = Bitmap.createScaledBitmap(myimagemodel?.bitmap,
                    image_width.text.toString().toInt(),
                    image_height.text.toString().toInt(),false)
            myimagemodel?.width = myimagemodel?.bitmap?.width?:1
            myimagemodel?.height = myimagemodel?.bitmap?.height?:1
        }

        override fun onPostExecute(result: Unit) {
            //go to the edit and do another function
            (activity as imagesizeListener).changeimage()
        }
    }



}