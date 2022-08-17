package com.breno.gitviewer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.breno.gitviewer.helper.HelperEvents
import com.breno.gitviewer.R
import com.breno.gitviewer.databinding.ItemUserRepositorieBinding
import com.breno.gitviewer.model.RepositoryGitResponse

class AdapterRepository(val context: Context) : RecyclerView.Adapter<AdapterRepository.RepositoryViewHolder>() {

    private val list = mutableListOf<RepositoryGitResponse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(
            ItemUserRepositorieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(list[position], HelperEvents.returnColor(list[position].language.orEmpty()))
    }

    override fun getItemCount() = list.size

    fun setList(list: List<RepositoryGitResponse>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(val binding: ItemUserRepositorieBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(repository: RepositoryGitResponse, color: Int) {
                binding.apply {
                    loginItemUserRepositorie.text = repository.owner?.login
                    branchItemUserRepositorie.text = repository.default_branch
                    dateUpdatedItemUserRepositorie.text = repository.updated_at?.substring(0, 10)
                    descriptionItemUserRepositorie.text = repository.description ?: "Sem Descrição"
                    nameItemUserRepositorie.text = repository.name
                    qtdStarItemUserRepositorie.text = repository.stargazers_count.toString()
                    qtdWatchItemUserRepositorie.text = repository.watchers_count.toString()
                    branchItemUserRepositorie.text = context.getString(R.string.branch, repository.default_branch)
                    languageItemUserRepositorie.text = context.getString(R.string.language, repository.language ?: "Não especificada")

                    languageCircleItemUserRepositorie.setCardBackgroundColor(ContextCompat.getColor(context, color))
                }
            }
    }
}