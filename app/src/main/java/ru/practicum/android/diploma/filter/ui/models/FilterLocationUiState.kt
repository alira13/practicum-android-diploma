package ru.practicum.android.diploma.filter.ui.models

import ru.practicum.android.diploma.R

data class FilterLocationUiState(
    val item1: FilterItem,
    val item2: FilterItem,
    val approveButtonVisibility: Boolean
)

sealed class FilterItem(
    open val endIconRes: Int = R.drawable.ic_arrow_forward,
    open val hintTextAppearance: Int = R.style.Regular16GrayTextStyle,
    open val itemName: String? = null
) {
    data object Absent: FilterItem()
    data class Present(
        override val endIconRes: Int = R.drawable.ic_close,
        override val hintTextAppearance: Int = R.style.SmallTextStyle,
        override val itemName: String?
    ): FilterItem()
}
