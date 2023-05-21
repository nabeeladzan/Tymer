package org.finalware.tymer.ui.preset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import org.finalware.tymer.R
import org.finalware.tymer.databinding.FragmentPresetBinding

class FragmentPreset: Fragment() {
    private lateinit var binding: FragmentPresetBinding

    private val viewModel: PresetViewModel by lazy {
        PresetViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPresetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentPreset_to_fragmentCreate)
        }

        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = PresetAdapter(viewModel.getPresets())
            setHasFixedSize(true)
        }
    }
}