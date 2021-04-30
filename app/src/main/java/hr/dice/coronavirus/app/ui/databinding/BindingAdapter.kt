package hr.dice.coronavirus.app.ui.databinding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import hr.dice.coronavirus.app.common.gone
import hr.dice.coronavirus.app.common.loadImage
import hr.dice.coronavirus.app.common.visible
import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState

object BindingAdapter {

    @BindingAdapter("app:errorState")
    @JvmStatic
    fun setUiStateForError(view: View, viewState: ViewState?) {
        when (viewState) {
            is Error -> view.visible()
            else -> view.gone()
        }
    }

    @BindingAdapter("app:successState")
    @JvmStatic
    fun setUiStateForSuccess(view: View, viewState: ViewState?) {
        when (viewState) {
            is Success<*> -> view.visible()
            else -> view.gone()
        }
    }

    @BindingAdapter("app:loadingState")
    @JvmStatic
    fun setUiStateForLoading(view: View, viewState: ViewState?) {
        when (viewState) {
            is Loading -> view.visible()
            else -> view.gone()
        }
    }

    @BindingAdapter("app:noInternetConnectionState")
    @JvmStatic
    fun setUiStateForNoInternetConnection(view: View, viewState: ViewState?) {
        when (viewState) {
            is NoInternetState -> view.visible()
            else -> view.gone()
        }
    }

    @BindingAdapter("app:isVisible")
    @JvmStatic
    fun setVisibility(view: View, isVisible: Boolean?) {
        when (isVisible) {
            true -> view.visible()
            else -> view.gone()
        }
    }

    @BindingAdapter("app:visibilityByNumber")
    @JvmStatic
    fun setVisibilityByNumber(view: View, increasing: Int) {
        when (increasing) {
            -1 -> {
                view.visible()
            }
            0 -> {
                view.gone()
            }
            1 -> {
                view.visible()
            }
        }
    }

    @BindingAdapter("app:graphDirectionByNumber")
    @JvmStatic
    fun setGraphDirectionByNumber(view: View, increasing: Int) {
        when (increasing) {
            -1 -> {
                view.rotation = 40F
            }
            0 -> {
                view.rotation = 20F
            }
            1 -> {
                view.rotation = 0F
            }
        }
    }

    @BindingAdapter("app:arrowDirectionByNumber")
    @JvmStatic
    fun setArrowDirectionByNumber(view: View, increasing: Int) {
        if (increasing == -1) view.rotation = 180F
    }

    @BindingAdapter("app:imagePath")
    @JvmStatic
    fun loadImage(imageView: ImageView, imagePath: String?) {
        imageView.loadImage(imagePath)
    }
}
