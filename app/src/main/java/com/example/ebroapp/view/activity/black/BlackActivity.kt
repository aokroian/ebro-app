package com.example.ebroapp.view.activity.black

import android.view.LayoutInflater
import com.example.ebroapp.databinding.ActivityBlackBinding
import com.example.ebroapp.view.base.BaseActivity

class BlackActivity : BaseActivity<ActivityBlackBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityBlackBinding =
        ActivityBlackBinding::inflate
}