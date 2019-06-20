package com.example.john.a436projectstart

/*
*An image is going to be need to be in the emulator/phone when the app is run to get to the track
*or edit activities directly from the Main activity. From what I have seen the images will end up
* in the Downloads folder for the emulator, when choosing a file you will have to go to the downloads
* folder.
*
*For the the inputting files implicit intents are used, instead of android.content, to get to
* another app for the filesystem and a uri is taken from the output of the filesystem application
* and used to open the image is android.provider.MediaStore.
*
* Most of the buttons and menus don't do anything except bring up dialogs, but the format is there.
* One important button though, is the edit button in the set/change notifications dialog brought up
* in theTrack activity, which will demonstrate the ability to create notifications, which will be using
* android.app.notificationinstead of the android.support.v4.app.NotificationCompat as stated in the
* milestone 2
*/


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.john.a436projectstart.Constants.Companion.IMAGE_RESULT
import com.example.john.a436projectstart.Constants.Companion.IMAGE_RESULT2

class Constants
{
    companion object {
        const val IMAGE_RESULT = 20
        const val IMAGE_RESULT2 = 21
        const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 42
    }
}


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
    }

    //go to the edit activity with a new bitmap
    fun new(view: View)
    {
        val uri: Uri? = null
        val intent = Intent(this, Edit::class.java).apply {}
        intent.putExtra("URI", uri)
        startActivity(intent)
    }

    //Use an implicit intent for the user to select an image then send it to the edit activity
    fun edit(view: View)
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val finalIntent = Intent.createChooser(intent, "Select design")
        startActivityForResult(finalIntent,IMAGE_RESULT2)


    }

    //Use an implicit intent for the user to select an image then send it to the track activity
    fun track(view: View)
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val finalIntent = Intent.createChooser(intent, "Select design")
        startActivityForResult(finalIntent,IMAGE_RESULT)

    }

    //used to get the results back from the implicit intent calls
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == IMAGE_RESULT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
               val intent = Intent(this, Track::class.java).apply {}
               val uri =  data?.data
                intent.putExtra("URI", uri)
               startActivity(intent)
            }
        }
        else if(requestCode == IMAGE_RESULT2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                val intent = Intent(this, Edit::class.java).apply {}
                val uri =  data?.data
                intent.putExtra("URI", uri)
                startActivity(intent)
            }
        }

    }


}
