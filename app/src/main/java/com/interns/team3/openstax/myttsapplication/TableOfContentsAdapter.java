package com.interns.team3.openstax.myttsapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TableOfContentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Content> dataSet;
    public View.OnClickListener modOnClickListener = new ModOnClickListener();
    public View.OnClickListener bookOnClickListener = new BookOnClickListener();


    public static class ModOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            String modId = ((TextView) v.findViewById(R.id.modID)).getText().toString();
            //Toast.makeText(getApplicationContext(), targetId, Toast.LENGTH_SHORT).show();

            Intent intent = ((Activity) v.getContext()).getIntent();
            String bookId = intent.getStringExtra("Book ID");

            intent = new Intent(v.getContext(), TextbookView.class);
            intent.putExtra("Module ID", modId);
            intent.putExtra("Book ID", bookId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }


    public static class BookOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            String targetId = ((TextView) v.findViewById(R.id.book_id)).getText().toString();
            //Toast.makeText(getApplicationContext(), targetId, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(v.getContext(), TableOfContentsActivity.class);
            intent.putExtra("Book ID", targetId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView modID;
        public TextView modTitle;
        public TextView modChapter;



        public ModuleViewHolder(View v) {
            super(v);
            modID = (TextView) v.findViewById(R.id.modID);
            modTitle = (TextView) v.findViewById(R.id.modTitle);
            modChapter = (TextView) v.findViewById(R.id.modNum);

        }


    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView chapterNum;
        public TextView chapterTitle;
        public ChapterViewHolder(View v) {
            super(v);
            chapterNum = (TextView) v.findViewById(R.id.chapterNum);
            chapterTitle = (TextView) v.findViewById(R.id.chapterTitle);
        }
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle;
        public ImageView bookImg;
        public TextView bookId;
        public BookViewHolder(View v) {
            super(v);
            bookTitle = (TextView) v.findViewById(R.id.book_title);
            bookId = (TextView) v.findViewById(R.id.book_id);
        }
    }

    public TableOfContentsAdapter(ArrayList<Content> dataSet)
    {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        // 0 = module, 1 = chapter
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Content item = dataSet.get(position);
        if(item instanceof Content.Module)
            return 0;
        else if(item instanceof Content.Chapter)
            return 1;
        else if(item instanceof Content.Book)
            return 2;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: // Module
            {
                // create a new view
                View v = (View) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.toc_module, parent, false);

                TableOfContentsAdapter.ModuleViewHolder vh = new TableOfContentsAdapter.ModuleViewHolder(v);
                v.setOnClickListener(modOnClickListener);

                return vh;
            }
            case 1: // Chapter
            {
                // create a new view
                View v = (View) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.toc_chapter, parent, false);

                TableOfContentsAdapter.ChapterViewHolder vh = new TableOfContentsAdapter.ChapterViewHolder(v);
                return vh;
            }
            case 2: // Book
                View v = (View) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.textbook_card, parent, false);

                TableOfContentsAdapter.BookViewHolder vh = new TableOfContentsAdapter.BookViewHolder(v);
                v.setOnClickListener(bookOnClickListener);
                return vh;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0: {
                ModuleViewHolder moduleViewHolder = (ModuleViewHolder) holder;
                TextView myModID = moduleViewHolder.modID;
                TextView myModTitle = moduleViewHolder.modTitle;
                TextView myModChapter = moduleViewHolder.modChapter;

                Content.Module module = (Content.Module) dataSet.get(position);
                String section_num = module.getSectionNum();
                String title = module.getTitle();


                myModID.setText(module.getId());

                String tempModuleTitle;
                if (!(module.getTitle().equals("Introduction")))
                    tempModuleTitle = section_num + " " + title;
                else tempModuleTitle = title;

                myModTitle.setText(tempModuleTitle);

                myModChapter.setText(section_num);
                //System.out.println("MODULE: \t" + dataSet.get(position).id + "\t" + dataSet.get(position).title + "\t" + dataSet.get(position).chapter);
                break;
            }
            case 1: {
                ChapterViewHolder chapterViewHolder = (ChapterViewHolder) holder;
                TextView myChapterNum = chapterViewHolder.chapterNum;
                TextView myChapterTitle = chapterViewHolder.chapterTitle;

                Content.Chapter chapter = (Content.Chapter) dataSet.get(position);
                String chapter_num = chapter.getChapterNum();
                String title = chapter.getTitle();

                myChapterNum.setText(chapter_num);

                String tempChapterTitle = chapter_num + " " + title;
                myChapterTitle.setText(tempChapterTitle);
                // System.out.println("CHAPTER: \t" + dataSet.get(position).title + "\t" + dataSet.get(position).chapter);
                break;
            }
            case 2: {
                BookViewHolder bookViewHolder = (BookViewHolder) holder;
                TextView myBookTitle = bookViewHolder.bookTitle;
                TextView myBookId = bookViewHolder.bookId;

                Content.Book book = (Content.Book) dataSet.get(position);
                String title= book.getTitle();
                String id = book.getId();

                myBookTitle.setText(title);
                myBookId.setText(id);

                break;
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
