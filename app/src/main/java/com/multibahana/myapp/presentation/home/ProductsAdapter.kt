package com.multibahana.myapp.presentation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.multibahana.myapp.R
import com.multibahana.myapp.databinding.RvProductItemBinding
import com.multibahana.myapp.domain.model.AllProductsEntity

class ProductsAdapter(private var productList: AllProductsEntity) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        val binding =
            RvProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductsViewHolder,
        position: Int
    ) {
        val product = productList.products[position]
        Log.d("ProductsAdapter", "Product: $product")

        holder.binding.apply {
            tvTitle.text = product.title

            val priceText = product.price.let {
                "$ %,d".format(it.toLong())
            }
            tvPrice.text = priceText

            tvStock.text = product.stock.toString()

            Glide.with(ivThumbnail.context)
                .load(product.thumbnail)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivThumbnail)
        }

    }

    override fun getItemCount(): Int = productList.products.size

    fun updateProducts(newProducts: AllProductsEntity) {
        productList = newProducts
        notifyDataSetChanged()
    }

    inner class ProductsViewHolder(val binding: RvProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}