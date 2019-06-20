package com.example.john.a436projectstart

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_track.*
import kotlinx.android.synthetic.main.content_track.*
import java.io.*

class Track : AppCompatActivity(), NotificationDialogFragment.NotificationDialogFragmentListener  {
    private var notificationManager: NotificationManager? = null
    var myimagemodel : ImageModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myimagemodel = ViewModelProviders.of(this).get(ImageModel::class.java)
        myimagemodel?.bitmap = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
        myimagemodel?.width =  myimagemodel?.bitmap?.width?: 1
        myimagemodel?.height =  myimagemodel?.bitmap?.height?:1
        setContentView(R.layout.activity_track)
        setSupportActionBar(toolbar)

        row_value.setOnClickListener {
            myimagemodel?.row?.setValue(Integer.parseInt(col_value.text.toString()))

            if((myimagemodel?.row?.value?:-1 > ((myimagemodel?.height?:1)-1))) {
                myimagemodel?.row?.setValue((myimagemodel?.height?:1) - 1)
            }

        }
        col_value.setOnClickListener {
            myimagemodel?.col?.setValue(Integer.parseInt(col_value.text.toString()))
            if(myimagemodel?.col?.value?:-1 >((myimagemodel?.width?:1)-1)) {
                myimagemodel?.col?.setValue((myimagemodel?.width?:1) - 1)
            }
        }
        row_plus.setOnClickListener {
            if(myimagemodel?.row?.value?:-1 <((myimagemodel?.height?:1)-1)) {
                myimagemodel?.row?.setValue((myimagemodel?.row?.value?:0) +1)
            }
        }
        col_plus.setOnClickListener {
            if(myimagemodel?.col?.value?:1 <((myimagemodel?.width?:1)-1)) {
                myimagemodel?.col?.setValue((myimagemodel?.col?.value?:0 )+ 1)
            }
            else if(myimagemodel?.row?.value?:1 <((myimagemodel?.height?:1)-1)) {
                myimagemodel?.col?.setValue(0)
                myimagemodel?.row?.setValue((myimagemodel?.row?.value?:0) + 1)
            }
        }
        row_minus.setOnClickListener {
            if(myimagemodel?.row?.value?: 1> 0 ) {
                myimagemodel?.row?.setValue((myimagemodel?.row?.value?: 1)- 1)
            }
        }
        col_minus.setOnClickListener {
            if (myimagemodel?.col?.value?:1 > 0) {
                myimagemodel?.col?.setValue((myimagemodel?.col?.value?:1) - 1)

            } else if(myimagemodel?.row?.value?: 1 > 0) {
                myimagemodel?.col?.setValue((myimagemodel?.width?:1 )- 1)
                myimagemodel?.row?.setValue((myimagemodel?.row?.value?:1) - 1)

            }
        }

        val rowob = Observer<Int> {row ->
            // Update the UI, in this case, a TextView.
            row_value.setText(row.toString())
            var color =  myimagemodel?.bitmap?.getPixel(myimagemodel?.col?.value?:0,myimagemodel?.row?.value?:0)
            if(color == 0) color = Color.WHITE
            Color_selected.setBackgroundColor(color?:Color.BLACK)

            myCanvas2.invalidate()
        }
        val colob = Observer<Int> {col ->
            // Update the UI, in this case, a TextView.
            col_value.setText(col.toString())
            var color =  myimagemodel?.bitmap?.getPixel(myimagemodel?.col?.value?:0,myimagemodel?.row?.value?:0)
            if(color == 0) color = Color.WHITE
            Color_selected.setBackgroundColor(color?:Color.BLACK)
            myCanvas2.invalidate()
        }

        GetImage(this).execute(this.contentResolver)
        myimagemodel?.row?.observe(this,rowob)
        myimagemodel?.col?.observe(this,colob)
        myCanvas2.drawtype = -1

        val settings = getSharedPreferences(myimagemodel?.name, Context.MODE_PRIVATE)
        myimagemodel?.row?.setValue(settings.getInt("row", 0))
        myimagemodel?.col?.setValue(settings.getInt("col", 0))
        if(myimagemodel?.row?.value?:1 < 0)
        {
            myimagemodel?.row?.value = 0
        }
        if(myimagemodel?.col?.value?:1 < 0)
        {
            myimagemodel?.col?.value = 0
        }
        if(myimagemodel?.row?.value?:-1 >= myimagemodel?.height?:0)
        {
            myimagemodel?.row?.value = (myimagemodel?.height?:1)-1
        }
        if(myimagemodel?.col?.value?:-1 >= myimagemodel?.width?:0)
        {
            myimagemodel?.col?.value = (myimagemodel?.width?:1)-1
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel("com.example.john.a436projectstart", "Testing out Notifications", "You got a notification")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.track_menu, menu)
        myimagemodel?.screenwidth = (myCanvas2.parent as View).width
        myimagemodel?.screenheight = (myCanvas2.parent as View).height
        return super.onCreateOptionsMenu(menu)
    }

    override fun onEditDialogClick()
    {
        sendNotification()
    }



    private fun createNotificationChannel(id: String, name: String, description: String)
    {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id,name,importance)
        channel.description = description
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.vibrationPattern = longArrayOf(100,200,300,400,500,400,300,200,400)
        notificationManager?.createNotificationChannel(channel)

    }

    fun sendNotification()
    {
        val notificationID = 101
        val channelID = "com.example.john.a436projectstart"
        val notification = Notification.Builder(this@Track,channelID)
                .setContentTitle("Example Notification")
                .setContentText("THis is an example notification")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setChannelId(channelID)
                .build()
        notificationManager?.notify(notificationID,notification)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_m -> {

            try {
                this.openFileOutput(myimagemodel?.name + ".png", Context.MODE_PRIVATE).use {out ->
                    (myimagemodel?.bitmap
                            ?: Bitmap.createBitmap(myimagemodel?.width?:1, myimagemodel?.height?:1, Bitmap.Config.ARGB_8888)).compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }


            val intent = Intent(this, Edit::class.java).apply {}
            intent.putExtra("NAME", myimagemodel?.name + ".png")
            startActivity(intent)
            true
        }

        R.id.set_notification -> {
            val myFragment = NotificationDialogFragment()
            myFragment.show(supportFragmentManager,"Notification_fragment")

            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()

        val settings = getSharedPreferences(myimagemodel?.name, Context.MODE_PRIVATE)
        val edit = settings.edit()
        edit.putInt("row", myimagemodel?.row?.value?:0)
        edit.putInt("col",myimagemodel?.col?.value?:0)
        edit.apply()
    }



    override fun onDestroy() {
        super.onDestroy()
        val settings = getSharedPreferences(myimagemodel?.name, Context.MODE_PRIVATE)
        val edit = settings.edit()
        edit.putInt("row", myimagemodel?.row?.value?:0)
        edit.putInt("col",myimagemodel?.col?.value?:0)
        edit.apply()
    }

    private inner class GetImage(cont : Context) : AsyncTask<ContentResolver, Unit, Unit>() {
        val context = cont
        override fun doInBackground(vararg params: ContentResolver?) {
            val bitmap = getIntent().getParcelableExtra<Uri>("URI")
            myimagemodel?.name= getIntent().getStringExtra("NAME")?: ""
            if (bitmap != null) {
                myimagemodel?.bitmap = MediaStore.Images.Media.getBitmap(params.get(0), bitmap)
                myimagemodel?.name  = bitmap.path.split("/").last().split(".")[0]

            }
            else if (myimagemodel?.name != "")
            {
                myimagemodel?.bitmap = BitmapFactory.decodeStream(openFileInput((myimagemodel?.name?:"NON") + ".png"))
                myimagemodel?.name = (myimagemodel?.name?:"NON.").split(".")[0]
            }
            else {
                myimagemodel?.bitmap = Bitmap.createBitmap(myimagemodel?.width?:1, myimagemodel?.height?:1, Bitmap.Config.ARGB_8888)
            }
        }

        override fun onPostExecute(result: Unit) {
            myCanvas2.set = false
            myCanvas2.setBitmap(myimagemodel?.bitmap!!, myimagemodel!!)
            myCanvas2.invalidate()
            android.widget.Toast.makeText(context, "image opened", android.widget.Toast.LENGTH_SHORT).show()
        }
    }


}
