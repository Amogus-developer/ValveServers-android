package com.example.tf2servers.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tf2servers.R
import com.example.tf2servers.adapters.ListAdapter
import com.example.tf2servers.databinding.FragmentListBinding
import com.example.tf2servers.models.DataModel
import com.example.tf2servers.models.ServerData
import com.example.tf2servers.models.ServerInfo
import com.example.tf2servers.udp.SourceEngineClient
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.util.concurrent.Executors


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    private val executor = Executors.newSingleThreadExecutor()
    private val mainExecutor = Handler(Looper.getMainLooper())
    private val objectMapper = ObjectMapper()

    private val dataModel: DataModel by activityViewModels()
    private val client: SourceEngineClient = SourceEngineClient()
    private val adapter = ListAdapter(this@ListFragment)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        adapter.clear()
        try {
            objectMapper.readValue(File(requireContext().filesDir, "server.json"),
                Array<ServerData>::class.java).forEach {setInfo(it.address, it.port)}
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter
    }
    private fun setInfo(address: String, port: Int){
        executor.execute {
                val info = client.getInfo(address, port)
                mainExecutor.post {
                    adapter.addInfo(info)
            }
        }
    }
    fun deleteListener(listInfo: ArrayList<ServerInfo>) {
        val array = Array(listInfo.size) {
            val info = listInfo[it]
            ServerData(info.address, info.port)
        }
        objectMapper.writeValue(File(requireContext().filesDir, "server.json"), array)
    }
    fun alertListener(info: ServerInfo){
        dataModel.info.value = info
        findNavController().navigate(R.id.action_listFragment_to_alertFragment)
    }
    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}