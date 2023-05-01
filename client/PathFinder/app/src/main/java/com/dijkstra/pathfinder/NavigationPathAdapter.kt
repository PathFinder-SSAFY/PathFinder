package com.dijkstra.pathfinder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dijkstra.pathfinder.databinding.NavigationPathListItemBinding

private const val TAG = "NavigationPathAdapter_ssafy"
class NavigationPathAdapter(var pathList: MutableList<String>) :
    RecyclerView.Adapter<NavigationPathAdapter.NavigationPathViewHolder>() {
    inner class NavigationPathViewHolder(val binding: NavigationPathListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationPathViewHolder {
        val binding = NavigationPathListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NavigationPathViewHolder(binding)
    }

    override fun getItemCount(): Int = pathList.size

    override fun onBindViewHolder(holder: NavigationPathViewHolder, position: Int) {
        holder.binding.navigationPathDirectionTextview.text = pathList[position]
        Log.d(TAG, "onBindViewHolder: ${pathList[position]}")
    }

}