package pl.edu.uj.wieliczko.shopapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.edu.uj.wieliczko.shopapplication.databinding.FragmentInsertPasswordBinding

class InsertPasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentInsertPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
}