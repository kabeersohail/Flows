package com.example.flows.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flows.R
import com.example.flows.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {

    lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLaunchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}