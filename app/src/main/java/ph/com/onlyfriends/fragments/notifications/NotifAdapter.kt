package ph.com.onlyfriends.fragments.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ph.com.onlyfriends.R

class NotifAdapter(var typeList: List<String>, var msgList: List<String>)
    : RecyclerView.Adapter<NotifSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifSearchViewHolder {
        val notifView: View = LayoutInflater.from(parent.context).inflate(R.layout.template_notif, parent, false)

        return NotifSearchViewHolder(notifView)
    }

    override fun onBindViewHolder(holder: NotifSearchViewHolder, position: Int) {
        holder.bind(typeList[position], msgList[position])
    }

    override fun getItemCount(): Int {
        return typeList.size
    }
}