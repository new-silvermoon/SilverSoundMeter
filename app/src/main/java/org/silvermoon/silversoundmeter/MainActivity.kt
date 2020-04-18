package org.silvermoon.silversoundmeter


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.silvermoon.silversoundmeter.util.*
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity() : AppCompatActivity(), OnClickListener {


    private var isRefreshed: Boolean = false
    private var isGraphInitialized: Boolean = false
    private var isThreadRunning: Boolean = true
    private var isListening: Boolean = true
    private var thread: Thread? = null
    private var currentTime = 0L
    private var savedTime = 0L
    private var soundRecorder: SoundRecorder? = null

    private var volume = 10000F
    private var refresh = 0



        val handler = object : Handler() {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                val decimalFormat = DecimalFormat("####.0")

                if (msg.what == 1) {

                    soundmeter.refresh()
                    tvMinVal.text = decimalFormat.format(DecibelUtils.minDB)
                    tvMmval.text =
                        decimalFormat.format((DecibelUtils.minDB + DecibelUtils.maxDb) / 2)
                    tvMaxval.text = decimalFormat.format(DecibelUtils.maxDb)
                    tvCurVal.text = decimalFormat.format(DecibelUtils.dbCount)

                    if (refresh == 1) {

                        var now = Date().time
                        now = now - currentTime
                        now = now / 1000
                        refresh = 0

                    } else {
                        refresh++
                    }


                }

            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_main)
        soundRecorder = SoundRecorder()

    }

    override fun onResume() {
        super.onResume()

        if(!PermissionsUtil.hasPermissions(this))
        {
            PermissionsUtil.requestPermissions(this)
        }
        else{
            initiateRecording()
        }
    }

    fun initiateRecording(){
        val file = FileIOUtil.createFile("temp.amr")
        if (file != null) {
            startRecording(file)
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.recFileErr),
                Toast.LENGTH_LONG
            ).show()
        }
        isListening = true
    }

    override fun onPause() {
        super.onPause()
        isListening = false
        soundRecorder!!.delete()
        thread = null
        isGraphInitialized = false
    }

    override fun onDestroy() {
        if (thread != null) {
            isThreadRunning = false
            thread = null
        }
        soundRecorder!!.delete()
        super.onDestroy()
    }



    fun listenToAudio(){
        thread = Thread(Runnable {
            while (isThreadRunning) {
                try {
                    if (isListening) {
                        volume = soundRecorder!!.getMaxAmplitude()
                        if (volume > 0 && volume < 1000000) {
                            DecibelUtils.setDBCount(20 * (Math.log10(volume.toDouble())).toFloat());
                            val message = Message()
                            message.what = 1
                            handler.sendMessage(message)
                        }
                    }
                    if (isRefreshed) {
                        Thread.sleep(1200)
                        isRefreshed = false
                    } else {
                        Thread.sleep(200)
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    isListening = false
                }
            }
        })
        thread!!.start()
    }

    fun startRecording(file: File){
        try {
            soundRecorder!!.recFile = file
            if (soundRecorder!!.startRecording()) {
                listenToAudio()
            } else {
                Toast.makeText(this, getString(R.string.recStartErr), Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.recBusyErr), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {

        if(v!!.id == R.id.btnrefresh ){
            isRefreshed = true
            DecibelUtils.minDB = 100F
            DecibelUtils.dbCount = 0F
            DecibelUtils.lastDbCount = 0F
            DecibelUtils.maxDb = 0F



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PERMISSIONS_REQ_CODE && resultCode == Activity.RESULT_OK){

            initiateRecording()
        }

    }





}
