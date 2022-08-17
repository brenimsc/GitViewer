package com.breno.gitviewer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.breno.gitviewer.databinding.QtdDescriptionBinding

class CountDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: QtdDescriptionBinding

    internal val description: TextView
        get() = binding.descriptionItem

    internal val qtd: TextView
        get() = binding.qtdItem


    init {
        bindViews()
        setLayout(attrs)
    }

    private fun bindViews() {
        binding = QtdDescriptionBinding.inflate(LayoutInflater.from(context), this, true)
    }

//    private fun setText() {
//        binding.descriptionItem.text = description
////        binding.qtdItem.apply {
////            text = qtd
////            if (qtd?.toInt() == 0) setTextColor(ContextCompat.getColor(context, R.color.gray_light))
////        }
//    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.CountDescription
            )

           attributes.getResourceId(R.styleable.CountDescription_qtd, 0).takeIf { it != 0 }?.run {
               context.getString(this)
           }?.apply {
               setQtd(this)
           }

            setDescription(attributes.getString(R.styleable.CountDescription_description).orEmpty())



            attributes.recycle()


        }
    }

//    @BindingAdapter("qtd")
//    fun CountDescription.qtd(valor: String = "") {
//        qtd = valor
//        binding.qtdItem.apply {
//            text = qtd
//            if (qtd?.toInt() == 0) setTextColor(ContextCompat.getColor(context, R.color.gray_light))
//        }
//    }


}

@BindingAdapter("qtd")
fun CountDescription.setQtd(valor: String = "") {
    qtd.apply {
        text = valor
        valor.toInt().takeIf {
            it == 0
        }?.run {
            setTextColor(ContextCompat.getColor(context, R.color.gray_light))
        }
    }
}

@BindingAdapter("description")
fun CountDescription.setDescription(name: String = "") {
    description.apply {
        text = name
    }
}
