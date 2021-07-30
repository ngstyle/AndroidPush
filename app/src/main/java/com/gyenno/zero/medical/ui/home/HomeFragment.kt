package com.gyenno.zero.medical.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.jpush.android.api.JPushInterface
import com.gyenno.zero.medical.AliasLiveData
import com.gyenno.zero.medical.JpushReceiver
import com.gyenno.zero.medical.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // 获取当前设备别名
        binding.btnFetchAlias.setOnClickListener {
            JPushInterface.getAlias(activity, JpushReceiver.SEQUENCE_GET_ALIAS)
            homeViewModel.setLoading(true)
        }

        // 更新别名
        binding.btnUpdateAlias.setOnClickListener {
            val alias = binding.editAlias.text.toString()
            if (alias.isNotBlank()) {
                JPushInterface.setAlias(activity, JpushReceiver.SEQUENCE_SET_ALIAS, alias)
                homeViewModel.setLoading(true)
            } else {
                Toast.makeText(activity, "别名不能为空", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegistrationId.text = "RegistrationID：${JPushInterface.getRegistrationID(activity) ?: "暂未获取到RegistrationID"}"

        AliasLiveData.observe(viewLifecycleOwner) {
            Timber.d(it.toString())
            if (it.errorCode != 0) {
                Toast.makeText(activity, "errorCode = ${it.errorCode}, Retry please!", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvAlias.text = it.alias
            }
            homeViewModel.setLoading(false)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}