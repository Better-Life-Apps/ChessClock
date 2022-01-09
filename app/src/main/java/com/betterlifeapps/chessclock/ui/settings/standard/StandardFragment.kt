package com.betterlifeapps.chessclock.ui.settings.standard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.betterlifeapps.chessclock.R

class StandardFragment : Fragment() {

    companion object {
        fun newInstance() = StandardFragment()
    }

    private lateinit var viewModel: StandardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_standard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StandardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}