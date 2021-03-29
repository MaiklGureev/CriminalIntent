package ru.gureev.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var requiresPolice: Int = 0,
    var suspect: String = "",
    var suspectPhone: String = ""
) : Serializable {

    val photoFileName get() = "Img_$id.jpg"

    override fun toString(): String {
        return "Crime(id=$id, title='$title', date=$date, isSolved=$isSolved, requiresPolice=$requiresPolice, suspect='$suspect', suspectPhone='$suspectPhone', photoFileName='$photoFileName')"
    }


}
