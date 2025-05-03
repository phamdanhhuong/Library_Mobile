package com.phamhuong.library.fragment.book;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.AudioPlayerViewModel;
import com.phamhuong.library.model.NotificationType;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;
import com.phamhuong.library.utils.CustomDialogHelper;
import com.phamhuong.library.utils.NotificationHelper;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioBookFragment extends Fragment {

    private int bookId;
    private String bookTitle;
    private String audioUrl;
    private String bookAuthor; // You might need to pass author as well
    private String coverUrl;
    private AudioPlayerViewModel audioPlayerViewModel;
    private ImageView audioBookCoverImageView;
    private TextView audioBookTitleTextView;
    private TextView audioBookAuthorTextView;
    private SeekBar audioSeekBar;
    private ImageButton playPauseButton;
    private TextView currentTimeTextView;
    private TextView totalTimeTextView;
    private ImageButton next5sButton;
    private ImageButton previous5sButton, btnAddToWishlist, backButton;
    Spinner speedSpinner;
    private ArrayAdapter<CharSequence> speedAdapter;

    public static AudioBookFragment newInstance(int bookId, String bookTitle, String bookAuthor, String audioUrl, String coverUrl) {
        AudioBookFragment fragment = new AudioBookFragment();
        Bundle args = new Bundle();
        args.putInt("bookId", bookId);
        args.putString("bookTitle", bookTitle);
        args.putString("audioUrl", audioUrl);
        args.putString("coverUrl", coverUrl);
        args.putString("bookAuthor", bookAuthor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getInt("bookId");
            bookTitle = getArguments().getString("bookTitle");
            audioUrl = getArguments().getString("audioUrl");
            bookAuthor = getArguments().getString("bookAuthor");
            coverUrl = getArguments().getString("coverUrl");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_book, container, false);
        audioBookCoverImageView = view.findViewById(R.id.audioBookCoverImageView);
        audioBookTitleTextView = view.findViewById(R.id.audioBookTitleTextView);
        audioBookAuthorTextView = view.findViewById(R.id.audioBookAuthorTextView);
        audioSeekBar = view.findViewById(R.id.audioSeekBar);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        currentTimeTextView = view.findViewById(R.id.currentTimeTextView);
        totalTimeTextView = view.findViewById(R.id.totalTimeTextView);
        next5sButton = view.findViewById(R.id.Next5sButton);
        previous5sButton = view.findViewById(R.id.Previous5sButton);
        btnAddToWishlist = view.findViewById(R.id.btnAddToWishlist);
        speedSpinner = view.findViewById(R.id.playbackSpeedSpinner);
        backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        speedAdapter = (ArrayAdapter<CharSequence>) speedSpinner.getAdapter();
        speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String speedStr = parent.getItemAtPosition(position).toString().replace("x", "");
                float speed = Float.parseFloat(speedStr);
                audioPlayerViewModel.setPlaybackSpeed(speed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnAddToWishlist.setOnClickListener(v -> {
            addToWishlist();
        });

        audioPlayerViewModel = new ViewModelProvider(requireActivity()).get(AudioPlayerViewModel.class);

        if (bookTitle != null) {
            audioBookTitleTextView.setText(bookTitle);
        }
        if (bookAuthor != null) {
            audioBookAuthorTextView.setText(bookAuthor);
        }

        Glide.with(this)
                .load(coverUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.img_profile_book_more)
                        .error(R.drawable.img_profile_book_more))
                .into(audioBookCoverImageView);


        if (audioUrl != null) {
            audioPlayerViewModel.playAudio(audioUrl, bookTitle, true);
        } else {
            Toast.makeText(requireContext(), "Audio URL not provided", Toast.LENGTH_SHORT).show();
        }
        next5sButton.setOnClickListener(v -> {
            Long current = audioPlayerViewModel.currentPosition().getValue();
            Long total = audioPlayerViewModel.duration().getValue();
            if (current != null && total != null) {
                long newPos = Math.min(current + 5000, total);
                audioPlayerViewModel.seekTo(newPos);
            }
        });

        previous5sButton.setOnClickListener(v -> {
            Long current = audioPlayerViewModel.currentPosition().getValue();
            if (current != null) {
                long newPos = Math.max(current - 5000, 0);
                audioPlayerViewModel.seekTo(newPos);
            }
        });

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isCurrentlyPlaying = audioPlayerViewModel.isPlaying().getValue();
                if (isCurrentlyPlaying != null) {
                    if (isCurrentlyPlaying) {
                        audioPlayerViewModel.pauseAudio();
                        playPauseButton.setImageResource(R.drawable.ic_play);
                    } else {
                        if (audioUrl != null && audioPlayerViewModel.duration().getValue() != null && audioPlayerViewModel.duration().getValue() > 0) {
                            audioPlayerViewModel.resumeAudio();
                            playPauseButton.setImageResource(R.drawable.ic_pause);
                        }
                    }
                } else {
                    // Handle initial state if isPlaying is null
                    if (audioUrl != null && audioPlayerViewModel.duration().getValue() != null && audioPlayerViewModel.duration().getValue() > 0) {
                        audioPlayerViewModel.playAudio(audioUrl, bookTitle, true);
                        playPauseButton.setImageResource(R.drawable.ic_pause);
                    }
                }
            }
        });

        audioSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioPlayerViewModel.seekTo((long) progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle touch start
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Handle touch stop
            }
        });

        observeLiveData();

        if (speedAdapter != null) {
            for (int i = 0; i < speedAdapter.getCount(); i++) {
                if (speedAdapter.getItem(i).toString().equals("1x")) {
                    speedSpinner.setSelection(i);
                    break;
                }
            }
        }
        return view;
    }

    private void observeLiveData() {
        audioPlayerViewModel.isPlaying().observe(getViewLifecycleOwner(), isPlaying -> {
            if (isPlaying != null) {
                playPauseButton.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });

        audioPlayerViewModel.duration().observe(getViewLifecycleOwner(), duration -> {
            if (duration != null) {
                audioSeekBar.setMax(duration.intValue());
                totalTimeTextView.setText(formatTime(duration));
                Log.d("AudioBookFragment", "Audio Duration: " + formatTime(duration));
            }
        });

        audioPlayerViewModel.currentPosition().observe(getViewLifecycleOwner(), position -> {
            if (position != null) {
                audioSeekBar.setProgress(position.intValue());
                currentTimeTextView.setText(formatTime(position));
            }
        });
    }

    private String formatTime(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long seconds = Math.abs(totalSeconds % 60);
        long minutes = Math.abs(totalSeconds / 60);
        return String.format("%02d:%02d", minutes, seconds);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Dừng audio khi người dùng chuyển sang màn hình khác
        audioPlayerViewModel.stopAudio(); // Hoặc gọi audioPlayerViewModel.pauseAudio(); nếu muốn tạm dừng
    }
    void addToWishlist() {
        APIService apiServiceWishlist = RetrofitClient.getRetrofit().create(APIService.class);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        int userId = dbHelper.getLoginInfoSQLite().getUserId();
        Map<String, Integer> requestBodyWishlist = new HashMap<>();
        requestBodyWishlist.put("user_id", userId);
        requestBodyWishlist.put("book_id", bookId);

        // Initialize NotificationHelper
        NotificationHelper notificationHelper = new NotificationHelper(getContext());

        apiServiceWishlist.addToWishList(requestBodyWishlist).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body().isStatus()) {
                    CustomDialogHelper.showWishlistSuccessPopup(getActivity());
                    // Gọi hàm tạo thông báo từ NotificationHelper
                    notificationHelper.createNotification(
                            String.valueOf(userId),
                            "Đã thêm vào Yêu thích",
                            "Bạn đã thêm cuốn sách \"" + bookTitle + "\" vào danh sách yêu thích.",
                            NotificationType.RECOMMENDATION.name()
                    );

                } else {
                    CustomDialogHelper.showWishlistFailurePopup(getActivity());
                    Toast.makeText(getContext(), "Thêm vào danh sách yêu thích thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("", "Error: " + t.getMessage());
            }
        });
    }
}