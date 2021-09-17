package ph.com.onlyfriends.fragments.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Post

class AdapterPosts(private var postList: List<Post>) : RecyclerView.Adapter<AdapterPosts.PostViewHolder>() {


    // ViewHolder
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // views from template_post
        val ivDefaultPicture: ImageView = itemView.findViewById(R.id.tv_post_dp)
        var tvName: TextView = itemView.findViewById(R.id.tv_post_name)
        var tvHandle: TextView = itemView.findViewById(R.id.tv_post_handle)
        var tvContent: TextView = itemView.findViewById(R.id.tv_post_content)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postView: View = LayoutInflater.from(parent.context).inflate(R.layout.template_post, parent, false)

        return PostViewHolder(postView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        // get data
        val currPost = postList[position]

        // set post data
        holder.ivDefaultPicture.setImageResource(R.drawable.ic_default_user)
        holder.tvName.text = currPost.uName
        holder.tvHandle.text = currPost.uHandle
        holder.tvContent.text = currPost.pContent

    }

    override fun getItemCount(): Int {
        return postList.size
    }
}
