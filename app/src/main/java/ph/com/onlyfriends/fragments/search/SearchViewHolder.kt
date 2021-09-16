package ph.com.onlyfriends.fragments.search

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ph.com.onlyfriends.Keys
import ph.com.onlyfriends.SearchProfileActivity
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Friend

open class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var ivDefaultPicture: ImageView = itemView.findViewById(R.id.tv_search_dp)
    private var tvUsername: TextView = itemView.findViewById(R.id.tv_search_name)
    private var tvHandle: TextView = itemView.findViewById(R.id.tv_search_handle)
    private var clLayout: ConstraintLayout = itemView.findViewById(R.id.cl_search_layout)

    open fun bind(model: Friend) {
        ivDefaultPicture.setImageResource(R.drawable.ic_default_user)
        tvUsername.text = model.name
        tvHandle.text = model.handle

        clLayout.setOnClickListener {
            val intent = Intent(
                itemView.context,
                SearchProfileActivity::class.java
            )

            intent.putExtra(Keys.NAME.name, model.name)
            intent.putExtra(Keys.HANDLE.name, model.handle)
            intent.putExtra(Keys.ISFOLLOWED.name, "false")

            itemView.context.startActivity(intent)
        }
    }
}