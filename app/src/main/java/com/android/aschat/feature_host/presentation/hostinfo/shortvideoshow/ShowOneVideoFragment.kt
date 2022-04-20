package com.android.aschat.feature_host.presentation.hostinfo.shortvideoshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.android.aschat.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView


/**
 * 作为展示一个视频的fragment
 */
class ShowOneVideoFragment(private val mVideoPath: String): Fragment() {

    private lateinit var mPlayerView: StyledPlayerView
    private lateinit var mPlayButton: ImageView
    private lateinit var mPauseButton: View
    private lateinit var mPlayer: ExoPlayer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.host_showshortvideo_one_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidget()
    }

    private fun initWidget() {
        mPlayerView = requireView().findViewById(R.id.host_detail_one_video_playerview)
        mPlayButton = requireView().findViewById(R.id.host_detail_one_video_play)
        mPauseButton = requireView().findViewById(R.id.host_detail_one_video_pause)
        mPlayer = ExoPlayer.Builder(requireContext()).build()
        mPlayerView.player = mPlayer

        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri(mVideoPath)
        mPlayer.apply {
            // Set the media item to be played.
            setMediaItem(mediaItem)
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
            addListener(object :Player.Listener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        mPlayButton.visibility = View.INVISIBLE
                    }else {
                        mPlayButton.visibility = View.VISIBLE
                    }
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    super.onIsLoadingChanged(isLoading)
                    if (isLoading) {
                        mPlayButton.visibility = View.INVISIBLE
                    }
                }
            })
            play()
        }
        mPauseButton.setOnClickListener {
            if (mPlayer.isPlaying) {
                mPlayer.pause()
            }else {
                mPlayer.play()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPlayer.release()
    }
}