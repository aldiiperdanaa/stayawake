package com.aldiperdana.mobilestayawake.ui.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldiperdana.mobilestayawake.ui.adapter.HistoryAdapter
import com.aldiperdana.mobilestayawake.helper.HistoryDatabaseHelper
import com.aldiperdana.mobilestayawake.R
import com.aldiperdana.mobilestayawake.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var db: HistoryDatabaseHelper
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = HistoryDatabaseHelper(requireContext())
        historyAdapter = HistoryAdapter(db.getAllHistory(), requireContext())

        binding.historyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.historySpacing)
        binding.historyRecyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        binding.historyRecyclerView.adapter = historyAdapter

        updateVisibility()
    }

    override fun onResume() {
        super.onResume()
        historyAdapter.refreshData(db.getAllHistory())
    }

    private class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            outRect.top = space
            outRect.bottom = space

            if (position % 2 == 0) {
                outRect.right = space
            } else {
                outRect.left = space
            }
        }
    }

    private fun updateVisibility() {
        if (db.getAllHistory().isEmpty()) {
            binding.emptyHistory.root.visibility = View.VISIBLE
            binding.historyRecyclerView.visibility = View.GONE
        } else {
            binding.emptyHistory.root.visibility = View.GONE
            binding.historyRecyclerView.visibility = View.VISIBLE
        }
    }
}
