package org.finalware.tymer.ui.preset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import org.finalware.tymer.R
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.databinding.FragmentPresetBinding
import org.finalware.tymer.model.Preset
import org.finalware.tymer.network.ApiStatus

class FragmentPreset: Fragment(), PresetAdapter.ViewHolderListener {
    private lateinit var binding: FragmentPresetBinding

    private val viewModel: PresetViewModel by lazy {
        val db = PresetDB.getDatabase(requireContext())
        PresetViewModel(db)
    }

    private lateinit var myAdapter: PresetAdapter

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

        viewModel.getPresets()

        viewModel.getUserPresets().observe(viewLifecycleOwner) {
            myAdapter.updateUserPresets(it)
        }

        viewModel.getServerPresets().observe(viewLifecycleOwner) {
            myAdapter.updatePresets(it)
        }

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }

        myAdapter = PresetAdapter()
        myAdapter.viewHolderListener = this

        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = myAdapter
            setHasFixedSize(true)
        }
    }

    override fun deletePreset(preset: Preset) {
        val succeeded = viewModel.deletePreset(preset)

        if (succeeded) {
            // update the list
            viewModel.getPresets()

            // show toast
            Toast.makeText(context, "Preset deleted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Failed to delete preset", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }
}