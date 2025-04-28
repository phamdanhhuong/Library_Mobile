package com.phamhuong.library.fragment.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

public class BookFragment extends Fragment {
    private Book book;
    private ImageButton btnBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        // Nhận dữ liệu từ Bundle
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Thực hiện hành động quay lại
            FragmentManager fm = getParentFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                // Nếu không có fragment nào trong back stack, có thể đóng Activity
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        if (view.getLayoutParams() != null) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        // Load Fragment con
        loadFragment(FragmentBookInfo.newInstance(book), R.id.fragmentBookInfo);
        loadFragment(FragmentCommentSection.newInstance(book.getId()), R.id.fragmentCommentSection);
        loadFragment(FragmentRelateBook.newInstance(book.getAuthor(), book.getGenre()), R.id.fragmentRelateBook);

        return view;
    }

    private void loadFragment(Fragment fragment, int containerId) {
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
}
