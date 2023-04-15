package com.example.storyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStoryItem

class StoryAdapter(private val listStory: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageStory: ImageView = itemView.findViewById(R.id.image_story)
        val nameUser: TextView = itemView.findViewById(R.id.name_user)
        val descriptionUser: TextView = itemView.findViewById(R.id.description_user)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_story, viewGroup, false)
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val story = listStory[position]

        Glide.with(viewHolder.itemView.context)
            .load(story.photoUrl)
            .into(viewHolder.imageStory)

        viewHolder.nameUser.text = story.name
        viewHolder.descriptionUser.text = story.description
    }

    override fun getItemCount() = listStory.size
}