package hr.dice.coronavirus.app.ui.databinding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import hr.dice.coronavirus.app.common.gone
import hr.dice.coronavirus.app.common.loadImage
import hr.dice.coronavirus.app.common.visible
import hr.dice.coronavirus.app.ui.base.EmptyState
import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState

object BindingAdapter {

    @BindingAdapter("app:errorState")
    @JvmStatic
    fun View.setUiStateForError(viewState: ViewState?) {
        when (viewState) {
            is Error -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:successState")
    @JvmStatic
    fun View.setUiStateForSuccess(viewState: ViewState?) {
        when (viewState) {
            is Success<*> -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:loadingState")
    @JvmStatic
    fun View.setUiStateForLoading(viewState: ViewState?) {
        when (viewState) {
            is Loading -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:noInternetConnectionState")
    @JvmStatic
    fun View.setUiStateForNoInternetConnection(viewState: ViewState?) {
        when (viewState) {
            is NoInternetState -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:emptyState")
    @JvmStatic
    fun View.setUiStateForEmpty(viewState: ViewState?) {
        when (viewState) {
            is EmptyState -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:isVisible")
    @JvmStatic
    fun View.setVisibility(isVisible: Boolean?) {
        when (isVisible) {
            true -> visible()
            else -> gone()
        }
    }

    @BindingAdapter("app:visibilityByNumber")
    @JvmStatic
    fun View.setVisibilityByNumber(increasing: Int) {
        when (increasing) {
            -1 -> {
                visible()
            }
            0 -> {
                gone()
            }
            1 -> {
                visible()
            }
        }
    }

    @BindingAdapter("app:graphDirectionByNumber")
    @JvmStatic
    fun View.setGraphDirectionByNumber(increasing: Int) {
        when (increasing) {
            -1 -> {
                rotation = 40F
            }
            0 -> {
                rotation = 20F
            }
            1 -> {
                rotation = 0F
            }
        }
    }

    @BindingAdapter("app:arrowDirectionByNumber")
    @JvmStatic
    fun View.setArrowDirectionByNumber(increasing: Int) {
        if (increasing == -1) rotation = 180F
    }

    @BindingAdapter("app:imagePath")
    @JvmStatic
    fun ImageView.loadImageUrl(imagePath: String?) {
        loadImage(imagePath)
    }
}
