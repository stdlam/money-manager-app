package com.yplay.yspending.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.databinding.ItemMonthSummaryBinding
import com.yplay.yspending.manager.ConvertingManager

class SpendingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mGroupedList = mapOf<Int, List<Spend>>()
    private var mListDay = mutableSetOf<Int>()
    private var mCurrentMonth : Int? = null
    private var mListener: ClickEvent? = null
    private var mIsSelectMode = false

    fun setData(spends: List<Spend>, currentMonth: Int) {
        mCurrentMonth = currentMonth
        mGroupedList = spends.groupBy { it.day }
        this.mListDay.clear()
        this.mListDay.addAll(mGroupedList.keys)
        notifyDataSetChanged()
    }

    fun setListener(listener: ClickEvent) {
        this.mListener = listener
    }

    fun returnNormalMode() {
        this.mIsSelectMode = false
        notifyDataSetChanged()
    }

    fun isSelectMode() = mIsSelectMode

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val priceSummary = mGroupedList[this.mListDay.elementAt(position)]?.sumByDouble { it.price.toDouble() }
        (holder as SpendStickyViewHolder).setData(this.mListDay.elementAt(position), priceSummary)
        val daySpend = mGroupedList[this.mListDay.elementAt(position)]?.get(0)

        holder.itemView.setOnClickListener {
            daySpend?.let {
                mListener?.onClick(it.month, it.day, it.year)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SpendStickyViewHolder(ItemMonthSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return this.mListDay.size
    }

    inner class SpendStickyViewHolder(private val binding: ItemMonthSummaryBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(day: Int, sumDay: Double?) {
            if (mCurrentMonth!! < 9) {
                if (day < 10) {
                    binding.tvDate.text = "0${mCurrentMonth!! + 1}/0$day"
                }
                else {
                    binding.tvDate.text = "0${mCurrentMonth!! + 1}/$day"
                }
            }
            else {
                if (day < 10) {
                    binding.tvDate.text = "${mCurrentMonth!! + 1}/0$day"
                }
                else {
                    binding.tvDate.text = "${mCurrentMonth!! + 1}/$day"
                }
            }
            binding.tvSummaryPrice.text = ConvertingManager.priceFormat(sumDay)
        }
    }

    interface ClickEvent {
        fun onClick(month: Int, day: Int, year: Int)
    }
}