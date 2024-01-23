package com.example.to_do.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database([Note::class],version=1)
abstract class NoteDatabase:RoomDatabase() {
    abstract fun noteDao():NoteDao

    //All instance of the this abstract class, this will remain same
    companion object {
        //THIS MAKES THE FIELD IMMEDIATELY VISIBLE TO OTHER THREAD
        @Volatile
        private var instance:NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context:Context) = instance ?: synchronized(LOCK){
            instance ?: getDataBase(context).also {
                instance = it
            }

        }

        private fun getDataBase(context:Context): NoteDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_database"
            ).build()
    }
    }
