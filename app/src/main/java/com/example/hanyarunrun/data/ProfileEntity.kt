package com.example.hanyarunrun.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val studentName: String,
    val studentId: String,
    val studentEmail: String,
    val profileUri: String?
)
