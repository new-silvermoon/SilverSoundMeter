package org.silvermoon.silversoundmeter.util

import android.R.attr.path
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException


class FileIOUtil {
    companion object{
        val TAG = FileIOUtil::class.java.simpleName
        val LOCAL = "SoundMeter"
        val LOCAL_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator
        val REC_PATH = LOCAL_PATH + LOCAL + File.separator


        fun doesExtSDCardExist(): Boolean{
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
        }

        fun hasFile(name: String): Boolean{
            val file = createFile(name)
            return file != null && file.exists()
        }

        fun createFile(name: String): File {

            val dir = File(REC_PATH)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(REC_PATH + name)
            if(file.exists()){
                file.delete()
            }
            try{
                file.createNewFile()
            }
            catch (ex: IOException){
                Log.e(TAG,ex.message)
            }

            return file
        }


    }

    init {
        val rootFile = File(LOCAL_PATH)
        if(!rootFile.exists()){
            rootFile.mkdirs()
        }

        val recFile = File(REC_PATH)
        if(!recFile.exists()){
            recFile.mkdirs()
        }
    }




}