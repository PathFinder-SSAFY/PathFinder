package com.dijkstra.pathfinder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dijkstra.pathfinder.data.dto.Path
import com.dijkstra.pathfinder.databinding.NavigationPathListItemBinding
import okhttp3.internal.format

private const val TAG = "NavigationPathAdapter_ssafy"
class NavigationPathAdapter(var pathList: MutableList<Path>) :
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
        val currentPath = pathList[position]
        when (currentPath.direction) {
            5 -> {
                holder.binding.navigationPathDirectionTextview.text = "좌회전"
                holder.binding.navigationPathDirectionImageview.setImageResource(R.drawable.navigation_diriection_arrow_turn_left)
            }
            6 -> {
                holder.binding.navigationPathDirectionTextview.text = "우회전"
                holder.binding.navigationPathDirectionImageview.setImageResource(R.drawable.navigation_diriection_arrow_turn_right)
            }
            else -> {
                holder.binding.navigationPathDirectionTextview.text = "${String.format("%.2f", pathList[position].distance) }m 직진"
                holder.binding.navigationPathDirectionImageview.setImageResource(R.drawable.navigation_diriection_arrow_straight)
            }
        }
    }

}