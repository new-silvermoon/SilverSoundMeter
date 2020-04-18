package org.silvermoon.silversoundmeter.util

class DecibelUtils {
    companion object{
        var dbCount = 40f
        var minDB = 100f
        var maxDb = 0f
        var lastDbCount = dbCount
        val min_sound_level = 0.5f
        var sound_db_val = 0f

        fun setDBCount(dbVal: Float){
            if(dbVal > lastDbCount){
                sound_db_val =  if (dbVal - lastDbCount > min_sound_level) (dbVal - lastDbCount) else min_sound_level
            }
            else{
                sound_db_val = if(dbVal - lastDbCount < -min_sound_level) (dbVal - lastDbCount) else -min_sound_level
            }

            dbCount = lastDbCount + sound_db_val * 0.2f
            lastDbCount = dbCount

            if(dbCount < minDB){
                minDB = dbCount
            }
            if(dbCount > maxDb){
                maxDb = dbCount
            }


        }
    }
}