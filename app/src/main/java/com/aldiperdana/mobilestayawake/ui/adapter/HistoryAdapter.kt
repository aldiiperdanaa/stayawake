package com.aldiperdana.mobilestayawake.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aldiperdana.mobilestayawake.R
import com.aldiperdana.mobilestayawake.data.model.HistoryModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class HistoryAdapter(private var history: List<HistoryModel>, context: Context) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyTitle: TextView = itemView.findViewById(R.id.historyTitle)
//        val historyStartTime: TextView = itemView.findViewById(R.id.historyStartTime)
        val historyFinishTime: TextView = itemView.findViewById(R.id.historyFinishTime)
        val historyDuration: TextView = itemView.findViewById(R.id.historyDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int = history.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = history[position]
        holder.historyTitle.text = history.title
//        holder.historyStartTime.text = history.start_time

        val formattedFinishTime = formatFinishTime(history.finish_time)
        holder.historyFinishTime.text = formattedFinishTime

        val formattedDuration = formatDuration(history.duration)
        holder.historyDuration.text = formattedDuration
    }

    private fun formatFinishTime(rawFinishTime: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val finishTime = formatter.parse(rawFinishTime)

        val currentTime = System.currentTimeMillis()
        val elapsedTimeMillis = currentTime - finishTime.time

        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
        val days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis)

        return when {
            seconds < 60 -> "$seconds seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    }

    private fun formatDuration(rawDuration: String): String {
        val parts = rawDuration.split(":")
        val hours = parts[0]
        val minutes = parts[1]
        val seconds = parts[2]

        return if (hours == "00") {
            "$minutes:$seconds"
        } else {
            "$hours:$minutes:$seconds"
        }
    }

    fun refreshData(newHistory: List<HistoryModel>) {
        history = newHistory
        notifyDataSetChanged()
    }
}