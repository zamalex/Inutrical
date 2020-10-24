package inutrical.com.inutrical.search

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import inutrical.com.inutrical.R
import kotlinx.android.synthetic.main.search_dialog.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class SearchAdapter(val context: Context, val array: List<HistoryModel.Data>,val historyClickListener: HistoryClickListener) :
    RecyclerView.Adapter<SearchAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linear: LinearLayout = itemView.findViewById(R.id.linear)
        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val img: ImageView = itemView.findViewById(R.id.history_img)

        init {
            img.setOnClickListener {
                historyClickListener.onItemClick(array[adapterPosition])
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        if (array != null)
            return array.size
        else return 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position % 2 == 0)
            holder.linear.setBackgroundColor(Color.parseColor("#F1F3FA"))
        else
            holder.linear.setBackgroundColor(Color.parseColor("#FAFBFE"))

        holder.name.text = array[position].clinicalDietation
        holder.date.text = array[position].followUpDate

        try {
            val cal = Calendar.getInstance()

            val s = array[position].followUpDate
            val inFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val dtIn = inFormat.parse(s)

            val month_date = SimpleDateFormat("dd MMMM yyyy")
            val month_name: String = month_date.format(dtIn)
            holder.date.text = month_name

        } catch (e: Exception) {
            holder.date.text = array[position].followUpDate

        }


    }
}