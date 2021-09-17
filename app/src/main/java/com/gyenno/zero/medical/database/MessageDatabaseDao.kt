package com.gyenno.zero.medical.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
@Dao
interface MessageDatabaseDao {

    @Insert
    suspend fun insert(message: MessageRecorder)

    @Update
    suspend fun update(message: MessageRecorder)

    @Query("SELECT * FROM message_recorder_table ORDER BY recorderId ASC")
    fun getAllMessages(): LiveData<List<MessageRecorder>>

    @Query("SELECT * from message_recorder_table WHERE uuid = :uuid")
    suspend fun get(uuid: String): MessageRecorder?
}