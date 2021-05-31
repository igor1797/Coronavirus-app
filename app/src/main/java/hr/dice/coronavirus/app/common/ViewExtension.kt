package hr.dice.coronavirus.app.common

import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import hr.dice.coronavirus.app.R

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun ImageView.loadImage(imageUrl: String?) {

    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.apply {
        strokeWidth = 5F
        centerRadius = 50F
        start()
    }

    Glide.with(this)
        .load(imageUrl)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.broken_image)
        .into(this)
}
