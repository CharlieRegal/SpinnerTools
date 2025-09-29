package xyz.mattjashworth.spinnertools.sheet.adapters


import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import xyz.mattjashworth.spinnertools.sheet.Spinner

object SpinnerBindingAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun <T> setItems(spinner: Spinner<T>, items: ArrayList<T>?) {
        if(spinner.itemsBinding != items){
            spinner.setItems(items?: ArrayList())
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "items")
    fun <T> getItems(spinner: Spinner<T>) : ArrayList<T>? {
        return spinner.itemsBinding
    }

    @JvmStatic
    @BindingAdapter("itemsAttrChanged")
    fun <T> setItemsListener(spinner : Spinner<T>, listener : InverseBindingListener?) {
        if(listener != null){
            spinner.itemsValueChanged = {
                listener.onChange()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("selectedObject")
    fun <T> setSelectedItem(spinner: Spinner<T>, item: T) {
        if(spinner.selectedObject != item){
            spinner.setSelectedItem(item )
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedObject")
    fun <T> getSelectedItem(spinner: Spinner<T>) : T? {
        return spinner.selectedObject
    }

    @JvmStatic
    @BindingAdapter("selectedObjectAttrChanged")
    fun <T> setSelectedItemListener(spinner : Spinner<T>, listener : InverseBindingListener?) {
        if (listener != null) {
            spinner.selectedObjectValueChanged = {
                listener.onChange()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("selectedItems")
    fun <T> setSelectedItems(spinner: Spinner<T>, items: List<T>?) {
        if(spinner.selectedObjects != items){
            spinner.setSelectedItem(items?: ArrayList())
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "selectedItems")
    fun <T> getSelectedItems(spinner: Spinner<T>) : List<T>? {
        return spinner.selectedObjects
    }


    @JvmStatic
    @BindingAdapter("selectedItemsAttrChanged")
    fun <T> setSelectedItemsListener(spinner : Spinner<T>, listener : InverseBindingListener?) {
        if(listener != null){
            spinner.selectedObjectsValueChanged = {
                listener.onChange()
            }
        }
    }


}