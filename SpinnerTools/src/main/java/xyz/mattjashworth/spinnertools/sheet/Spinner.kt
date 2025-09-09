package xyz.mattjashworth.spinnertools.sheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import xyz.mattjashworth.spinnertools.R
import xyz.mattjashworth.spinnertools.sheet.enums.Mode

@SuppressLint("PrivateResource")
class Spinner<T>(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private var selectedItem: EditText
    private var card: CardView
    private var items = ArrayList<T>()

    private var selectedObject: T? = null
    private var selectedObjects: List<T>? = null

    private var displayMember: String? = null
    private var title = "Select Item"
    private var searchable = false
    private var dismissWhenSelected = false
    private var mode: Mode = Mode.SINGLE


    //colors
    private var backgroundColor: Int? = null
    private var hintTextColor: Int? = null
    private var textColor: Int? = null

    private  var textInputLayout: TextInputLayout

    private var type: Any? = null

    private var onItemSelectedListener: OnItemSelectedListener<T>? = null
    private var onMultiItemSelectedListener: OnMultiItemSelectedListener<T>? = null

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
        ta.getInt(R.styleable.Spinner_mode, 0).let {
            mode = Mode.fromInt(it)
        }

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
            val s = SpinnerSheet<T>(context, this.items, title, displayMember, searchable, mode)
            when (mode) {
                Mode.SINGLE -> if (selectedObject != null) s.setSelectedObject(selectedObject!!)
                Mode.MULTI -> if (selectedObjects != null && selectedObjects!!.isNotEmpty()) s.setSelectedObject(selectedObjects!!)
            }
            s.setOnItemClickListener(object : SpinnerSheet.OnSearchSpinnerClickListener<T> {
                override fun onClick(position: Int, model: T) {

                    if (dismissWhenSelected)
                        s.dismiss()


                    val textToDisplay = if (model is String) {
                        model
                    } else {
                        try {
                            val kClass = model!!::class
                            val kProp = kClass.members.find { it.name == displayMember }
                            if (kProp != null) {
                                kProp.call(model)?.toString() ?: ""
                            } else {
                                model.toString()
                            }
                        } catch (ex: Exception) {
                            model.toString()
                        }
                    }

                    selectedItem.setText(textToDisplay)
                    selectedObject = model

                    onItemSelectedListener?.onItemSelected(model)
                }

            })
            s.setOnMultiClickListener(object : SpinnerSheet.OnSearchSpinnerMultiClickListener<T> {
                override fun onClick(model: List<T>) {

                    if (model.isNotEmpty() && model[0] is String) {

                        val stringBuilder = StringBuilder()

                        model.forEachIndexed { index, t ->
                            if (index == model.count() - 1) stringBuilder.append(t.toString())
                            else stringBuilder.append("$t, ")
                        }

                        selectedItem.setText(stringBuilder.toString())
                        selectedObjects = model

                    } else {

                        val stringBuilder = StringBuilder()

                        if (model.isEmpty()) {
                            selectedItem.text.clear()
                            selectedObjects = null
                            return
                        }

                        model.forEachIndexed { index, t ->

                            val kClass = model[index]!!::class
                            val kProp = kClass.members.find { it.name == displayMember }

                            if (kProp != null) {
                                stringBuilder.append("${kProp.call(model[index])?.toString() ?: ""}, ")
                            } else {
                                stringBuilder.append("${model[index].toString()}, ")
                            }
                        }


                        selectedObjects = model
                        selectedItem.setText(stringBuilder.toString())

                    }

                    onMultiItemSelectedListener?.onItemSelected(model)
                }

            })
        })


    }

    fun setSelectedItem(obj: T) {
        val textToDisplay = if (obj is String) {
            obj
        } else if (obj == null) {
            null
        } else {
            try {
                val kClass = obj!!::class
                val kProp = kClass.members.find { it.name == displayMember }
                if (kProp != null) {
                    kProp.call(obj)?.toString() ?: ""
                } else {
                    obj.toString()
                }
            } catch (ex: Exception) {
                obj.toString()
            }
        }

        if (textToDisplay == null) {
            selectedObject = null
            selectedItem.text.clear()
        } else {
            when(mode) {
                Mode.SINGLE -> selectedObject = obj
                Mode.MULTI -> selectedObjects = listOf(obj)
            }
            selectedItem.setText(textToDisplay)
        }
    }

    fun setSelectedItem(obj: List<T>) {
        val textToDisplay = if (obj[0] is String) {
            val stringBuilder = StringBuilder()

            obj.forEachIndexed { index, t ->
                if (index == obj.count() - 1) stringBuilder.append(t.toString())
                else stringBuilder.append("$t, ")
            }
            stringBuilder.toString()
        } else {

            val stringBuilder = StringBuilder()

            try {
                val kClass = obj[0]!!::class
                val kProp = kClass.members.find { it.name == displayMember }
                if (kProp != null) {
                    obj.forEachIndexed { index, t ->
                        if (index == obj.count() - 1) stringBuilder.append(
                            kProp.call(t)?.toString() ?: ""
                        )
                        else stringBuilder.append(kProp.call(t)?.toString() + ", ")
                    }
                } else {
                    obj.forEachIndexed { index, t ->
                        if (index == obj.count() - 1) stringBuilder.append(t.toString())
                        else stringBuilder.append("$t, ")
                    }
                }
                stringBuilder.toString()
            } catch (ex: Exception) {
                obj.forEachIndexed { index, t ->
                    if (index == obj.count() - 1) stringBuilder.append(t.toString())
                    else stringBuilder.append("$t, ")
                }
                stringBuilder.toString()
            }
        }

        when (mode) {
            Mode.SINGLE -> selectedObject = obj[0]
            Mode.MULTI -> selectedObjects = obj
        }
        selectedItem.setText(textToDisplay)

    }

    fun setMode(mode: Mode) {
        this.mode = mode
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

    fun setOnMultiItemSelectedListener(onMultiItemSelectedListener: OnMultiItemSelectedListener<T>) {
        this.onMultiItemSelectedListener = onMultiItemSelectedListener
    }

    interface OnMultiItemSelectedListener<T> {
        fun onItemSelected(models: List<T>)
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
