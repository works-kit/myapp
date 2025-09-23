package com.multibahana.myapp.presentation.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.multibahana.myapp.databinding.RvPostItemBinding
import com.multibahana.myapp.domain.model.PostEntity

class PostAdapter(
    private var listPosts: List<PostEntity>,
    private var onDeletePost: (PostEntity) -> Unit,
    private var onEditPost: (PostEntity) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = RvPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = listPosts[position]

        holder.binding.apply {
            postTitle.text = post.title

            editPost.setOnClickListener { onEditPost(post)}
            deletePost.setOnClickListener { onDeletePost(post) }
        }

    }

    override fun getItemCount(): Int = listPosts.size

    inner class PostViewHolder(val binding: RvPostItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updatePosts(newPosts: List<PostEntity>) {
        listPosts = newPosts
        notifyDataSetChanged()
    }
}