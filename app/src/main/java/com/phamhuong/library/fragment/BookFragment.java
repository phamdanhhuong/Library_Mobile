package com.phamhuong.library.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.phamhuong.library.R;
import com.phamhuong.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {
    private Book book;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        // Nhận dữ liệu từ Bundle
        if (getArguments() != null) {
            book = (Book) getArguments().getSerializable("book");
        }

        if (view.getLayoutParams() != null) {
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        List<Book> booksByAuthor = getBooksByAuthor(book.getAuthor());
        List<Book> booksByCategory = getBooksByCategory(book.getGenre());
        // Load Fragment con
        loadFragment(FragmentBookInfo.newInstance(book.getTitle(), book.getAuthor(), book.getSummary(), book.getCoverUrl()), R.id.fragmentBookInfo);
        loadFragment(FragmentCommentSection.newInstance(book.getId()), R.id.fragmentCommentSection);
        loadFragment(FragmentRelateBook.newInstance(booksByAuthor, booksByCategory), R.id.fragmentRelateBook);


        return view;
    }

    private void loadFragment(Fragment fragment, int containerId) {
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();
    }
    private List<Book> getBooksByAuthor(String author) {
        // Lấy danh sách sách theo tác giả từ nguồn dữ liệu (API, database, v.v...)
        return new ArrayList<>();  // Thay thế bằng dữ liệu thực tế
    }

    private List<Book> getBooksByCategory(String category) {
        // Lấy danh sách sách theo thể loại từ nguồn dữ liệu
        return new ArrayList<>();  // Thay thế bằng dữ liệu thực tế
    }
}
