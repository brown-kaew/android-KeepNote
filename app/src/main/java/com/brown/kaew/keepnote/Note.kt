package com.brown.kaew.keepnote

import java.util.*

data class Note(
    var title: String,
    var note: String,
    var date: String = Date().time.toString()
)
