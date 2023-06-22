package com.example.tf2servers.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.tf2servers.databinding.FragmentAlertBinding
import com.example.tf2servers.models.DataModel
import com.example.tf2servers.service.MyIntentService


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private val dataModel: DataModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btAdd.setOnClickListener {
            if (!TextUtils.isEmpty(binding.players.text)) {
                dataModel.info.observe(activity as LifecycleOwner, {
                    val intentMyIntentService =
                        Intent(requireContext(), MyIntentService::class.java).putExtra("time", 10_500).putExtra("players", binding.players.text.toString().toInt()).putExtra("address", it.address).putExtra("port", it.port)
                    requireContext().startService(intentMyIntentService)
                })
            }
            else{
                Toast.makeText(requireContext(), "Заполните поле", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val openLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
        try {
            uri?.let { print(it) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}