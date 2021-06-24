package com.yplay.yspending.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yplay.yspending.R
import com.yplay.yspending.data.model.Spend
import com.yplay.yspending.databinding.ItemDayAddBinding
import com.yplay.yspending.databinding.ItemDayDetailBinding
import com.yplay.yspending.manager.ConvertingManager

class DayDetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mListDayDetail = mutableListOf<Spend>()
    private var mListener: ItemSelected? = null
    private var mSelectedList =  mutableListOf<Spend>()
    private var mIsSelectedMode = false
    private var mSelectAllFlag = false

    fun setData(spends: List<Spend>?) {
        spends?.let {
            mListDayDetail.clear()
            mListDayDetail.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun setListener(context: ItemSelected) {
        this.mListener = context
    }

    fun switchToNormal() {
        mIsSelectedMode = false
        mSelectAllFlag = false
        mSelectedList.clear()
        notifyDataSetChanged()
    }

    fun selectAll() {
        mSelectAllFlag = true
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DayDetailViewHolder) {
            Log.d(TAG, "onBindViewHolder DayDetailViewHolder")
            when {
                mIsSelectedMode -> {
                    holder.enableSelectMode(mSelectedList.contains(mListDayDetail[position]))
                    if (mSelectAllFlag) {
                        holder.checkItem(false)
                        mSelectedList.clear()
                        mSelectedList.addAll(mListDayDetail)
                        mListener?.onSelected(mSelectedList)
                    }
                }
                else -> holder.disableSelectMode()
            }

            holder.setData(mListDayDetail[position])
            holder.itemView.setOnClickListener {
                if (mIsSelectedMode) {
                    if (mSelectedList.contains(mListDayDetail[position])) {
                        holder.checkItem(true)
                        mSelectedList.remove(mListDayDetail[position])
                    } else {
                        holder.checkItem(false)
                        mSelectedList.add(mListDayDetail[position])
                    }
                    mListener?.onSelected(mSelectedList)
                } else mListener?.onItemSelected(mListDayDetail[position])
            }

            holder.itemView.setOnLongClickListener {
                if (!mIsSelectedMode) {
                    mSelectedList.clear()
                    mSelectedList.add(mListDayDetail[position])
                    mIsSelectedMode = true
                    mListener?.onSelectMode()
                    mListener?.onSelected(mSelectedList)
                    notifyDataSetChanged()
                }
                true
            }
        }
        else if (holder is DayDetailPlusViewHolder) {
            Log.d(TAG, "onBindViewHolder DayDetailPlusViewHolder")
            if (mIsSelectedMode) {
                holder.itemView.isEnabled = false
            } else {
                holder.itemView.isEnabled = true
                holder.itemView.setOnClickListener {
                    mListener?.onItemSelected()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_DETAIL) {
            Log.d(TAG, "onCreateViewHolder DayDetailViewHolder")
            DayDetailViewHolder(ItemDayDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
        else {
            Log.d(TAG, "onCreateViewHolder DayDetailPlusViewHolder")
            DayDetailPlusViewHolder(ItemDayAddBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount mListDayDetail.size ${mListDayDetail.size}")
        return mListDayDetail.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType position $position")
        if (position != 0 ) return TYPE_DETAIL
        return TYPE_ADD
    }

    inner class DayDetailPlusViewHolder(private val binding: ItemDayAddBinding): RecyclerView.ViewHolder(binding.root)

    inner class DayDetailViewHolder(private val binding: ItemDayDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun setData(spend: Spend) {
            binding.tvThings.text = spend.thingForPay
            binding.tvPrice.text = ConvertingManager.priceFormat(spend.price.toDouble())
            Log.d(TAG, "setData DayDetailViewHolder price ${ConvertingManager.priceFormat(spend.price.toDouble())}")
        }

        fun enableSelectMode(contained: Boolean) {
            binding.ivSelect.visibility = View.VISIBLE
            binding.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
            checkItem(!contained)
        }

        fun disableSelectMode() {
            binding.ivSelect.visibility = View.GONE
        }

        fun checkItem(contained: Boolean) {
            if (contained) {
                binding.ivSelect.setImageResource(R.drawable.ic_radio_button_unchecked)
            } else {
                binding.ivSelect.setImageResource(R.drawable.ic_check_circle)
            }
        }
    }

    interface ItemSelected {
        fun onSelected(spends: List<Spend>?)
        fun onItemSelected(spend: Spend? = null)
        fun onSelectMode()
    }

    companion object {
        const val TYPE_DETAIL = 0
        const val TYPE_ADD = 1
        private val TAG = DayDetailAdapter::class.simpleName
    }

}