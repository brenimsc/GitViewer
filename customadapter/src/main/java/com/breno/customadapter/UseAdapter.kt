package com.breno.customadapter

import androidx.annotation.LayoutRes

data class UseAdapter<T>(
    @LayoutRes val itemLayoutRes: Int,
    val itemVardId: Int,
    override var items: List<T> = arrayListOf()
) : Adapter<T>(itemLayoutRes, itemVardId, items) {

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val item = getItemForPosition(position)
        holder.binding.apply {
            setVariable(getItemVarId(position), item)
        }
    }

    companion object {
        fun <T> getAdapter(
            @LayoutRes itemLayoutRes: Int,
            itemVarId: Int,
            items: List<T> = arrayListOf(),
            onItemClickCallback: ((position: Int, item: T) -> Unit)? = null
        ): UseAdapter<T> {
            return UseAdapter(itemLayoutRes, itemVarId, items).also {
                it.setOnItemClickListener { position, item ->
                    onItemClickCallback?.invoke(position, item)
                }
            }
        }
    }
}