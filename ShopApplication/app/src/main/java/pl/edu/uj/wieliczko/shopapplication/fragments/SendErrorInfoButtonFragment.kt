package pl.edu.uj.wieliczko.shopapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.edu.uj.wieliczko.shopapplication.databinding.FragmentSendErrorInfoButtonBinding

class SendErrorInfoButtonFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentSendErrorInfoButtonBinding.inflate(inflater, container, false)
        return binding.root
    }
}