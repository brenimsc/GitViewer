package com.breno.gitviewer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.breno.gitviewer.databinding.NavBarBinding

class NavBarCustomized @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: NavBarBinding

    internal val title: TextView
        get() = binding.titleNavBar
    internal val iconStart: ImageView
        get() = binding.iconStartNavBar
    internal val iconEnd: ImageView
        get() = binding.iconEndNavBar

    var type: TypeNavBar = TypeNavBar.BACK_TITLE_CLOSE
        set(value) {
            field = value
            attComponent()
        }

    init {
        bindView()
        setLayout(attrs)
    }

    private fun attComponent() {
        when (type) {
            TypeNavBar.BACK_TITLE_CLOSE -> setVisibilityComponent(VISIBLE, VISIBLE, VISIBLE)
            TypeNavBar.BACK_CLOSE -> setVisibilityComponent(VISIBLE, VISIBLE, GONE)
            TypeNavBar.BACK_TITLE -> setVisibilityComponent(VISIBLE, GONE, VISIBLE)
            TypeNavBar.CLOSE_TITLE -> setVisibilityComponent(GONE, VISIBLE, VISIBLE)
            TypeNavBar.TITLE -> setVisibilityComponent(GONE, GONE, VISIBLE)
        }
    }

    private fun setVisibilityComponent(
        iconStartVisibility: Int,
        iconEndVisibility: Int,
        titleVisibility: Int
    ) {
        iconStart.visibility = iconStartVisibility
        iconEnd.visibility = iconEndVisibility
        title.visibility = titleVisibility
    }


    enum class TypeNavBar(val value: Int) {
        TITLE(0),
        BACK_TITLE_CLOSE(1),
        BACK_TITLE(2),
        CLOSE_TITLE(3),
        BACK_CLOSE(4);

        internal companion object {
            internal fun valueOf(id: Int): TypeNavBar {
                for (f in values()) {
                    if (f.value == id) return f
                }
                throw IllegalArgumentException()
            }
        }
    }

    private fun bindView() {
        binding = NavBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.NavBarCustomized
            )

            type = TypeNavBar.valueOf(
                attributes.getInt(
                    R.styleable.NavBarCustomized_navBarType,
                    TypeNavBar.BACK_TITLE_CLOSE.value
                )
            )

            setTitleNavBar(attributes.getString(R.styleable.NavBarCustomized_titleNavBar).orEmpty())

            setDrawableStart(
                attributes.getResourceId(
                    R.styleable.NavBarCustomized_drawableStart,
                    R.drawable.ic_arrow_left
                )
            )

            setIconEnd(
                attributes.getResourceId(
                    R.styleable.NavBarCustomized_drawableStart,
                    R.drawable.ic_close
                )
            )
            attributes.recycle()
        }
    }


}

@BindingAdapter("titleNavBar")
fun NavBarCustomized.setTitleNavBar(name: String? = null) {
    title.text = name
}

@BindingAdapter("drawableStart")
fun NavBarCustomized.setDrawableStart(drawable: Int = R.drawable.ic_arrow_left) {
    iconStart.setImageResource(drawable)
}

@BindingAdapter("iconEnd")
fun NavBarCustomized.setIconEnd(drawable: Int = R.drawable.ic_close) {
    iconEnd.setImageResource(drawable)
}

@BindingAdapter("clickIconEnd")
fun NavBarCustomized.setClickIconEnd(onClickListener: View.OnClickListener? = null) {
    iconEnd.setOnClickListener(onClickListener)
}
