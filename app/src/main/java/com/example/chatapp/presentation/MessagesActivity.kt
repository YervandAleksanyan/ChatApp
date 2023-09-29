package com.example.chatapp.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.applicationComponent
import com.example.chatapp.databinding.ActivityMessagesBinding
import com.example.chatapp.presentation.rv_components.MessagesAdapter
import com.example.chatapp.presentation.viewModel.MessagesViewModel
import kotlinx.coroutines.launch


class MessagesActivity : AppCompatActivity() {

    private val viewModel: MessagesViewModel by viewModels {
        applicationComponent.messagesViewModelFactory
    }
    private val recyclerViewLayoutManager: LinearLayoutManager
        get() = (binding.messagesRv.layoutManager as LinearLayoutManager)
    private lateinit var binding: ActivityMessagesBinding
    private var keyboardHeight = 0

    private lateinit var adapter: MessagesAdapter
    private var targetingToEnd = true

    override fun onCreate(savedInstanceState: Bundle?) {
        applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMessagesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        window.setBackgroundDrawableResource(R.drawable.maths)

        initViews()
        observeData()

        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val insetValues = insets.getInsets(WindowInsetsCompat.Type.systemBars()) + insets.getInsets(WindowInsetsCompat.Type.ime())
            binding.root.updatePadding(insetValues.left, insetValues.top, insetValues.right, insetValues.bottom)

            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            if(imeVisible) {
                keyboardHeight = imeHeight
                if(!targetingToEnd)
                    binding.messagesRv.smoothScrollBy(0, imeHeight)
                else binding.messagesRv.smoothScrollBy(0, 2*imeHeight)
            } else if(!targetingToEnd) binding.messagesRv.smoothScrollBy(0, -keyboardHeight)
            else binding.messagesRv.smoothScrollBy(0, keyboardHeight)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun initViews() {
        adapter = MessagesAdapter(LayoutInflater.from(this))
        binding.messagesRv.layoutManager = LinearLayoutManager(this)
        binding.messagesRv.adapter = adapter

        binding.sendIv.setOnClickListener {
            if(binding.writeMessageEt.text.toString().isNotBlank()) {
                viewModel.sendMessage(binding.writeMessageEt.text.toString())
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                binding.writeMessageEt.setText("")
            }
        }

        binding.messagesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                targetingToEnd = if(newState == RecyclerView.SCROLL_STATE_DRAGGING) false
                else !recyclerView.canScrollVertically(1)
            }
        })
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collect {
                    adapter.submitList(it)
                    if(targetingToEnd && it.size > 1) {
                        targetingToEnd = true
//                        binding.messagesRv.smoothScrollToPosition(it.lastIndex)
                        smoothScroll(it.lastIndex)
                    }
                }
            }
        }
    }

    private fun smoothScroll(targetPosition: Int) {
        val smoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int =
                SNAP_TO_START

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return 4f*super.calculateSpeedPerPixel(displayMetrics)
            }
        }
        smoothScroller.targetPosition = targetPosition
        targetingToEnd = true
        recyclerViewLayoutManager.startSmoothScroll(smoothScroller)
    }
}

operator fun Insets.plus(other: Insets) =
    Insets.of(
        this.left + other.left,
        this.top + other.top,
        this.right + other.right,
        this.bottom + other.bottom,
    )