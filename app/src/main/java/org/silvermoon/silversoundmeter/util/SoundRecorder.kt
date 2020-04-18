package org.silvermoon.silversoundmeter.util

import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException

class SoundRecorder {

    public var recFile: File? = null
    var mediaRecorder: MediaRecorder? = null
    var isRecording: Boolean = false
    val TAG = SoundRecorder::class.java.simpleName

    fun getMaxAmplitude(): Float{
        if(mediaRecorder != null){
            try{
                return mediaRecorder!!.maxAmplitude.toFloat()
            }
            catch (exception: Exception){
                Log.e(TAG,exception.message)
                return 0F
            }
        }
        else{
            return 5f
        }
    }

    fun startRecording(): Boolean{

        if(recFile == null){
            return false
        }

        try{
            mediaRecorder = MediaRecorder()
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder!!.setOutputFile(recFile!!.absolutePath)
            mediaRecorder!!.prepare()
            mediaRecorder!!.start()
            isRecording = true
            return true


        }
        catch (ex: IOException){
            Log.e(TAG,ex.message)
            mediaRecorder!!.reset()
            mediaRecorder!!.release()
            mediaRecorder = null
            isRecording = false

        }
        catch(ex: IllegalStateException){
            stopRecording()
            Log.e(TAG,ex.message)
            isRecording = false
        }

        return false

    }

    fun stopRecording(){

        if(mediaRecorder != null){
            if(isRecording){
                try{
                    mediaRecorder!!.stop()
                    mediaRecorder!!.release()
                }
                catch (ex: Exception){
                    Log.e(TAG,ex.message)
                }
            }

            mediaRecorder = null
            isRecording = false
        }

    }

    fun delete(){

        stopRecording()
        if(recFile != null){
            recFile!!.delete()
            recFile = null
        }

    }


}