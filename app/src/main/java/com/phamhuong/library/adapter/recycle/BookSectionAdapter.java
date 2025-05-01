package com.phamhuong.library.adapter.recycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phamhuong.library.R;
import com.phamhuong.library.adapter.home.BookAdapterRelate;
import com.phamhuong.library.fragment.book.AllBooksFragment;
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
            ((HeaderViewHolder) holder).bind(section.title, section, context);
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
        TextView tvViewAllText;
        ImageButton btnViewAll;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSectionTitle);
            tvViewAllText = itemView.findViewById(R.id.tvViewAllText);
            btnViewAll = itemView.findViewById(R.id.btnViewAll);
        }

        void bind(String title, Section section, Context context) {
            tvTitle.setText(title);

            View.OnClickListener viewAllClickListener = v -> {
                ArrayList<Book> sectionBooks = new ArrayList<>((List<Book>) section.items);
                AllBooksFragment fragment = AllBooksFragment.newInstance(section.title, sectionBooks);

                FragmentActivity activity = (FragmentActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();
            };
            btnViewAll.setOnClickListener(viewAllClickListener);
            tvViewAllText.setOnClickListener(viewAllClickListener);
        }
    }


    static class BooksViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvBooks;
        ImageButton btnPrevious;
        ImageButton btnNext;
        LinearLayoutManager layoutManager;
        BooksViewHolder(View itemView) {
            super(itemView);
            rvBooks = itemView.findViewById(R.id.rvSectionBooks);
            rvBooks.setLayoutManager(new LinearLayoutManager(
                itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            layoutManager = new LinearLayoutManager(
                    itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvBooks.setLayoutManager(layoutManager);
            btnPrevious = itemView.findViewById(R.id.btnPrevious); // Tìm nút trong item_book_section.xml
            btnNext = itemView.findViewById(R.id.btnNext);       // Tìm nút trong item_book_section.xml

            rvBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int itemCount = recyclerView.getAdapter() != null ? recyclerView.getAdapter().getItemCount() : 0;

                    if (firstVisibleItemPosition == 0) {
                        btnPrevious.setVisibility(View.GONE);
                    } else {
                        btnPrevious.setVisibility(View.VISIBLE);
                    }

                    if (lastVisibleItemPosition == itemCount - 1 || itemCount == 0) {
                        btnNext.setVisibility(View.GONE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                    }
                }
            });

            // Thiết lập OnClickListener cho các nút
            btnPrevious.setOnClickListener(v -> {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 0) {
                    rvBooks.smoothScrollToPosition(firstVisibleItemPosition - 1);
                }
            });

            btnNext.setOnClickListener(v -> {
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = rvBooks.getAdapter().getItemCount();
                if (lastVisibleItemPosition < itemCount - 1) {
                    rvBooks.smoothScrollToPosition(lastVisibleItemPosition + 1);
                }
            });
        }

        void bind(List<?> items) {
            // Set adapter based on item type (books or categories)
            if (items != null && !items.isEmpty()) { // Check if the list is not null and not empty
                if (items.get(0) instanceof Book) {
                    rvBooks.setAdapter(new BookVerticalAdapter(itemView.getContext(), (List<Book>)items));
                } else {
                    // Handle categories
                    //rvBooks.setAdapter(new CategoryAdapter(itemView.getContext(), (List<Category>)items, ));
                }
            } else {
                // Handle the case where the items list is empty
                // You might want to set a default adapter or hide the RecyclerView
                rvBooks.setAdapter(null); // Or set an empty adapter
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