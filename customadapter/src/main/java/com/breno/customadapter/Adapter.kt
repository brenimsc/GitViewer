package com.breno.customadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class Adapter<T>(
    @LayoutRes val itemLayout: Int,
    private val itemVarId: Int,
    open var items: List<T> = arrayListOf()
) : RecyclerView.Adapter<GenericViewHolder>() {

    var callback: ((position: Int, item: T) -> Unit)? = null

    fun setOnItemClickListener(callback: (position: Int, item: T) -> Unit) {
        this.callback = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )

        return GenericViewHolder(binding).also { viewHolder ->
            binding.root.apply {
                callback?.let {
                    setOnClickListener {
                        callback?.invoke(
                            viewHolder.adapterPosition,
                            items[viewHolder.adapterPosition]
                        )
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItemForPosition(position).run {
            when {
                itemLayout != 0 -> itemLayout
                else -> throw IllegalArgumentException("Informe explicitamente o id do layout no construtor")
            }
        }
    }

    override fun getItemCount() = items.size

    protected fun getItemVarId(position: Int): Int {
        return getItemForPosition(position).run {
            when {
                itemVarId != 0 -> itemVarId
                else -> throw IllegalArgumentException("Informe explicitamente o id da variavel no construtor")
            }
        }
    }

    protected fun getItemForPosition(position: Int) = items[position]

    fun addAll(items: List<T>) {
        val startPosition = itemCount

        this.items = ArrayList(this.items)
            .apply { addAll(items) }

        notifyItemRangeInserted(startPosition, items.size)
    }

    fun addItem(item: T) {
        items = ArrayList<T>(items)
            .apply { add(item) }

        notifyItemInserted(itemCount - 1)
    }

    fun addItem(index: Int, item: T) {
        items = ArrayList<T>(items)
            .apply { add(index, item) }

        notifyItemInserted(index)
    }

    fun removeItem(item: T) {
        removeItem(items.indexOfFirst { it == item })
    }

    fun removeItem(position: Int) {
        position.takeIf { it in 0 until itemCount }
            ?.let {
                items = ArrayList<T>(items)
                    .apply { removeAt(it) }

                notifyItemRemoved(it)
            }
    }

    fun clear() {
        items = arrayListOf()
        notifyDataSetChanged()
    }

    fun clearAndAddAll(items: List<T>) {
        clear()
        addAll(items)
    }


}