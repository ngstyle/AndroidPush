package com.gyenno.zero.medical.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gyenno.zero.medical.NotificationMessageLiveData
import com.gyenno.zero.medical.databinding.FragmentDashboardBinding
import timber.log.Timber

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            val count = binding.editTextNumber.text.toString()
            if (count.isNotEmpty())
                dashboardViewModel.pushMessage(count.toInt())
            else
                dashboardViewModel.pushMessage(2)
        }

        binding.btnStop.setOnClickListener {
            dashboardViewModel.stopPush()
        }

        val messageAdapter = MessageAdapter()
        binding.recyclerView.adapter = messageAdapter
        dashboardViewModel.allMessages.observe(viewLifecycleOwner) {
            Timber.i("allMessages: ${it.size}")
            messageAdapter.submitList(it)
            binding.recyclerView.scrollToPosition(it.size - 1)
        }

        dashboardViewModel.current.observe(viewLifecycleOwner) {
            binding.tvCurrent.text = "当前第 $it 次"
        }

        // NotificationMessageLiveData 是单例， 应用退出可能还存活，可再次订阅数据
        NotificationMessageLiveData.observe(viewLifecycleOwner) {
            dashboardViewModel.updateMessage(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}