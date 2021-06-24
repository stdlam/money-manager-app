package com.yplay.yspending.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yplay.yspending.data.model.Suggestion
import com.yplay.yspending.databinding.ItemSuggestBinding

class SuggestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListSuggestion: MutableList<Suggestion> = mutableListOf()
    private var mListener: ClickEvent? = null

    fun setData(suggests: List<Suggestion>) {
        mListSuggestion.clear()
        mListSuggestion.addAll(suggests)
        notifyDataSetChanged()
    }

    fun setListener(listener: ClickEvent) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SuggestViewHolder(ItemSuggestBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mListSuggestion.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SuggestViewHolder).setData(mListSuggestion[position])
        holder.itemView.setOnClickListener { mListener?.onClick(mListSuggestion[position]) }
    }

    inner class SuggestViewHolder(private val binding: ItemSuggestBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(suggest: Suggestion) {
            binding.tvSuggest.text = suggest.suggest
        }
    }

    interface ClickEvent {
        fun onClick(suggest: Suggestion)
    }
}