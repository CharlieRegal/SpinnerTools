package xyz.mattjashworth.spinnertools.sheet

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import xyz.mattjashworth.spinnertools.R
import xyz.mattjashworth.spinnertools.sheet.adapters.SearchSpinnerAdapter

class Spinner<T>(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private lateinit var selectedItem: EditText
    private var card: CardView
    private var items = ArrayList<T>()

    private var selectedObject: T? = null

    private var displayMember: String? = null
    private var title = "Select Item"
    private var searchable = false
    private var dismissWhenSelected = false

    private lateinit var textInputLayout: TextInputLayout

    private var type: Any? = null

    private var onItemSelectedListener: OnItemSelectedListener<T>? = null

    init {

        val root = inflate(context, R.layout.spinner, this)

        val ta: TypedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Spinner)
        displayMember = ta.getString(R.styleable.Spinner_DisplayMember) ?: ""
        title = ta.getString(R.styleable.Spinner_Title) ?: ""
        searchable = ta.getBoolean(R.styleable.Spinner_Searchable, false)
        dismissWhenSelected = ta.getBoolean(R.styleable.Spinner_DismissWhenSelected, false)
        ta.recycle()



        selectedItem = findViewById<EditText>(R.id.tv_spinner_selected)
        textInputLayout = findViewById<TextInputLayout>(R.id.tIL)
        card = findViewById(R.id.card_spinner)
        textInputLayout.hint = title

        setChildListener(rootView, OnClickListener {
            val s = SpinnerSheet<T>(context, items, title, displayMember, searchable)
            if (selectedObject != null)
                s.setSelectedObject(selectedObject!!)
            s.setOnItemClickListener(object : SpinnerSheet.OnSearchSpinnerClickListener<T> {
                override fun onClick(position: Int, model: T) {

                    if (dismissWhenSelected)
                        s.dismiss()

                    val gson = Gson()
                    val jsonStr = gson.toJson(model)

                    if (model is String) {

                        selectedItem.setText(model)

                    } else {

                        val obj = JsonParser.parseString(jsonStr).asJsonObject

                        val map = obj.asMap()
                        val keys = map.keys

                        var res = ""

                        if (!displayMember.isNullOrEmpty()) res =
                            obj.get(displayMember).asString
                        else res = obj.get(keys.max()).asString

                        selectedObject = model
                        selectedItem.setText(res)

                    }

                    onItemSelectedListener?.onItemSelected(model)
                }

            })
        })


    }

    fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener<T>) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    interface OnItemSelectedListener<T> {
        fun onItemSelected(model: T)
    }

    private fun setChildListener(parent: View, listener: OnClickListener) {
        parent.setOnClickListener(listener)
        if (parent !is ViewGroup) {
            return
        }

        val parentGroup = parent
        for (i in 0 until parentGroup.childCount) {
            setChildListener(parentGroup.getChildAt(i), listener)
        }
    }

    fun setItems(_items: ArrayList<T>) {
        items = _items
    }

    fun setDisplayMember(id: String) {
        displayMember = id
    }

    fun setTitle(title: String) {
        this.title = title
    }

}