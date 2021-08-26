package ph.com.onlyfriends.fragments.search

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Friend

open class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tvUsername: TextView = itemView.findViewById(R.id.tv_template_name)
    private var tvHandle: TextView = itemView.findViewById(R.id.tv_template_handle)

    open fun bind(model: Friend) {
        tvUsername.text = model.name
        tvHandle.text = model.handle
    }
}