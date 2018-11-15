package com.example.margarita.scievent

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView

/* Android components extensions */

/**
 * Fragment transactions.
 */
fun FragmentManager.transaction(transaction: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().transaction().commit()
}

/**
 * View group inflation.
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, parent: ViewGroup = this, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, parent, attachToRoot)
}

fun ImageView.setColor(@ColorRes colorRes: Int) {
    setColorFilter(ContextCompat.getColor(context, colorRes))
}

/**
 * Convert dp po px
 */
fun Context.dpToPx(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)

/**
 * Converts sp to px
 */
fun Context.spToPx(sp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), resources.displayMetrics)

/**
 * Convert drawable to bitmap
 */
fun Drawable.toBitmap(): Bitmap {
    val width = if (intrinsicWidth > 0) intrinsicWidth else 1
    val height = if (intrinsicHeight > 0) intrinsicHeight else 1
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
        val canvas = Canvas(it)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}

/**
 * Returns color from ContextCompat
 *
 * @param res color resource idEventWithSectionPosition
 * @return color
 */
fun Context.getCompatColor(@ColorRes res: Int) = ContextCompat.getColor(this, res)

/**
 * Returns drawable from ContextCompat
 *
 * @param res drawable resource idEventWithSectionPosition
 * @return drawable
 */
fun Context.getCompatDrawable(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)

/**
 * Returns drawable from ContextCompat with color
 *
 * @param drawableRes drawable resource idEventWithSectionPosition
 * @param color color of icon
 * @return drawable
 */
fun Context.getCompatDrawable(@DrawableRes drawableRes: Int, @ColorInt color: Int): Drawable {
    val drawable = ContextCompat.getDrawable(this, drawableRes)
    drawable!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    return drawable
}

/**
 * Convert drawables list to bitmap (with overlay)
 */
fun List<Drawable>.toBitmapWithOverlay(): Bitmap {
    val firstWidth = first().intrinsicWidth
    val width = maxOf(sumBy { it.intrinsicWidth / 2 } + firstWidth / 2, 1)
    val height = map { it.intrinsicHeight }.max()

    return Bitmap.createBitmap(width + 2, height ?: 1 + 2, Bitmap.Config.ARGB_8888).also {
        val canvas = Canvas(it)
        var offset = width - firstWidth
        asReversed().forEach {
            val end = offset + it.intrinsicWidth
            it.setBounds(offset, 0, end, canvas.height)
            it.draw(canvas)
            offset -= it.intrinsicWidth / 2
        }
    }
}

/**
 * Hides virtual keyboard
 *
 * @param view view with focus
 */
fun Context.hideKeyboard(view: View?) = view?.let {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(it.windowToken, 0)
}

/**
 * Shows virtual keyboard
 *
 * @param view view with focus
 */
fun Context.showKeyboard(view: View) {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(view, 0)
}

/**
 * Sets visibility of view to [View.VISIBLE]
 */
fun View.visible() { visibility = View.VISIBLE }

/**
 * Sets visibility of view to [View.INVISIBLE]
 */
fun View.invisible() { visibility= View.INVISIBLE }

/**
 * Sets visibility of view to [View.GONE]
 */
fun View.gone() { visibility = View.GONE }