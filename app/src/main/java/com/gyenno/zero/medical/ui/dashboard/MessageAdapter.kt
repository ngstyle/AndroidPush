package com.gyenno.zero.medical.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gyenno.zero.medical.database.MessageRecorder
import com.gyenno.zero.medical.databinding.ListItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
class MessageAdapter :
    ListAdapter<MessageRecorder, MessageAdapter.ViewHolder>(MessageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @SuppressLint("SimpleDateFormat")
    class ViewHolder private constructor(private val binding: ListItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val timeFormat1 = SimpleDateFormat("HH:mm:ss.SSS")
        val timeFormat2 = SimpleDateFormat("mm:ss.SSS")

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                return ViewHolder(ListItemMessageBinding.inflate(inflater, parent, false))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: MessageRecorder) {
            binding.tvOrder.text = item.recorderId.toString()
            if (item.sentTimeMilli == null)
                binding.tvSendTime.text = "-"
            else
                binding.tvSendTime.text = "${timeFormat1.format(Date(item.sendTimeMilli))}/${timeFormat2.format(Date(item.sentTimeMilli!!))}"

            if (item.receiveTimeMilli == null) {
                binding.tvReceiveTime.text = "-"
                binding.tvCostTime.text = "-"
            } else {
                binding.tvReceiveTime.text = timeFormat2.format(Date(item.receiveTimeMilli!!))
                val time1 = item.sentTimeMilli!! - item.sendTimeMilli
                val time2 = item.receiveTimeMilli!! - item.sendTimeMilli

                if (time1 > 3000 || time2 > 3000)
                    binding.tvCostTime.setTextColor(Color.parseColor("#D32F2F"))
                else
                    binding.tvCostTime.setTextColor(Color.DKGRAY)
                binding.tvCostTime.text = "$time1/$time2"
            }

        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<MessageRecorder>() {
    override fun areItemsTheSame(oldItem: MessageRecorder, newItem: MessageRecorder): Boolean {
        return oldItem.recorderId == newItem.recorderId
    }

    override fun areContentsTheSame(oldItem: MessageRecorder, newItem: MessageRecorder): Boolean {
        return oldItem == newItem
    }
}
