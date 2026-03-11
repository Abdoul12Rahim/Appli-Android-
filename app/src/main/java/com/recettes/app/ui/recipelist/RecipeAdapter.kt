package com.recettes.app.ui.recipelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.recettes.app.R
import com.recettes.app.data.model.Meal
import com.recettes.app.databinding.ItemRecipeBinding

class RecipeAdapter(
    private val onClick: (Meal) -> Unit
) : ListAdapter<Meal, RecipeAdapter.RecipeViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecipeViewHolder(
        private val binding: ItemRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meal: Meal) {
            binding.textRecipeTitle.text = meal.name
            binding.textRecipeArea.text = meal.area?.takeIf { it.isNotBlank() }
                ?: meal.category ?: ""
            binding.chipRecipeCategory.text = meal.category ?: ""
            binding.chipRecipeCategory.visibility =
                if (meal.category.isNullOrBlank()) android.view.View.GONE
                else android.view.View.VISIBLE

            Glide.with(binding.imageRecipe.context)
                .load(meal.thumbnail)
                .placeholder(R.color.shimmer_base)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .centerCrop()
                .into(binding.imageRecipe)

            binding.cardRecipe.setOnClickListener { onClick(meal) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(old: Meal, new: Meal) = old.id == new.id
            override fun areContentsTheSame(old: Meal, new: Meal) = old == new
        }
    }
}
