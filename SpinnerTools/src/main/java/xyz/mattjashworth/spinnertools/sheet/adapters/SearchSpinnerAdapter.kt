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

internal class SearchSpinnerAdapter<T>(private val context: Context, items: ArrayList<T>, displayMember: String?): RecyclerView.Adapter<SearchSpinnerAdapter.ViewHolder>() {

    private val spinnerItems: ArrayList<T>
    private var selectedPosition = -1 // no selection by default

    private var onClickListener: OnClickListener<T>? = null

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
            this.selectedPosition = holder.adapterPosition
            if (onClickListener != null) {
                onClickListener!!.onClick(position, model)
            }
            notifyDataSetChanged()
        }


        if (selectedPosition == position)
            holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                R.drawable.checked, 0)
        else
            holder.itemText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)

    }

    fun setSelectedPosition(obj: T) {
        val idx = spinnerItems.indexOf(obj)
        selectedPosition = idx
    }



    fun setOnClickListener(onClickListener: OnClickListener<T>) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener<T> {
        fun onClick(position: Int, model: T)
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