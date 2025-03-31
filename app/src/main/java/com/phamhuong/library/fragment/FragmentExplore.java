package com.phamhuong.library.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.LibraryActivity;
import com.phamhuong.library.R;

public class FragmentExplore extends Fragment {
    private ImageView imgBook, imgAudioBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        imgBook = view.findViewById(R.id.imgBook);
        imgAudioBook = view.findViewById(R.id.imgAudioBook);

        View.OnClickListener openLibrary = v -> {
            Intent intent = new Intent(getActivity(), LibraryActivity.class);
            if (v.getId() == R.id.imgBook) {
                intent.putExtra("CATEGORY", "BOOK");
            } else {
                intent.putExtra("CATEGORY", "AUDIOBOOK");
            }
            startActivity(intent);
        };

        imgBook.setOnClickListener(openLibrary);
        imgAudioBook.setOnClickListener(openLibrary);

        return view;
    }
}