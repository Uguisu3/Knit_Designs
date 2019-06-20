package com.example.john.a436projectstart

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Color.*
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.content_edit.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.FileOutputStream
import java.io.IOException

class Edit : AppCompatActivity(),
        PaintBrushInfo.pbiListener,
        ColorDialogFragment.ColorFragmentListener,
        CanvasDialogFragment.canvassizeListener,
        ImageDialogFragment.imagesizeListener,
        SaveDialogFragment.SaveDialogFragmentListener{
    var mycolormodel: ColorModel? = null
    var myimagemodel: ImageModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)
        mycolormodel = ViewModelProviders.of(this).get(ColorModel::class.java)
        myCanvas.setColors(mycolormodel!!)
        myimagemodel = ViewModelProviders.of(this).get(ImageModel::class.java)

        val fragment = PaintBrushInfo.newInstance()
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.toolbar_options, fragment)
        transaction.commit()
        GetImage(this).execute(this.contentResolver)
        Track_button.setOnClickListener {
            try {
                this.openFileOutput((myimagemodel?.name?:"NON") + ".png", Context.MODE_PRIVATE).use {out ->
                    (myimagemodel?.bitmap
                            ?: Bitmap.createBitmap(myimagemodel?.width?:1, myimagemodel?.height?:1, Bitmap.Config.ARGB_8888)).
                            compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            GetImage(this).cancel(true)
            GetColor(this).cancel(true)
            val intent = Intent(this, Track::class.java).apply {}
            intent.putExtra("NAME", myimagemodel?.name?:"NON" + ".png")
            startActivity(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.edit_menu, menu)
        myimagemodel?.screenwidth = (myCanvas.parent as View).width
        myimagemodel?.screenheight = (myCanvas.parent as View).height

        return super.onCreateOptionsMenu(menu)
    }

    override fun changecanvas() {
        myCanvas.invalidate()
    }

    override fun changeimage() {
        myCanvas.invalidate()
    }

    override fun onSelected() {
        myCanvas.setcolor(mycolormodel?.chosen?: ColorData(0,0,0))
    }

    override fun onSeekBar(state: Int) {

    }

    override fun saveimage()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        }
        else
        {
            SaveImage(this).execute()
        }
    }

    override fun refresh() {
        when(myCanvas.drawtype) {
            //paintbrush
            0 -> {
                val fragment = PaintBrushInfo.newInstance()
                val fm = supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.toolbar_options, fragment)
                transaction.commit()
            }
            //paint bucket
            1->{
                val fragment = PaintBucketInfo.newInstance()
                val fm = supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.toolbar_options, fragment)
                transaction.commit()
            }
            //copypaste
            2->{
                val fragment = CopyPaste.newInstance()
                val fm = supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.toolbar_options, fragment)
                transaction.commit()
            }

        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            //switch the fragment being displayed based on the radio button clicked
            when (view.id) {
                R.id.PaintBrush ->
                    if (checked) {
                        myCanvas.drawtype = 0
                        val fragment = PaintBrushInfo.newInstance()
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.toolbar_options, fragment)
                        transaction.commit()

                    }
                R.id.PaintBucket ->
                    if (checked) {
                        myCanvas.drawtype = 1
                        val fragment = PaintBucketInfo.newInstance()
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.toolbar_options, fragment)
                        transaction.commit()
                    }
                R.id.Copy_Paste ->
                    if (checked) {
                        myCanvas.drawtype = 2
                        val fragment = CopyPaste.newInstance()
                        val fm = supportFragmentManager
                        val transaction = fm.beginTransaction()
                        transaction.replace(R.id.toolbar_options, fragment)
                        transaction.commit()
                    }
            }

        }
    }

    //go to different display fragments based on the item selected
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.canvas_size -> {

            val myFragment = CanvasDialogFragment()
            myFragment.show(supportFragmentManager, "canvas_fragment")
            true
        }

        R.id.image_size -> {
            val myFragment = ImageDialogFragment()
            myFragment.show(supportFragmentManager, "image_fragment")

            true
        }
        R.id.color_change -> {
            val myFragment = ColorDialogFragment()
            myFragment.show(supportFragmentManager, "color_fragment")
            true
        }
        R.id.save -> {
            val myFragment = SaveDialogFragment()
            myFragment.show(supportFragmentManager, "save_fragment")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    //gets the response back from the request permissions
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                   SaveImage(this).execute()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GetImage(this).cancel(true)
        GetColor(this).cancel(true)
    }

    //an Async task to open up the image
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
                myimagemodel?.bitmap = BitmapFactory.decodeStream(openFileInput(myimagemodel?.name))
                myimagemodel?.name = myimagemodel?.name?:"NON.".split(".")[0]
            }
            else {
                myimagemodel?.bitmap = Bitmap.createBitmap(myimagemodel?.width?:1, myimagemodel?.height?:1, Bitmap.Config.ARGB_8888)
            }



        }

        override fun onPostExecute(result: Unit) {
            myCanvas.setBitmap(myimagemodel!!.bitmap!!, myimagemodel!!)
            myCanvas.invalidate()
            android.widget.Toast.makeText(context, "image opened", android.widget.Toast.LENGTH_SHORT).show()
           GetColor(context).execute(0)
        }
    }


    private inner class GetColor(cont: Context) : AsyncTask<Int, Unit, Int>() {
        val context = cont
        override fun doInBackground(vararg params: Int?):Int {
            //getting all the colors from the bitmap recieved and adding it to the colors
                for( y in 0 .. (((myimagemodel?.bitmap?.height)?:1)-1))
                {
                    val color = myimagemodel?.bitmap?.getPixel(params[0]!!,y)
                    mycolormodel!!.color.add(
                            ColorData(red(color?: Color.BLACK),
                                    green(color?: Color.BLACK),
                                    blue(color?: Color.BLACK)))
                }
            mycolormodel?.add(0,0,0)
            var bob = 0
            if((mycolormodel?.color?.size?:20000) > 20000)
            {
              bob =  params[0]!!.plus(myimagemodel?.width?:10000000)
            }
            else
            {
                bob = params[0]!!.plus( 1)
            }
            return (bob)
        }

        override fun onPostExecute(result: Int) {
            //refresh refresh paintbrush and redraw canvas

            if(result < myimagemodel?.width?:0) {
                GetColor(context).execute(result)
            }
            else{

                android.widget.Toast.makeText(context, "Colors updated", android.widget.Toast.LENGTH_SHORT).show()
                val fragment = PaintBrushInfo.newInstance()
                val fm = supportFragmentManager
                val transaction = fm.beginTransaction()
                transaction.replace(R.id.toolbar_options, fragment)
                transaction.commit()
            }
        }
    }

    private inner class SaveImage(cont: Context) : AsyncTask<String, Unit, Unit>() {
        val context = cont
        override fun doInBackground(vararg params: String?) {
            try {
                FileOutputStream((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path.toString() + "/" +(myimagemodel?.name?:"NON") + ".png")).use { out ->

                    (myimagemodel?.bitmap
                            ?: Bitmap.createBitmap(myimagemodel?.width?:1, myimagemodel?.height?:1, Bitmap.Config.ARGB_8888)).compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        override fun onPostExecute(result: Unit) {
            android.widget.Toast.makeText(context, "image saved", android.widget.Toast.LENGTH_SHORT).show()
        }
    }





}
