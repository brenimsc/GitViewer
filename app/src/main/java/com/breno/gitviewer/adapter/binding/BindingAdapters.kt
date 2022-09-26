package com.breno.gitviewer.adapter.binding

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.breno.customadapter.UseAdapter
import com.breno.gitviewer.helper.HelperEvents
import com.bumptech.glide.Glide
import com.faltenreich.skeletonlayout.applySkeleton



private const val LIST_ITEM_COUNT_DEFAULT_SKELETON = 3

/* ImageView */

@BindingAdapter("loadImage")
fun ImageView.loadImage(url: String?) {
    Glide.with(this).load(url)
        .into(this)
}

@BindingAdapter("imageInt")
fun ImageView.setImageInt(drawableInt: Int?) {
    drawableInt?.takeIf { it != 0 }?.run {
        this@setImageInt.setImageDrawable(ContextCompat.getDrawable(context, drawableInt))
    }
}

/* ImageView Region End */

/* RecyclerView */

@BindingAdapter("adapter", "skeleton", "qtdSkeleton", "empty", requireAll = false)
fun <T> RecyclerView.adapter(
    adapter: UseAdapter<T>? = null,
    showSkeleton: Boolean? = null,
    qtdSkeleton: Int? = null,
    empty: Boolean? = null
) {
    showSkeleton?.takeIf { it }?.run {
        empty?.takeUnless { it }?.run {
            applySkeleton(adapter?.itemLayoutRes ?: return, itemCount = qtdSkeleton ?: LIST_ITEM_COUNT_DEFAULT_SKELETON).showSkeleton()
        } ?: applySkeleton(adapter?.itemLayoutRes ?: return, itemCount = qtdSkeleton ?: LIST_ITEM_COUNT_DEFAULT_SKELETON).showSkeleton()

    } ?: adapter?.run {
        this@adapter.adapter = adapter
    }
}

/* RecyclerView Region End */

/* ViewPager */

@BindingAdapter("adapter")
fun ViewPager2.adapter(fragmentAdapter: FragmentStateAdapter) {
    fragmentAdapter.run {
        adapter = fragmentAdapter
    }
}

@BindingAdapter("currentItem")
fun ViewPager2.setCurrentItem(currentItem: Int) {
    this.currentItem = currentItem
}

/* ViewPager Region End */

/* CardView */

@BindingAdapter("color")
fun CardView.setColor(color: Int) {
    this.setCardBackgroundColor(ContextCompat.getColor(context, color))
}

@BindingAdapter("languageColor")
fun CardView.setLanguageColor(language: String?) {
    language?.isNotEmpty()?.run {
        HelperEvents.returnColor(language).run {
            setCardBackgroundColor(ContextCompat.getColor(context, this))
        }
    }
}

/* CardView Region End */

/* View */

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean?) {
    visible?.let {
        isVisible = visible
    }
}

@BindingAdapter("invisible")
fun View.setInvisible(invisible: Boolean?) {
    invisible?.let {
        isInvisible = invisible
    }
}

/* View Region End */


