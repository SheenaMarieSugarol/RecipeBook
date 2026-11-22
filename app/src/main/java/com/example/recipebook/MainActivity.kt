package com.example.recipebook

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    private lateinit var etRecipeTitle: EditText
    private lateinit var etRecipeDescription: EditText

    private lateinit var btnAddSweet: Button
    private lateinit var btnAddSavory: Button

    private val recipes = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Connect XML components
        recyclerView = findViewById(R.id.recyclerViewRecipes)

        etRecipeTitle = findViewById(R.id.etRecipeTitle)

        etRecipeDescription =
            findViewById(R.id.etRecipeDescription)

        btnAddSweet = findViewById(R.id.btnAddSweet)
        btnAddSavory = findViewById(R.id.btnAddSavory)

        // Sample Sweet Recipes
        recipes.add(
            Recipe(
                "Chocolate Cake",
                "A delicious chocolate dessert.",
                Flavor.SWEET
            )
        )

        recipes.add(
            Recipe(
                "Pancakes",
                "Fluffy pancakes served with syrup.",
                Flavor.SWEET
            )
        )

        // Sample Savory Recipes
        recipes.add(
            Recipe(
                "Chicken Adobo",
                "Chicken cooked with soy sauce, vinegar, and garlic.",
                Flavor.SAVORY
            )
        )

        recipes.add(
            Recipe(
                "Garlic Pasta",
                "Pasta cooked with garlic and olive oil.",
                Flavor.SAVORY
            )
        )

        // Setup RecyclerView
        recipeAdapter = RecipeAdapter(recipes)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = recipeAdapter

        // Add Sweet Recipe
        btnAddSweet.setOnClickListener {
            addRecipe(Flavor.SWEET)
        }

        // Add Savory Recipe
        btnAddSavory.setOnClickListener {
            addRecipe(Flavor.SAVORY)
        }

        // Enable swipe to delete
        setupSwipeToDelete()
    }

    private fun addRecipe(flavor: Flavor) {

        val title =
            etRecipeTitle.text.toString().trim()

        val description =
            etRecipeDescription.text.toString().trim()

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter a title and description",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val newRecipe = Recipe(
            title = title,
            description = description,
            flavor = flavor
        )

        recipeAdapter.addRecipe(newRecipe)

        // Clear fields after adding
        etRecipeTitle.text.clear()
        etRecipeDescription.text.clear()

        Toast.makeText(
            this,
            "Recipe added successfully!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupSwipeToDelete() {

        val swipeHelper =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun getSwipeDirs(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {

                    val position =
                        viewHolder.bindingAdapterPosition

                    return if (
                        recipeAdapter.isRecipe(position)
                    ) {
                        super.getSwipeDirs(
                            recyclerView,
                            viewHolder
                        )
                    } else {
                        0
                    }
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {

                    val position =
                        viewHolder.bindingAdapterPosition

                    recipeAdapter.removeRecipe(position)

                    Toast.makeText(
                        this@MainActivity,
                        "Recipe deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        ItemTouchHelper(swipeHelper)
            .attachToRecyclerView(recyclerView)
    }
}
