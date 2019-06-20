package com.example.john.a436projectstart

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.save_dialog_fragment.*



class SaveDialogFragment : DialogFragment() {
    var myimagemodel : ImageModel? = null
    interface SaveDialogFragmentListener {
        fun saveimage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myimagemodel = ViewModelProviders.of(activity!!).get(ImageModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.save_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Save_name.setText( myimagemodel?.name)
        Save_button.setOnClickListener {

            myimagemodel?.name = Save_name.text.toString()
            (activity as SaveDialogFragmentListener).saveimage()
            dismiss()

        }
        Cancel.setOnClickListener {
            dismiss()
        }
    }

}

