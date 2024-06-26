package xyz.mattjashworth.spinnertools.sheet

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonParser
import xyz.mattjashworth.spinnertools.R



@RequiresApi(Build.VERSION_CODES.N_MR1)
class Spinner<T>(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    private lateinit var selectedItem: EditText
    private var card: CardView
    private var items = ArrayList<T>()

    private var SelectedObject: T? = null

    private var DisplayMember: String? = null
    private var Title = "Select Item"
    private var SelectedMode: Mode = Mode.Sheet
    private var Searchable = false
    private var DismissWhenSelected = false

    private enum class Mode {Sheet, Dropdown}
    private var type: Any? = null

    private var onItemSelectedListener: OnItemSelectedListener<T>? = null

    init {

        val root = inflate(context, R.layout.spinner, this)

        val ta: TypedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.Spinner)
        DisplayMember = ta.getString(R.styleable.Spinner_DisplayMember)!!
        SelectedMode = Mode.values()[ta.getInt(R.styleable.Spinner_Mode, 0)]
        Title = ta.getString(R.styleable.Spinner_Title)!!
        Searchable = ta.getBoolean(R.styleable.Spinner_Searchable, false)
        DismissWhenSelected = ta.getBoolean(R.styleable.Spinner_DismissWhenSelected, false)

        selectedItem = findViewById<EditText>(R.id.tv_spinner_selected)
        val layout = findViewById<TextInputLayout>(R.id.tIL)
        card = findViewById(R.id.card_spinner)
        layout.hint = Title

        setChildListener(rootView, OnClickListener {
            val s = SpinnerSheet<T>(context, items, Title, DisplayMember)
            if (SelectedObject != null)
                s.setSelectedObject(SelectedObject!!)
            s.setOnItemClickListener(object : SpinnerSheet.OnSearchSpinnerClickListener<T> {
                override fun onClick(position: Int, model: T) {

                    if (DismissWhenSelected)
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

                        if (!DisplayMember.isNullOrEmpty()) res = obj.get(DisplayMember).asString
                        else res = obj.get(keys.max()).asString

                        SelectedObject = model
                        selectedItem.setText(res)

                        onItemSelectedListener?.onItemSelected(model)
                    }
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
        DisplayMember = id
    }

    fun setTitle(title: String) {
        Title = title
    }

}