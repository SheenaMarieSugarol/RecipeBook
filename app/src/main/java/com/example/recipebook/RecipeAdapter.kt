package com.example.recipebook

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(
    private val recipes: MutableList<Recipe>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_FLAVOR = 0
        private const val TYPE_RECIPE = 1
    }

    private val items = mutableListOf<Any>()

    init {
        refreshItems()
    }


    private fun refreshItems() {
        items.clear()

        for (flavor in Flavor.values()) {
            items.add(FlavorTitle(flavor))

            items.addAll(
                recipes.filter { it.flavor == flavor }
            )
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is FlavorTitle) {
            TYPE_FLAVOR
        } else {
            TYPE_RECIPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_FLAVOR) {
            val view = inflater.inflate(
                R.layout.item_flavor,
                parent,
                false
            )
            FlavorViewHolder(view)
        } else {
            val view = inflater.inflate(
                R.layout.item_recipe,
                parent,
                false
            )
            RecipeViewHolder(view)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {

            is FlavorViewHolder -> {
                val item = items[position] as FlavorTitle

                holder.flavorTitle.text =
                    when (item.flavor) {
                        Flavor.SWEET -> "Sweet Recipes"
                        Flavor.SAVORY -> "Savory Recipes"
                    }
            }

            is RecipeViewHolder -> {
                val recipe = items[position] as Recipe

                holder.recipeTitle.text = recipe.title

                holder.itemView.setOnClickListener {
                    AlertDialog.Builder(
                        holder.itemView.context
                    )
                        .setTitle(recipe.title)
                        .setMessage(recipe.description)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
        refreshItems()
    }

    fun isRecipe(position: Int): Boolean {
        return position in items.indices &&
                items[position] is Recipe
    }

    fun removeRecipe(position: Int) {
        if (!isRecipe(position)) return

        val recipe = items[position] as Recipe
        recipes.remove(recipe)
        refreshItems()
    }

    class FlavorViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val flavorTitle: TextView =
            itemView.findViewById(R.id.tvFlavorTitle)
    }

    class RecipeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val recipeTitle: TextView =
            itemView.findViewById(R.id.tvRecipeTitle)
    }
}
