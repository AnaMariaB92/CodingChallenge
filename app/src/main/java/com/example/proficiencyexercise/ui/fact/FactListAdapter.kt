package com.example.proficiencyexercise.ui.fact

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.proficiencyexercise.R
import com.example.proficiencyexercise.model.Fact
import kotlinx.android.synthetic.main.view_facts_item.view.*

class FactListAdapter : RecyclerView.Adapter<FactListAdapter.FactViewHolder>() {

    private var mDataSource: List<Fact>? = null

    fun setDataSource(dataSource: List<Fact>) {
        this.mDataSource = dataSource
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_facts_item, parent, false)
        return FactViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        val factsItem = mDataSource!![position]
        holder.bind(factsItem)
    }


    override fun getItemCount(): Int {
        return mDataSource?.size ?: 0
    }

    class FactViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(fact: Fact) {
            itemView.fact_title.text = fact.title
            itemView.fact_description.text = fact.description

            Glide.with(itemView.context)
                .asBitmap().centerCrop()
                .load(fact.imageHref)
                .into(object : BitmapImageViewTarget(itemView.image_view) {
                    override fun setResource(resource: Bitmap?) {
                        super.setResource(resource)
                        val bitmapDrawable = RoundedBitmapDrawableFactory.create(
                            itemView.resources,
                            resource)
                        bitmapDrawable.isCircular = true
                        itemView.image_view.setImageDrawable(bitmapDrawable)
                    }
                })
        }
    }

}