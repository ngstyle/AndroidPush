package com.gyenno.zero.medical.ui.dashboard

import android.app.Application
import androidx.lifecycle.*
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.NotificationMessage
import com.gyenno.zero.medical.database.MessageDatabase
import com.gyenno.zero.medical.database.MessageRecorder
import com.gyenno.zero.medical.network.GyennoApi
import com.gyenno.zero.medical.network.getPushContentBody
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.random.Random.Default.nextLong

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val messageDatabaseDao = MessageDatabase.getInstance(application).messageDatabaseDao
    val allMessages = messageDatabaseDao.getAllMessages()
    private var pushJob: Job? = null

    private var _current = MutableLiveData<Int>()
    val current: LiveData<Int>
        get() = _current


    fun pushMessage(count: Int) {
        val registrationID = JPushInterface.getRegistrationID(getApplication())

        pushJob = flow {
            repeat(count) {
                _current.value = it + 1
                val randomUUID = UUID.randomUUID().toString()
                messageDatabaseDao.insert(MessageRecorder(uuid = randomUUID))
                emit(
                    GyennoApi.retrofitService.pushMessage(
                        getPushContentBody(
                            registrationID,
                            randomUUID
                        )
                    ).apply { uuid = randomUUID })
                delay(nextLong(3000, 6000))
            }
        }.catch {
            Timber.e(it)
        }.onEach {
            Timber.i(it.toString())
            it.uuid?.let { uuid ->
                messageDatabaseDao.get(uuid)?.let { entity ->
                    entity.sentTimeMilli = System.currentTimeMillis()
                    messageDatabaseDao.update(entity)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun stopPush() {
        pushJob?.cancel()
    }

    fun updateMessage(notificationMessage: NotificationMessage) {
        Timber.i(notificationMessage.toString())
        viewModelScope.launch {
            messageDatabaseDao.get(notificationMessage.notificationContent)?.let { entity ->
                if (entity.receiveTimeMilli == null) {
                    entity.messageId = notificationMessage.msgId
                    entity.receiveTimeMilli = System.currentTimeMillis()
                    entity.costTime = "${(entity.sentTimeMilli?:0) - entity.sendTimeMilli} ms | ${(entity.receiveTimeMilli!! - entity.sendTimeMilli) / 1000} s"
                    messageDatabaseDao.update(entity)
                }
            }
        }
    }
}