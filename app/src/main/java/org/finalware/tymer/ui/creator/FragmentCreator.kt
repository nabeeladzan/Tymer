package org.finalware.tymer.ui.creator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.finalware.tymer.R
import org.finalware.tymer.database.PresetDB
import org.finalware.tymer.databinding.FragmentCreatorBinding
import org.finalware.tymer.ui.create.CreateViewModel

class FragmentCreator: Fragment() {
    private lateinit var binding: FragmentCreatorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(binding.profileImage.context)
            .load("https://avatars.githubusercontent.com/u/63929296?v=4")
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(binding.profileImage)
    }
}