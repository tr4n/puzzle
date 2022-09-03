package com.tr4n.puzzle.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tr4n.puzzle.extension.toStateCode
import com.tr4n.puzzle.extension.toStatePuzzleIndexList
import com.tr4n.puzzle.util.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "challenge")
data class Challenge(
    @PrimaryKey
    @ColumnInfo(name = "imageName")
    var imageName: String = "",
    @ColumnInfo(name = "size")
    var size: Int = Constant.DEFAULT_SIZE,
    @ColumnInfo(name = "state")
    var state: String = (0 until size * size).toStateCode(),
    @ColumnInfo(name = "type")
    var type: Int = 0,
    @ColumnInfo(name = "createdAt")
    var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updatedAt")
    var updatedAt: Long = System.currentTimeMillis(),
) : Parcelable {

    val puzzleIndexes get() = state.toStatePuzzleIndexList()
}
