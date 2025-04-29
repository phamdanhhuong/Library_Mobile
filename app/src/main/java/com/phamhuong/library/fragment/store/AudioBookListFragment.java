package com.phamhuong.library.fragment.store;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.book.AudioBookAdapter;
import com.phamhuong.library.fragment.book.AudioBookFragment;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class AudioBookListFragment extends Fragment implements AudioBookAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private AudioBookAdapter adapter;
    private List<Book> audioBooks = new ArrayList<>();
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audiobook_list, container, false);
        recyclerView = view.findViewById(R.id.audioBookRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AudioBookAdapter(getContext(), audioBooks, this);
        recyclerView.setAdapter(adapter);
        loadAudioBooks();
        return view;
    }

    private void loadAudioBooks() {
        progressBar.setVisibility(View.VISIBLE);
        APIService service = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponseT<List<Book>>> call = service.getAllAudioBooks();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Response<ApiResponseT<List<Book>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    ApiResponseT<List<Book>> apiResponse = response.body();
                    List<Book> fetchedAudioBooks = apiResponse.getData();
                    if (fetchedAudioBooks != null) {
                        audioBooks.clear();
                        for (Book book : fetchedAudioBooks) {
                            if (book.getAudioUrl() != null && !book.getAudioUrl().isEmpty()) {
                                audioBooks.add(book);
                            }
                        }
                        adapter.setAudioBooks(audioBooks);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load audiobooks", Toast.LENGTH_SHORT).show();
                    Log.e("AudioBookFragment", "Failed to load audiobooks: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseT<List<Book>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                Log.e("AudioBookListFragment", "Network error loading audiobooks: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(Book audiobook) {
        // Handle item click to replace content_frame with AudioBookFragment
        if (getActivity() != null) {
            AudioBookFragment audioBookFragment = AudioBookFragment.newInstance(audiobook.getId(), audiobook.getTitle(), audiobook.getAudioUrl());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, audioBookFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}