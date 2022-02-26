package pl.edu.uj.wieliczko.shopapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.edu.uj.wieliczko.shopapplication.databinding.FragmentConfirmAndLoginBinding

class ConfirmAndLoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentConfirmAndLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
}