package com.gyenno.zero.medical.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
@Entity(tableName = "message_recorder_table")
data class MessageRecorder(
    @PrimaryKey(autoGenerate = true)
    var recorderId: Long = 0L,

    @ColumnInfo(name = "message_id")
    var messageId: String? = null,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "send_time_milli")
    val sendTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sent_time_milli")
    var sentTimeMilli: Long? = null,

    @ColumnInfo(name = "receive_time_milli")
    var receiveTimeMilli: Long? = null,

    @ColumnInfo(name = "cost_time")
    var costTime: String? = null
)
