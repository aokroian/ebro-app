package com.example.ebroapp.view.fragment.musicfull

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.ebroapp.App
import com.example.ebroapp.databinding.FragmentMusicFullBinding
import com.example.ebroapp.domain.entity.song.SongListItem.Companion.TYPE_SEPARATOR
import com.example.ebroapp.domain.entity.song.SongListItem.Companion.TYPE_SONG
import com.example.ebroapp.domain.repository.DomainRepository
import com.example.ebroapp.utils.setImageFromUri
import com.example.ebroapp.utils.setTime
import com.example.ebroapp.view.adapter.MusicAdapter
import com.example.ebroapp.view.base.BaseFragment


class MusicFullFragment :
    BaseFragment<FragmentMusicFullBinding, MusicFullViewModel>(MusicFullViewModel::class.java) {

    private val player = App.get().player

    private val songAdapter by lazy {
        MusicAdapter { song ->
            player.nextSong(song)
            binding.btnPlay.isChecked = true
            fillCurrentSong()
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMusicFullBinding =
        FragmentMusicFullBinding::inflate

    override fun setupUI() {
        viewModel.songs.observe(viewLifecycleOwner) {
            songAdapter.addItems(it)
        }

        viewModel.insertedSongs.observe(viewLifecycleOwner) {
            songAdapter.changeItem(it.first, it.second)
        }

        initAdapter()

        player.setOnPlayerStateChangeListener { progress, duration ->
            binding.pbMusic.progress = if (duration != 0) progress * PERCENT / duration else 0
            binding.tvTimer.setTime(duration / PERCENT * progress)
        }

        binding.btnFavorite.setOnClickListener {
            player.currentSong?.let {
                DomainRepository.obtain().setSongIsFavorite(it.id, binding.btnFavorite.isChecked)
                it.isFavorites = binding.btnFavorite.isChecked
                viewModel.getInsertedSong(it.id)
            }
        }

        binding.btnPlay.apply {
            isChecked = player.isPlaying()
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) player.playMusic()
                else player.pauseMusic()
            }
        }

        binding.btnNextSong.setOnClickListener {
            player.nextSong()
            fillCurrentSong()
        }
        binding.btnPreviousSong.setOnClickListener {
            player.previousSong()
            fillCurrentSong()
        }

        binding.tvRadio.setOnClickListener { showServiceUnavailable() }
        binding.tvStreaming.setOnClickListener { showServiceUnavailable() }

        fillCurrentSong()
    }

    private fun showServiceUnavailable() {
        Toast.makeText(requireContext(), "Service is unavailable", Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 10).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (songAdapter.getItemViewType(position)) {
                        TYPE_SONG -> 1
                        TYPE_SEPARATOR -> 10
                        else -> -1
                    }
                }
            }
        }
        binding.rvSongs.apply {
            layoutManager = gridLayoutManager
            adapter = songAdapter
        }
        viewModel.getSongs()
    }

    private fun fillCurrentSong() {
        player.currentSong?.let { song ->
            binding.ivAlbumCover.setImageFromUri(song.uri)
            binding.tvName.text = song.name
            binding.tvSinger.text = song.singer
            binding.tvAlbum.text = song.album
            binding.btnFavorite.isChecked = song.isFavorites
        }
    }

    companion object {
        private const val PERCENT = 100
    }
}