package com.example.john.a436projectstart



import android.content.Context
import android.graphics.*
import android.graphics.Color.rgb
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
class myCanvas : View
{
    //basic graphics info for the canvas
    var width = Int
    var height = Int
    var mBitmap1 : Bitmap? = null
    private var mCanvas = Canvas()
    private val mBitmaptemp= Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
    private val mPath = Path()
    private val mPaint = Paint()
    private var mX = 0.0f
    private var mY = 0.0f
    private var posPath = Path()
    private var posPaint = Paint()
    var set = false
    var drawtype = 0;
    var myimagemodel : ImageModel? = null
    var mycolormodel : ColorModel? = null



    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    {
        mPaint.isAntiAlias = true
        mPaint.color = Color.BLACK
        mPaint.style  = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeWidth = 7f
        posPaint.color = Color.YELLOW
        posPaint.strokeWidth = 10f
        posPaint.style=Paint.Style.STROKE
    }

    //set the colors
    fun setColors(mcolors: ColorModel)
    {
        mycolormodel = mcolors
    }

    fun setcolor(mcolorData: ColorData)
    {
        mPaint.color = rgb(mcolorData.R,mcolorData.G,mcolorData.B)
    }

    private fun startTouch(x: Float,y: Float ) {
        if(myimagemodel!= null) {
            mX = x / ((myimagemodel?.screenwidth?:0).toFloat() / (myimagemodel?.width?:1))
            mY = y / ((myimagemodel?.screenheight?:0).toFloat() / (myimagemodel?.height?:1))


            when (drawtype) {
                //draw with the paint brush
                0 -> if (mX < myimagemodel?.width?:-1 && mY < myimagemodel?.height?:-1 && mX >= 0 && mY >= 0) {
                    fill(mX.toInt() - (mycolormodel?.size?:0 / 2), mY.toInt() - (mycolormodel?.size?:0 / 2), mycolormodel?.size?:0, rgb((mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).R, (mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).G, (mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).B))
                    mBitmap1 = Bitmap.createScaledBitmap(myimagemodel?.bitmap?:mBitmaptemp, myimagemodel?.screenwidth?:1450, myimagemodel?.screenheight?:1450, false)
                }
                //for tracking
                -1 -> {
                    myimagemodel?.row?.setValue(if (mY.toInt() >= 0 && mY.toInt() < myimagemodel?.height?:1) mY.toInt()
                    else if (mY.toInt() < 0) 0
                    else myimagemodel?.height?:1 - 1)

                    myimagemodel?.col?.setValue(if (mX.toInt() >= 0 && mX.toInt() < myimagemodel?.width?:1) mX.toInt()
                    else if (mX.toInt() < 0) 0
                    else myimagemodel?.width?:1 - 1)
                }
                //drawing with the paint bucket
                1 -> {
                    var visited = arrayOf<Array<Boolean>>()
                    for (i in 0..(myimagemodel?.width?:0)) {
                        var array = arrayOf<Boolean>()
                        for (j in 0..(myimagemodel?.height?:0)) {
                            array += false
                        }
                        visited += array
                    }

                    floodfill(mX.toInt(), mY.toInt(), visited, (myimagemodel?.bitmap?:mBitmaptemp).getPixel(mX.toInt(), mY.toInt()))
                }

                else -> print("not done yet")
            }
        }
    }


    private fun moveTouch (x:Float, y: Float)
    {
        if(myimagemodel != null) {
            mX = x / (myimagemodel!!.screenwidth.toFloat() / myimagemodel!!.width)
            mY = y / (myimagemodel!!.screenheight.toFloat() / myimagemodel!!.height)
            when (drawtype) {
                0 -> if (mX < myimagemodel!!.width && mY < myimagemodel!!.height && mX >= 0 && mY >= 0) {
                    fill(mX.toInt() - (mycolormodel!!.size / 2), mY.toInt() - (mycolormodel!!.size / 2), mycolormodel!!.size, rgb((mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).R, (mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).G, (mycolormodel?.chosen
                            ?: ColorData(0, 0, 0)).B))
                    mBitmap1 = Bitmap.createScaledBitmap(myimagemodel!!.bitmap?:mBitmaptemp, myimagemodel!!.screenwidth, myimagemodel!!.screenheight, false)
                }
                -1 -> {
                    myimagemodel!!.row.setValue(if (mY.toInt() >= 0 && mY.toInt() < myimagemodel!!.height) mY.toInt()
                    else if (mY.toInt() < 0) 0
                    else myimagemodel!!.height - 1)

                    myimagemodel!!.col.setValue(if (mX.toInt() >= 0 && mX.toInt() < myimagemodel!!.width) mX.toInt()
                    else if (mX.toInt() < 0) 0
                    else myimagemodel!!.width - 1)
                }

                1 -> {
                    var visited = arrayOf<Array<Boolean>>()
                    for (i in 0..myimagemodel!!.width) {
                        var array = arrayOf<Boolean>()
                        for (j in 0..myimagemodel!!.height) {
                            array += false
                        }
                        visited += array
                    }

                    floodfill(mX.toInt(), mY.toInt(), visited, (myimagemodel!!.bitmap?:mBitmaptemp).getPixel(mX.toInt(), mY.toInt()))
                }

                else -> print("not done yet")
            }
        }
  }

    //for drawing with different sizes
    private fun fill (x : Int, y: Int, size : Int,color : Int) {
        for(i in x .. x + size)
            for(j in y .. y + size)
                if(i < myimagemodel!!.width && j < myimagemodel!!.height && i >= 0 && j >=0)
                    (myimagemodel!!.bitmap?:mBitmaptemp).setPixel(i, j, color)
    }

    //used for the paintbucket (recursive)
    private fun floodfill(x: Int,y: Int,visited: Array<Array<Boolean>>, color: Int)
    {

        if(x >= myimagemodel!!.width || y >= myimagemodel!!.width) return
        if(x< 0 || y < 0) return
        if(visited[x][y]) return
        visited[x][y] = true
        (myimagemodel!!.bitmap?:mBitmaptemp).setPixel(x,y,rgb(mycolormodel!!.chosen?.R?:0,mycolormodel!!.chosen?.G?:0,mycolormodel!!.chosen?.B?:0))

        if(x > 0 && (myimagemodel!!.bitmap?:mBitmaptemp).getPixel(x-1,y) == color) floodfill(x-1,y,visited,color)
        if(x < myimagemodel!!.width-1 && (myimagemodel!!.bitmap?:mBitmaptemp).getPixel(x+1,y) == color) floodfill(x+1,y,visited,color)
        if(y > 0  && (myimagemodel!!.bitmap?:mBitmaptemp).getPixel(x,y-1) == color) floodfill(x,y-1,visited,color)
        if(y < myimagemodel!!.height-1 &&(myimagemodel!!.bitmap?:mBitmaptemp).getPixel(x,y+1) == color) floodfill(x,y+1,visited,color)

    }

    override protected fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBitmap1 = Bitmap.createScaledBitmap(myimagemodel?.bitmap?:
        mBitmaptemp, myimagemodel?.screenwidth?:1, myimagemodel?.screenheight?:1, false)
        canvas?.drawBitmap(mBitmap1?:mBitmaptemp,0.0f,0.0f,mPaint)
        //Done when tracking
        if(drawtype == -1)
        {

            canvas?.drawBitmap(mBitmap1,0.0f,0.0f,mPaint)
            //mPath.reset()
            val cx = ((myimagemodel?.screenwidth?:0) / (myimagemodel?.width?:1).toFloat())
            val cy = ((myimagemodel?.screenheight?:0) / (myimagemodel?.height?:1).toFloat())
            if(!set) {
                for (i in 0..(myimagemodel?.bitmap?:mBitmaptemp).width) {
                    mPath.moveTo(i * cx, 0f)
                    mPath.quadTo(i * cx, 0f, i * cx, (myimagemodel?.screenheight?:1450).toFloat())

                }
                for (i in 0..(myimagemodel?.bitmap?:mBitmaptemp).height) {
                    mPath.moveTo(0f, i * cy)
                    mPath.quadTo(0f, i * cy, (myimagemodel?.screenwidth?:1450).toFloat(), i * cy)
                }
                set = true
            }
            posPath.reset()
            val col = myimagemodel?.col?.value?:0
            val row = myimagemodel?.row?.value?:0
            posPath.moveTo(col*cx,0f)
            posPath.quadTo(col*cx,0f,col*cx,(myimagemodel?.screenheight?:1450).toFloat())
            posPath.moveTo((col + 1)* cx,0f)
            posPath.quadTo((col + 1)*cx,0f,(col + 1)*cx,(myimagemodel?.screenheight?:1450).toFloat())
            posPath.moveTo(0f,(row * cy))
            posPath.quadTo(0f,row * cy,(myimagemodel?.screenwidth?:1450).toFloat(),row * cy)
            posPath.moveTo(0f,(row + 1)*cy)
            posPath.quadTo(0f,(row + 1)*cy,(myimagemodel?.screenwidth?:1450).toFloat(),(row+1)*cy)
            canvas?.drawPath(mPath,mPaint)
            canvas?.drawPath(posPath,posPaint)

        }
    }

    private fun upTouch() {}

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x= event?.x?:0f
        val y = event?.y?:0f
        when (event?.action)
        {
           MotionEvent.ACTION_DOWN ->
           {
               startTouch(x,y)
               invalidate()
           }
            MotionEvent.ACTION_MOVE ->
            {
                moveTouch(x,y)
                invalidate()
            }
            MotionEvent.ACTION_UP ->
            {
                upTouch()
                invalidate()
            }
        }
        return true
    }

    fun setBitmap(bitm: Bitmap, im: ImageModel)
    {
        //get the bitmap and change it to the size of the canvas
        myimagemodel = im
        myimagemodel?.bitmap = bitm.copy(Bitmap.Config.ARGB_8888, true)
        myimagemodel?.width = bitm.width
        myimagemodel?.height = bitm.height
        mBitmap1 = Bitmap.createScaledBitmap(bitm,myimagemodel?.screenwidth?: 1,myimagemodel?.screenheight?: 0,false)
    }
}