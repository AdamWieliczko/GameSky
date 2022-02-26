package pl.edu.uj.wieliczko.shopapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.edu.uj.wieliczko.shopapplication.databinding.FragmentCreatorPhotoBinding

class CreatorPhotoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentCreatorPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }
}