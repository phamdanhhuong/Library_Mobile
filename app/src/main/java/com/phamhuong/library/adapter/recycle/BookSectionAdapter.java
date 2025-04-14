package com.phamhuong.library.adapter.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.home.BookAdapterRelate;
import com.phamhuong.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BOOKS = 1;

    private List<Section> sections;
    private Context context;

    public BookSectionAdapter(Context context) {
        this.context = context;
        this.sections = new ArrayList<>();
        setupSections();
    }
    public void addSection(String title, List<?> items) {
        sections.add(new Section(title, items));
        notifyItemRangeInserted(sections.size() * 2 - 2, 2);
    }
    private void setupSections() {
        // Add sections
//        addSection("For you", getSampleBooks());
//        addSection("Top selling", getSampleBooks());
//        addSection("New releases", getSampleBooks());
//        addSection("Top free", getSampleBooks());
//        addSection("Categories", getSampleBooks());
//        sections.add(new Section("For you", getSampleBooks()));
//        sections.add(new Section("Top selling", getSampleBooks()));
//        sections.add(new Section("New releases", getSampleBooks()));
//        sections.add(new Section("Top free", getSampleBooks()));
//        sections.add(new Section("Categories", getSampleBooks()));
    }

    private List<Book> getSampleBooks() {
        List<Book> sampleBooks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Book book = new Book();
            book.setTitle("Book Title " + i);
            book.setAuthor("Author " + i);
            // Set other necessary properties of the book here
            sampleBooks.add(book);
        }
        return sampleBooks;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_HEADER : TYPE_BOOKS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_section_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_section_books, parent, false);
            return new BooksViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Section section = sections.get(position / 2);
        
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(section.title);
        } else if (holder instanceof BooksViewHolder) {
            ((BooksViewHolder) holder).bind(section.items);
        }
    }

    @Override
    public int getItemCount() {
        return sections.size() * 2; // Each section has a header and content
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSectionTitle);
        }

        void bind(String title) {
            tvTitle.setText(title);
        }
    }

    static class BooksViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvBooks;

        BooksViewHolder(View itemView) {
            super(itemView);
            rvBooks = itemView.findViewById(R.id.rvSectionBooks);
            rvBooks.setLayoutManager(new LinearLayoutManager(
                itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        void bind(List<?> items) {
            // Set adapter based on item type (books or categories)
            if (items.get(0) instanceof Book) {
                rvBooks.setAdapter(new BookAdapterRelate(itemView.getContext(), (List<Book>)items));
            } else {
                // Handle categories
                //rvBooks.setAdapter(new CategoryAdapter(itemView.getContext(), (List<Category>)items, ));
            }
        }
    }

    static class Section {
        String title;
        List<?> items;

        Section(String title, List<?> items) {
            this.title = title;
            this.items = items;
        }
    }

}