package com.example.and_p4_bakeking.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.and_p4_bakeking.R;
import com.example.and_p4_bakeking.models.Recipe;
import com.example.and_p4_bakeking.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class ExoPlayerFragment extends Fragment implements Player.EventListener {
    private static final String LOG_TAG = ExoPlayerFragment.class.getSimpleName();

    private static final String STEP_BUNDLE_KEY = "step_bundle_key";
    private static final String CURRENT_STEP_STATE = "step_state";
    private SimpleExoPlayer mExoplayer;
    private PlayerView mPlayerView;
    private Step mCurrentStep;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    // Required empty public constructor
    public ExoPlayerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP_STATE);
        }else {
            Bundle bundle = this.getArguments();
            if(bundle != null){
                mCurrentStep = bundle.getParcelable(STEP_BUNDLE_KEY);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_exoplayer, container, false);
        mPlayerView = rootView.findViewById(R.id.playerView);

        //Hide the playView if there is no video
        //Otherwise initialize the mediaPlayer
        if (mCurrentStep.getVideoURL().equals("")){
                mPlayerView.setVisibility(View.GONE);
        } else {
            //Parse the currentStep video url into a uri and init the MediaPlayer & MediaSession
            initMediaSession();
            initMediaPlayer(Uri.parse(mCurrentStep.getVideoURL()));
        }

        return rootView;
    }

    //Initalize MediaSession to enable callback for external clients
    private void initMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), LOG_TAG);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        mMediaSession.setPlaybackState((mStateBuilder.build()));
        //Handle callbacks in the PlaySessionCallback inner class
        mMediaSession.setCallback(new PlayerSessionCallback());
        mMediaSession.setActive(true);
    }

    //Initialize the Exoplayer
    //@param Uri of the video to play
    private void initMediaPlayer(Uri videoUri) {
        if (mExoplayer == null){
        //Create an instance of the Exoplayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            DefaultRenderersFactory defaultRenderersFactory= new DefaultRenderersFactory(getContext());
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getContext(), defaultRenderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);
            //Set the Exoplayer to listen to fragment changes
            mExoplayer.addListener(this);
            //Prepare the mediaSource object to represent the media to play
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (mExoplayer != null){
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }


    }

    //Release the player when the activity is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if(mMediaSession != null){
            mMediaSession.setActive(false);
        }
    }

    //Save the state of the app so video could be retrieved on rotation
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP_STATE, mCurrentStep);
    }


    //Exoplayer state change listener, called whenever the stage changes regardless of source
    //Updates the MediaSession to keep playback in sync
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoplayer.getCurrentPosition(), 1f);
        }else if ((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoplayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

    }


    //Media Session callbacks for external clients
    private class PlayerSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay(){
            mExoplayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause(){
            mExoplayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious(){
            mExoplayer.seekTo(0);
        }


    }

}
