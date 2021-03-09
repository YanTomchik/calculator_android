package by.vlfl.lab2

import androidx.recyclerview.widget.RecyclerView
import by.vlfl.lab2.databinding.HistoryItemBinding

class HistoryItemViewHolder(val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(numberText: String) {
        binding.historyItemValue.text = numberText
    }
}