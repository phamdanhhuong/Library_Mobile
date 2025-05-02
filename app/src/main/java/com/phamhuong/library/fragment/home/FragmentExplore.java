package com.phamhuong.library.fragment.home;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.LibraryActivity;
import com.phamhuong.library.R;
import com.phamhuong.library.fragment.book.AllBooksFragment;
import com.phamhuong.library.model.ApiResponseT;
import com.phamhuong.library.model.Book;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.service.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentExplore extends Fragment {
    private ImageView imgBook, imgAudioBook;
    APIService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        imgBook = view.findViewById(R.id.imgBook);
        imgAudioBook = view.findViewById(R.id.imgAudioBook);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        View.OnClickListener loadCategoryBooks = v -> {
            String category = "";
            String title = "";
            Call<ApiResponseT<List<Book>>> call = null;
            if (v.getId() == R.id.imgBook) {
                category = "BOOK";
                title = "Sách";
                call = apiService.getAllEBooks();
            } else {
                category = "AUDIOBOOK";
                title = "Audiobook";
                call = apiService.getAllAudioBooks();
            }
            loadBooks(call, title);
        };

        imgBook.setOnClickListener(loadCategoryBooks);
        imgAudioBook.setOnClickListener(loadCategoryBooks);

        return view;
    }
    private void loadBooks(Call<ApiResponseT<List<Book>>> call, String title) {
        call.enqueue(new Callback<ApiResponseT<List<Book>>>() {
            @Override
            public void onResponse(Call<ApiResponseT<List<Book>>> call, Response<ApiResponseT<List<Book>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    ArrayList<Book> books = (ArrayList<Book>) response.body().getData();
                    navigateToAllBooksFragment(title, books);
                } else {
                    Log.e("FragmentExplore", "Failed to load books by category: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponseT<List<Book>>> call, Throwable t) {
                Log.e("FragmentExplore", "Error loading books by category: " + t.getMessage());
            }
        });
    }

    private void navigateToAllBooksFragment(String title, ArrayList<Book> books) {
        AllBooksFragment fragment = AllBooksFragment.newInstance(title, books);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }
}