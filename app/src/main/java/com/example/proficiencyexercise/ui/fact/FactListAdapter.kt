package com.example.proficiencyexercise.ui.fact

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.proficiencyexercise.R
import com.example.proficiencyexercise.model.Fact

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

        private val factImage: ImageView? by lazy {
            itemView.findViewById(R.id.image_view) as ImageView?
        }
        private val factTitle: TextView? by lazy {
            itemView.findViewById(R.id.fact_title) as TextView?
        }
        private val factDescription: TextView? by lazy {
            itemView.findViewById(R.id.fact_description) as TextView?
        }

        fun bind(fact: Fact) {
            factTitle!!.text = fact.title
            factDescription!!.text = fact.description

            Glide.with(itemView.context)
                .asBitmap().centerCrop()
                .load(fact.imageHref)
                .into(object : BitmapImageViewTarget(factImage!!) {
                    override fun setResource(resource: Bitmap?) {
                        super.setResource(resource)
                        val bitmapDrawable = RoundedBitmapDrawableFactory.create(
                            itemView.resources,
                            resource)
                        bitmapDrawable.isCircular = true
                        factImage!!.setImageDrawable(bitmapDrawable)
                    }
                })
        }
    }

}