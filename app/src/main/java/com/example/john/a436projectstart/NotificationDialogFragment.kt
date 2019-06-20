package com.example.john.a436projectstart


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.notification_dialog_fragment.*


class NotificationDialogFragment : DialogFragment() {


    interface NotificationDialogFragmentListener {
        fun onEditDialogClick()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.notification_dialog_fragment, container)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       Edit.setOnClickListener {

           (activity as NotificationDialogFragmentListener).onEditDialogClick()
            dismiss()
        }
//
        Cancel2.setOnClickListener {
            dismiss()
        }
    }
}