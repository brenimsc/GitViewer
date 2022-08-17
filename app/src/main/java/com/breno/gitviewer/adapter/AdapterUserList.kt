package com.breno.gitviewer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breno.gitviewer.databinding.ItemUserListBinding
import com.breno.gitviewer.model.User
import com.bumptech.glide.Glide

class AdapterUserList : RecyclerView.Adapter<AdapterUserList.UserListViewHolder>() {

    private val listUser = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount() = listUser.size

    fun setList(list: List<User>) {
        this.listUser.clear()
        this.listUser.addAll(list)
        notifyDataSetChanged()
    }

    var onClick: ((User) -> Unit?)? = null

    inner class UserListViewHolder(val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                itemView.setOnClickListener {
                    onClick?.invoke(user)
                }
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .into(imgItemUserList)

                loginItemUserList.text = user.login
            }

        }

    }


}

