package com.betterlifeapps.chessclock.ui.settings.custom

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.betterlifeapps.chessclock.R

class CustomFragment : Fragment() {

    companion object {
        fun newInstance() = CustomFragment()
    }

    private lateinit var viewModel: CustomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomViewModel::class.java)
        // TODO: Use the ViewModel
    }

}