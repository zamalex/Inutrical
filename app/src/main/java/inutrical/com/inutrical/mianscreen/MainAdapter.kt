package inutrical.com.inutrical.mianscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import inutrical.com.inutrical.R

class MainAdapter(val context: Context , var onMClick: onMainClick) :
    RecyclerView.Adapter<MainAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (position) {
            0 -> {
                holder.tv.text = "Calculate"
                holder.iv.setImageResource(R.drawable.calculate)
            }
            1 -> {
                holder.tv.text = "Existing Patient"
                holder.iv.setImageResource(R.drawable.patients)

            }

        }
        holder.cv.setOnClickListener{
            onMClick.onClickMain(position)
        }

    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv: TextView
        lateinit var iv: ImageView
        lateinit var cv: CardView


        init {
            tv = itemView.findViewById(R.id.textView2)
            iv = itemView.findViewById(R.id.imageView2)
            cv = itemView.findViewById(R.id.main_item)


        }


    }
}