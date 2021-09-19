package ph.com.onlyfriends.fragments.notifications


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.com.onlyfriends.R

class NotifSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var ivDefaultPicture: ImageView = itemView.findViewById(R.id.tv_notif_dp)
    private var tvType: TextView = itemView.findViewById(R.id.tv_notif_type)
    private var tvMessage: TextView = itemView.findViewById(R.id.tv_notif_message)

    open fun bind(type: String, msg: String) {
        ivDefaultPicture.setImageResource(R.drawable.ic_default_user)
        tvType.text = type
        tvMessage.text = "Wow, $msg followed you!"
    }

}