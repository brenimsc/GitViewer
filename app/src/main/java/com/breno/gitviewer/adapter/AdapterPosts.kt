package com.breno.gitviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breno.gitviewer.databinding.ItemPostsBinding
import com.breno.gitviewer.model.Post
import com.bumptech.glide.Glide

class AdapterPosts : RecyclerView.Adapter<AdapterPosts.PostsViewHolder>() {

    private val list: MutableList<Post> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            ItemPostsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    lateinit var onClick: (post: Post) -> Unit

    fun setList(list: List<Post>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class PostsViewHolder(private val binding: ItemPostsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {

                itemView.setOnClickListener {
                    onClick.invoke(post)
                }
                txtDescriptionPost.text = post.description

                txtNameUserPost.text = post.owner.login

                Glide.with(itemView).load(post.owner.avatar_url)
                    .into(imagePost)
            }

        }

    }
}