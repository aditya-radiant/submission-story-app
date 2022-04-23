package com.dicoding.picodiploma.submission_story_app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.submission_story_app.R
import com.dicoding.picodiploma.submission_story_app.data.repository.StoryRepository
import com.dicoding.picodiploma.submission_story_app.data.response.ListStoryItem
import com.dicoding.picodiploma.submission_story_app.databinding.StoryItemBinding
import com.dicoding.picodiploma.submission_story_app.ui.Helper
import com.dicoding.picodiploma.submission_story_app.ui.detail.DetailStoryActivity


class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val listStory = ArrayList<ListStoryItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListStory(itemStory: List<ListStoryItem>) {
        val diffCallback = StoryRepository(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class StoryViewHolder(private val v: StoryItemBinding): RecyclerView.ViewHolder(v.root){
        fun bind(item: ListStoryItem){
            v.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
            }
            Glide.with(v.imgPost)
                .load(item.photoUrl)
                .into(v.imgPost)
            v.tvUserName.text = item.name
            v.tvCaption.text = item.description
/*            v.imgPost.setOnClickListener {
                val intent = Intent(it.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, item)
                it.context.startActivity(intent)
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
        StoryViewHolder(StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = listStory[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback{
        fun onItemClicked(data: ListStoryItem)
    }




}
