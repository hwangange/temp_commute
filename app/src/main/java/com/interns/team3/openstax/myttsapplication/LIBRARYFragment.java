package com.interns.team3.openstax.myttsapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LIBRARYFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LIBRARYFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LIBRARYFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SectionedRecyclerViewAdapter sectionAdapter;

    public ArrayList<LibraryItem> favorites_dataSet, downloads_dataSet;



    public LIBRARYFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LIBRARYFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LIBRARYFragment newInstance(String param1, String param2) {
        LIBRARYFragment fragment = new LIBRARYFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        (getActivity()).setTitle("My Library");
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        /* Finding Favorites */
        // Shared Preferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("library", 0);
        HashSet<String> faves = (HashSet<String>) sharedPreferences.getStringSet("favorites", new HashSet<String>());
        favorites_dataSet = new ArrayList<LibraryItem>();
        for(String s : faves)
        {
            favorites_dataSet.add(makeLibraryItem(s));
        }

        /* Finding Downloads */
        HashSet<String> downloads = (HashSet<String>) sharedPreferences.getStringSet("downloads", new HashSet<String>());
        downloads_dataSet = new ArrayList<LibraryItem>();

        for(String s : downloads)
        {
            downloads_dataSet.add(makeLibraryItem(s));
        }


        sectionAdapter.addSection("favorites", new CustomSection("Favorites", favorites_dataSet));
        sectionAdapter.addSection("downloads", new CustomSection("Downloads", downloads_dataSet));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.library_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);
        sectionAdapter.notifyDataSetChanged();


        // Inflate the layout for this fragment
        return view;
    }

    public LibraryItem makeLibraryItem(String tag){
        String[] ary = tag.split("_");
        return new LibraryItem(ary[0], ary[1], ary[2]);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onDestroy(){

        super.onDestroy();
    }

    public void addFavorite(String tag) {
        favorites_dataSet.add(makeLibraryItem(tag));
        sectionAdapter.notifyDataSetChanged();
    }

    public void removeFavorite(String tag){
        String modId = makeLibraryItem(tag).getModId();
        for(LibraryItem li : favorites_dataSet){
            if(li.getModId().equals(modId)){
                favorites_dataSet.remove(li);
            }
        }
        sectionAdapter.notifyDataSetChanged();
    }

    public void addDownload(String tag){
        downloads_dataSet.add(makeLibraryItem(tag));
        sectionAdapter.notifyDataSetChanged();
    }

    public void removeDownload(String tag){
        String modId = makeLibraryItem(tag).getModId();
        for(LibraryItem li : downloads_dataSet){
            if(li.getModId().equals(modId)){
                downloads_dataSet.remove(li);
            }
        }
        sectionAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class CustomSection extends StatelessSection {
        String title;
        ArrayList<LibraryItem> list;

        CustomSection(String title, ArrayList<LibraryItem> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.section_item)
                    .headerResourceId(R.layout.section_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            LibraryItem item = list.get(position);
            String modTitle = item.getModTitle();

            itemHolder.tvItem.setText(modTitle);

            String bookTitle = item.getBookTitle();
            String modId = item.getModId();

            ImageView bookImg = itemHolder.imgItem;
            String modified_title= bookTitle.replaceAll(" ", "_").replaceAll("\\.", "").toLowerCase();
            int drawable_id = getContext().getResources().getIdentifier(modified_title, "drawable", getContext().getPackageName());
            Picasso.with(getContext()).load(drawable_id).into(bookImg);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                    ((MainActivity)getActivity()).playEntireModule(bookTitle, modId, modTitle);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvItem;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvItem = (TextView) view.findViewById(R.id.tvItem);
        }
    }

    public class LibraryItem {
        private String bookTitle;
        private String modTitle;
        private String modId;

        LibraryItem(String bookTitle, String modTitle, String modId) {
            this.bookTitle= bookTitle;
            this.modTitle = modTitle;
            this.modId = modId;
        }

        public String getBookTitle(){ return bookTitle; }
        public String getModTitle() {return modTitle; }
        public String getModId() { return modId; }
    }

}
