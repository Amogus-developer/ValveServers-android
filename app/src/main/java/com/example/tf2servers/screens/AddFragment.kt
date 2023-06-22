package com.example.tf2servers.screens

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tf2servers.databinding.FragmentAddBinding
import com.example.tf2servers.models.ServerData
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileNotFoundException

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private val objectMapper = ObjectMapper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btAdd.setOnClickListener {
                    if (!TextUtils.isEmpty(binding.edAddress.text) && !TextUtils.isEmpty(binding.edPort.text)) {
                        Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show()
                        addServer()
                    } else {
                        Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                    } }

    }

    private fun addServer(){
        val data = ServerData(binding.edAddress.text.toString(), binding.edPort.text.toString().toInt())
        val file = File(requireContext().filesDir, "server.json")
        try {
            val array = objectMapper.readValue(File(requireContext().filesDir, "server.json"), Array<ServerData>::class.java)
            objectMapper.writeValue(file, array.plus(data))
        } catch (e: Throwable) {
            val array = arrayOf(ServerData(data.address, data.port))
            objectMapper.writeValue(file, array)
        }
    }
}