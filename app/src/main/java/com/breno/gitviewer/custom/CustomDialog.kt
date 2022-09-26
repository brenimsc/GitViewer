package com.breno.gitviewer.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.breno.gitviewer.databinding.CustomDialogLoadingBinding

class CustomDialog @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: CustomDialogLoadingBinding

    init {
        binding = CustomDialogLoadingBinding.inflate(LayoutInflater.from(context), this, true)
    }

}

@BindingAdapter("loading")
fun CustomDialog.setVisible(visible: Boolean) {
    this.isVisible = visible
}