package com.gyenno.zero.medical.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gyenno.zero.medical.MiPushCommandMessageLiveData
import com.gyenno.zero.medical.R
import com.gyenno.zero.medical.databinding.FragmentHomeBinding
import com.gyenno.zero.medical.util.Manufacturer
import com.huawei.hms.aaid.HmsInstanceId
import com.xiaomi.mipush.sdk.MiPushClient
import kotlinx.coroutines.*
import timber.log.Timber

class HomeFragment : Fragment(), CoroutineScope by MainScope() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        when (Manufacturer.currentBrand) {
            Manufacturer.XIAOMI -> {
                binding.tvAliasLabel.text = "小米"
                binding.tvRegistrationId.text = MiPushClient.getRegId(activity)
            }
            Manufacturer.HUAWEI -> {
                binding.tvAliasLabel.text = "华为"
                launch {
                    try {
                        var token: String
                        withContext(Dispatchers.IO) {
                            token = HmsInstanceId.getInstance(activity).getToken("104573249", "HCM")
                        }
                        binding.tvRegistrationId.text = token
                        Timber.e("HW RegId:%s", token)
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
            }
        }

        if (Manufacturer.currentBrand == Manufacturer.XIAOMI) {
            // 获取别名
            binding.btnFetchAlias.setOnClickListener {
                val allAlias = MiPushClient.getAllAlias(activity)
                Timber.e("Alias: %s", allAlias)
                binding.tvAlias.text = allAlias.joinToString(",")
            }

            // 更新别名 (小米会追加)
            binding.btnUpdateAlias.setOnClickListener {
                val alias = binding.editAlias.text.toString()
                if (alias.isNotBlank()) {
                    MiPushClient.setAlias(activity, alias, null)
                    homeViewModel.setLoading(true)
                } else {
                    Toast.makeText(activity, "别名不能为空", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.tvAlias.visibility = View.GONE
            binding.btnFetchAlias.visibility = View.GONE
            binding.editAlias.visibility = View.GONE
            binding.btnUpdateAlias.visibility = View.GONE
        }

        if (Manufacturer.currentBrand == Manufacturer.XIAOMI) {
            // 删除别名（only 小米）
            binding.btnDelAlias.setOnClickListener {
                val alias = binding.editDeleteAlias.text.toString()
                if (alias.isNotBlank()) {
                    MiPushClient.unsetAlias(activity, alias, null)
                    homeViewModel.setLoading(true)
                } else {
                    Toast.makeText(activity, "别名不能为空", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.btnDelAlias.visibility = View.GONE
            binding.editDeleteAlias.visibility = View.GONE
        }

        MiPushCommandMessageLiveData.observe(viewLifecycleOwner) {
            when (it.command) {
                MiPushClient.COMMAND_SET_ALIAS,
                MiPushClient.COMMAND_UNSET_ALIAS -> {
                    homeViewModel.setLoading(false)
                    binding.tvAlias.text = MiPushClient.getAllAlias(activity).joinToString(",")
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cancel()
    }
}