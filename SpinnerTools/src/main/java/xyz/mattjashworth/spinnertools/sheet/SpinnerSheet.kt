package xyz.mattjashworth.spinnertools.sheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonParser
import xyz.mattjashworth.spinnertools.R
import xyz.mattjashworth.spinnertools.sheet.adapters.SearchSpinnerAdapter
import xyz.mattjashworth.spinnertools.sheet.enums.Mode
import java.lang.reflect.Modifier

internal class SpinnerSheet<T>(context: Context, items: ArrayList<T>, title: String, displayMember: String?, searchable: Boolean, mode: Mode) {


    private var onSearchSpinnerClickListener: OnSearchSpinnerClickListener<T>? = null
    private var onSearchSpinnerMultiClickListener: OnSearchSpinnerMultiClickListener<T>? = null

    private lateinit var diag: BottomSheetDialog
    private lateinit var adapter: SearchSpinnerAdapter<T>

    val a = items.clone()
    var filteredItems: ArrayList<T> = ArrayList(items.toList())

    init {

        filteredItems = ArrayList(items.toList())
        if (::adapter.isInitialized && adapter != null) {
            adapter.notifyDataSetChanged()
        }

        diag = BottomSheetDialog(context)
        val layoutInflater = LayoutInflater.from(context)
        val spinnerSheetView = layoutInflater.inflate(R.layout.bottomsheet_spinner, null)

        val search = spinnerSheetView.findViewById<EditText>(R.id.et_bottomsheet_search)
        if (!searchable) search.visibility = View.GONE
        val sheetTitle = spinnerSheetView.findViewById<TextView>(R.id.tv_sheet_title)
        sheetTitle.text = title

        val rcy = spinnerSheetView.findViewById<RecyclerView>(R.id.rcy_spinner_bottomsheet)

        val div = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        rcy.addItemDecoration(div)

        adapter = SearchSpinnerAdapter<T>(mode, filteredItems, displayMember)
        adapter.setOnClickListener(object : SearchSpinnerAdapter.OnClickListener<T> {
            override fun onClick(position: Int, model: T) {
                onSearchSpinnerClickListener?.onClick(position, model)
            }
        })
        adapter.setOnMultiSelectListener(object : SearchSpinnerAdapter.OnMultiSelectListener<T> {
            override fun onMultiSelect( models: List<T>) {
                onSearchSpinnerMultiClickListener?.onClick(models)
            }
        })

        search.addTextChangedListener {
            if (it.isNullOrBlank()) {
                filteredItems.clear()
                filteredItems.addAll(items)
            } else {

                val fil = items.filter { x -> reflectionToString(x).lowercase().contains(it.toString().lowercase()) }
                filteredItems.clear()
                filteredItems.addAll(fil)
            }
            adapter.notifyDataSetChanged()

        }

        val llm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcy.layoutManager = llm
        rcy.adapter = adapter

        diag.behavior.peekHeight = calculatePeekHeight(items.count())

        diag.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        diag.behavior.isDraggable = true




        diag.setCancelable(true)

        diag.setContentView(spinnerSheetView)

        diag.show()

    }

    fun dismiss() {
        diag.dismiss()
    }

    fun setSelectedObject(obj: T) {
        adapter.setSelectedPosition(obj)
    }

    fun setSelectedObject(obj: List<T>) {
        adapter.setSelectedPosition(obj)
    }

    fun calculatePeekHeight(itemCount: Int): Int {

        val peekH = itemCount * 55
        if (peekH > 8) return  BottomSheetBehavior.PEEK_HEIGHT_AUTO
        else return  peekH
    }

    fun setOnItemClickListener(onClickListener: OnSearchSpinnerClickListener<T>) {
        this.onSearchSpinnerClickListener = onClickListener
    }

    fun setOnMultiClickListener(onClickListener: OnSearchSpinnerMultiClickListener<T>) {
        this.onSearchSpinnerMultiClickListener = onClickListener
    }

    fun reflectionToString(obj: Any?): String {
        if(obj == null) {
            return "null"
        }
        val s = mutableListOf<String>()
        var clazz: Class<in Any>? = obj.javaClass
        while (clazz != null) {
            for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
                prop.isAccessible = true
                s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
            }
            clazz = clazz.superclass
        }
        return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
    }

    interface OnSearchSpinnerClickListener<T> {
        fun onClick(position: Int, model: T)
    }

    interface OnSearchSpinnerMultiClickListener<T> {
        fun onClick(model: List<T>)
    }
}