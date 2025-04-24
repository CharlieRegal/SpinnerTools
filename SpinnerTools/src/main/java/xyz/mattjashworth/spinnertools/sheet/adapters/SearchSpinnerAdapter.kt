package xyz.mattjashworth.spinnertools.sheet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonParser
import xyz.mattjashworth.spinnertools.R
import xyz.mattjashworth.spinnertools.sheet.enums.Mode

internal class SearchSpinnerAdapter<T>(private val mode: Mode, items: ArrayList<T>, displayMember: String?): RecyclerView.Adapter<SearchSpinnerAdapter.ViewHolder>() {

    private val spinnerItems: ArrayList<T>
    private var selectedPosition: Int = -1
    private var selectedPositions: MutableList<T> = mutableListOf()

    private var onClickListener: OnClickListener<T>? = null
    private var onMultiSelectListener: OnMultiSelectListener<T>? = null

    private val member: String?


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.searchspinner_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return spinnerItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: T = spinnerItems[position]

        val gson = Gson()
        val jsonStr = gson.toJson(model)

        if (model is String) {

            holder.itemText.text = model

        } else {

            val obj = JsonParser.parseString(jsonStr).asJsonObject

            val map = obj.asMap()
            val keys = map.keys

            var res = ""

            try {
                if (member != null) res = obj.get(member).asString
                else res = obj.get(keys.max()).asString
            } catch (ex: Exception) {
                res = obj.get(keys.max()).asString
            }


            holder.itemText.text = res
        }

        holder.itemView.setOnClickListener {

            when(mode) {
                Mode.SINGLE -> {
                    this.selectedPosition = holder.adapterPosition
                    onClickListener?.onClick(position, model)
                    notifyDataSetChanged()
                }
                Mode.MULTI -> {
                    if (this.selectedPositions.contains(model))
                        this.selectedPositions.remove(model)
                    else
                        this.selectedPositions.add(model)

                    onMultiSelectListener?.onMultiSelect(selectedPositions)
                    notifyDataSetChanged()
                }
            }
        }

        when(mode) {
            Mode.SINGLE -> {
                if (selectedPosition == holder.adapterPosition)
                    holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                        R.drawable.checked, 0)
                else
                    holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
            Mode.MULTI -> {
                if (selectedPositions.contains(model))
                    holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                        R.drawable.checked, 0)
                else
                    holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)

            }
        }
    }

    fun setSelectedPosition(obj: T) {
        val idx = spinnerItems.indexOf(obj)
        selectedPosition = idx
    }

    fun setSelectedPosition(objs: List<T>) {
        selectedPositions.clear()
        selectedPositions.addAll(objs)
    }


    fun setOnMultiSelectListener(onMultiSelectListener: OnMultiSelectListener<T>) {
        this.onMultiSelectListener = onMultiSelectListener
    }

    fun setOnClickListener(onClickListener: OnClickListener<T>) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener<T> {
        fun onClick(position: Int, model: T)
    }

    interface OnMultiSelectListener<T> {
        fun onMultiSelect(models: List<T>)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemText: TextView

        init {
            itemText = itemView.findViewById(R.id.searchSpinner_item_text)
        }
    }

    init {
        this.spinnerItems = items
        this.member = displayMember
    }
}