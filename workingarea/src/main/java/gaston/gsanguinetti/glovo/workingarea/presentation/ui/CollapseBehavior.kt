package gaston.gsanguinetti.glovo.workingarea.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CollapseBehavior<V : ViewGroup>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    var onCollapsing :((dy :Float) -> Unit)? = null

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (isBottomSheet(dependency)) {
            val behavior = (dependency.layoutParams as CoordinatorLayout.LayoutParams).behavior as BottomSheetBehavior<*>

            val peekHeight = behavior.peekHeight

            val actualPeek = if (peekHeight >= 0) peekHeight else (parent.height * 1.0 / 16.0 * 9.0).toInt()

            if (dependency.top >= actualPeek) {
                val dy = dependency.top - parent.height
                with((dy / 2).toFloat()) {
                    child.translationY = this
                    onCollapsing?.invoke(this)
                }
                return true
            }
        }

        return false
    }

    private fun isBottomSheet(@NonNull view: View): Boolean {
        val layoutParams = view.layoutParams
        return if (layoutParams is CoordinatorLayout.LayoutParams) {
            layoutParams.behavior is BottomSheetBehavior<*>
        } else false
    }

    companion object {
        fun <V : View> from(view: V): CollapseBehavior<*> {
            val params = view.layoutParams
            if (params !is androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) {
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            } else {
                val behavior = params.behavior
                return behavior as? CollapseBehavior<*>
                    ?: throw IllegalArgumentException("The view is not associated with BottomSheetBehavior")
            }
        }
    }
}