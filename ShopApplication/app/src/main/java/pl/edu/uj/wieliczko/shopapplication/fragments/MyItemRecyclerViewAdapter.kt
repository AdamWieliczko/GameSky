package pl.edu.uj.wieliczko.shopapplication.fragments

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import pl.edu.uj.wieliczko.shopapplication.databinding.FragmentItemBinding
import pl.edu.uj.wieliczko.shopapplication.realmModels.ProductRealm
import java.math.BigDecimal

class MyItemRecyclerViewAdapter(
    private val values: List<ProductRealm>, private val context: Context
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        
        return ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name + ": " + item.description
        holder.contentView.text = (BigDecimal(item.price).divide(BigDecimal(100))).toString() + " zł"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}