package com.example.ebroapp.view.fragment.lowertoolbar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEachIndexed
import com.example.ebroapp.R
import com.example.ebroapp.databinding.FragmentLowerToolbarBinding
import com.example.ebroapp.utils.setOnSeekBarListener
import com.example.ebroapp.view.base.BaseFragment

class LowerToolbarFragment : BaseFragment<FragmentLowerToolbarBinding>() {

    private val dotActive = R.drawable.ic_dot_active
    private val dotInactive = R.drawable.ic_dot_inactive

    private val leftSeatMax: Int = 3
    private val rightSeatMax: Int = 3
    private val fanMax: Int = 5

    private var leftSeatState: Int = 0
    private var frontWindowState: Boolean = false
    private var seatPositionState: Boolean = false
    private var fanState: Int = 0
    private var rearWindowState: Boolean = false
    private var rightSeatState: Int = 0

    private var leftTemp: Int = 20
    private var rightTemp: Int = 20


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLowerToolbarBinding =
        FragmentLowerToolbarBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareUpperButtons()
        prepareTemperatureBars()
    }

    private fun prepareUpperButtons() {
        binding.llFrontWindow
            .setOnClickListener {
                frontWindowState = !frontWindowState
                onToggleButtonClick(it, frontWindowState)
            }
        binding.llSeatPosition
            .setOnClickListener {
                seatPositionState = !seatPositionState
                onToggleButtonClick(it, seatPositionState)
            }
        binding.llRearWindow
            .setOnClickListener {
                rearWindowState = !rearWindowState
                onToggleButtonClick(it, rearWindowState)
            }

        binding.llLeftSeat
            .setOnClickListener {
                leftSeatState++
                if (leftSeatState > leftSeatMax) leftSeatState = 0
                setActiveDots(it as ViewGroup, leftSeatState)
            }

        binding.llRightSeat
            .setOnClickListener {
                rightSeatState++
                if (rightSeatState > rightSeatMax) rightSeatState = 0
                setActiveDots(it as ViewGroup, rightSeatState)
            }

        binding.llFan
            .setOnClickListener {
                fanState++
                if (fanState > fanMax) fanState = 0
                setActiveDots(it as ViewGroup, fanState)
            }

        setActiveDots(binding.llLeftSeat, leftSeatState)
        setActiveDots(binding.llRightSeat, rightSeatState)
        setActiveDots(binding.llFan, fanState)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun prepareTemperatureBars() {
        binding.skLsTemperature.setOnSeekBarListener {
            this.leftTemp = it
            setTemperatureLabel(binding.tvLsTemperature, this.leftTemp)
        }
        binding.skRsTemperature.setOnSeekBarListener {
            this.rightTemp = it
            setTemperatureLabel(binding.tvRsTemperature, this.rightTemp)
        }

        binding.btnDecreaseLsTemperature.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (this.leftTemp > 15) {
                    this.leftTemp--
                    setTemperatureLabel(binding.tvLsTemperature, this.leftTemp)
                    binding.skLsTemperature.progress = this.leftTemp - 15
                }
            }
            true
        }
        binding.btnIncreaseLsTemperature.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (this.leftTemp < 26) {
                    this.leftTemp++
                    setTemperatureLabel(binding.tvLsTemperature, this.leftTemp)
                    binding.skLsTemperature.progress = this.leftTemp - 15
                }
            }
            true
        }
        binding.btnDecreaseRsTemperature.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (this.rightTemp > 15) {
                    this.rightTemp--
                    setTemperatureLabel(binding.tvRsTemperature, this.rightTemp)
                    binding.skRsTemperature.progress = this.rightTemp - 15
                }
            }
            true
        }
        binding.btnIncreaseRsTemperature.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (this.rightTemp < 26) {
                    this.rightTemp++
                    setTemperatureLabel(binding.tvRsTemperature, this.rightTemp)
                    binding.skRsTemperature.progress = this.rightTemp - 15
                }
            }
            true
        }
        setTemperatureLabel(binding.tvLsTemperature, this.leftTemp)
        setTemperatureLabel(binding.tvRsTemperature, this.rightTemp)
        binding.skLsTemperature.progress = this.leftTemp - 15
        binding.skRsTemperature.progress = this.rightTemp - 15
    }

    private fun onToggleButtonClick(v: View, newValue: Boolean): Boolean {
        if (v is ViewGroup) {
            getImageView(v).let {
                it.setColorFilter(resources.getColor(if (newValue) R.color.text_white else R.color.text_gray));
            }
            return true
        }
        return false
    }

    private fun getImageView(v: ViewGroup): ImageView {
        return v.children.first { it is ImageView } as ImageView
    }

    private fun setActiveDots(v: ViewGroup, value: Int): Boolean {
        val layout = v.children.first { it is LinearLayout } as LinearLayout
        layout.forEachIndexed { ind, v ->
            if (ind < value) {
                (v as ImageView).setImageResource(dotActive)
                v.scaleX = 4f
                v.scaleY = 4f
            } else {
                (v as ImageView).setImageResource(dotInactive)
                v.scaleX = 1f
                v.scaleY = 1f
            }
        }

        (v.children.first { it is ImageView } as ImageView)
            .setColorFilter(resources.getColor(if (value == 0) R.color.text_gray else R.color.text_white))
        return true
    }

    private fun setTemperatureLabel(tempView: TextView, value: Int) {
        if (value < 16) {
            tempView.text = "LO"
        } else if (value > 25) {
            tempView.text = "HI"
        } else {
            tempView.text = "$value\u00B0"
        }
    }
}