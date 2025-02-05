package xyz.mattjashworth.spinnertools.sheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import xyz.mattjashworth.spinnertools.R
import xyz.mattjashworth.spinnertools.sheet.adapters.SearchSpinnerAdapter

@SuppressLint("PrivateResource")
class Spinner<T>(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private lateinit var selectedItem: EditText
    private var card: CardView
    private var items = ArrayList<T>()

    private var selectedObject: T? = null

    private var displayMember: String? = null
    private var title = "Select Item"
    private var searchable = false
    private var dismissWhenSelected = false


    //colors
    private var backgroundColor: Int? = null
    private var hintTextColor: Int? = null
    private var textColor: Int? = null

    private  var textInputLayout: TextInputLayout

    private var type: Any? = null

    private var onItemSelectedListener: OnItemSelectedListener<T>? = null

    init {

        val root = inflate(context, R.layout.spinner, this)

        val ta: TypedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Spinner)
        displayMember = ta.getString(R.styleable.Spinner_DisplayMember) ?: ""
        title = ta.getString(R.styleable.Spinner_Title) ?: ""
        searchable = ta.getBoolean(R.styleable.Spinner_Searchable, false)
        dismissWhenSelected = ta.getBoolean(R.styleable.Spinner_DismissWhenSelected, false)
        backgroundColor = ta.getColor(R.styleable.Spinner_backgroundColor, ContextCompat.getColor(context, android.R.color.white))
        hintTextColor = ta.getColor(R.styleable.Spinner_hintTextColor, ContextCompat.getColor(context, android.R.color.black))
        textColor = ta.getColor(R.styleable.Spinner_textColor, ContextCompat.getColor(context, android.R.color.black))

        val hintBottomMargin = ta.getDimension(R.styleable.Spinner_hint_bottomMargin, 0f)

        ta.recycle()


        selectedItem = findViewById<EditText>(R.id.tv_spinner_selected)
        textInputLayout = findViewById<TextInputLayout>(R.id.tIL)
        textInputLayout.addOnEditTextAttachedListener {
            it.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && textInputLayout.isHintAnimationEnabled) {
                        selectedItem.setPadding(0, hintBottomMargin.toInt(), 0, 0)
                    } else {
                        selectedItem.setPadding(0, 0, 0, 0)
                    }
                }

            })
        }
        card = findViewById(R.id.card_spinner)
        card.setCardBackgroundColor(backgroundColor ?: context.getColor(android.R.color.white))
        textInputLayout.hint = title
        textInputLayout.hintTextColor = ColorStateList.valueOf(hintTextColor ?: R.color.gray_600)
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(hintTextColor ?: R.color.gray_600)
        selectedItem.setTextColor(textColor ?: context.getColor(android.R.color.black))

        setChildListener(rootView, OnClickListener {
            val s = SpinnerSheet<T>(context, this.items, title, displayMember, searchable)
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
                        selectedObject = model

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

    fun setSelectedItem(obj: T) {
        val gson = Gson()
        val jsonStr = gson.toJson(obj)

        if (obj is String) {

            selectedItem.setText(obj)
            selectedObject = obj

        } else if (obj != null) {

            val jsonObj = JsonParser.parseString(jsonStr).asJsonObject

            val map = jsonObj.asMap()
            val keys = map.keys

            var res = ""

            if (!displayMember.isNullOrEmpty()) res =
                jsonObj.get(displayMember).asString
            else res = jsonObj.get(keys.max()).asString

            selectedObject = obj
            selectedItem.setText(res)

        } else if (obj == null) {
            selectedObject = null
            selectedItem.clear()
        }
    }

    fun getSelectedItem(): T? {
        return selectedObject
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
        this.textInputLayout.hint = title
    }

}
