package com.brown.kaew.keepnote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {

        //For Singleton instantiation
        @Volatile private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
//            if (instance == null) {
//                //instance of db
//                instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NoteDatabase::class.java,
//                    "note"
//                ).addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//
//                        GlobalScope.launch {
//                            populateDB(instance)
//                        }
//
//                    }
//                }).build()
//            }
//            return instance as NoteDatabase

            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    "note"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        GlobalScope.launch {
                            populateDB(instance)
                        }

                    }
                }).build()
            }
        }

        private suspend fun populateDB(db: NoteDatabase?) {
            withContext(Dispatchers.IO) {
                db?.noteDao()?.insertAll(

                    Note("11111", "aaaaaa"),
                    Note(
                        "22222",
                        "simply dummy text of the printing and typeset a gap eleaseinto electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r of Letraset sheets"
                    ),
                    Note("33333", "ng industry. "),
                    Note("44444", "lley of ty"),
                    Note(
                        "55555",
                        "into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r"
                    ),
                    Note(
                        "66666",
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took"
                    ),
                    Note(
                        "77777",
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r"
                    ),
                    Note(
                        "88888",
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took"
                    ),
                    Note(
                        "99999",
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the r"
                    )
                )
            }
        }
    }


}