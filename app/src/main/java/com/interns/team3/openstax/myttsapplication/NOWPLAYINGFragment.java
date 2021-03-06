package com.interns.team3.openstax.myttsapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NOWPLAYINGFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NOWPLAYINGFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NOWPLAYINGFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MOD_TITLE = "param1";
    private static final String ARG_MOD_ID = "param2";
    private static final String ARG_BOOK_ID = "param3";

    public static String modId, bookId, modTitle;
    public TextView titleView;
    public ImageView imageView;
    public Context context =getContext();

    private OnFragmentInteractionListener mListener;

    public LinearLayout availableLayout, unavailableLayout;
    public boolean showPlaying;

    public Button downloadButton;

    public NOWPLAYINGFragment() {
        // Required empty public constructor
        this.setArguments(new Bundle());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NOWPLAYINGFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NOWPLAYINGFragment newInstance(String param1, String param2, String param3) {
        NOWPLAYINGFragment fragment = new NOWPLAYINGFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOD_TITLE, param1);
        args.putString(ARG_MOD_ID, param2);
        args.putString(ARG_BOOK_ID, param3);

        modId = param2;
        fragment.setArguments(args);
        return fragment;
    }

    public void setContext(Context c) {
        context = c;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && modTitle == null) {
            modTitle = getArguments().getString(ARG_MOD_TITLE);
            modId = getArguments().getString(ARG_MOD_ID);
            bookId = getArguments().getString(ARG_BOOK_ID);

        }

        setRetainInstance(true);
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nowplaying, container, false);

        titleView = view.findViewById(R.id.nowPlayingTitle);
        titleView.setText(modTitle);
        titleView.setGravity(Gravity.CENTER_HORIZONTAL);

        imageView = view.findViewById(R.id.nowPlayingImage);

        availableLayout = view.findViewById(R.id.availableLayout);
        unavailableLayout = view.findViewById(R.id.unavailableLayout);

        if(showPlaying) { showPlaying();} else {
            hidePlaying();
            downloadButton = (Button) view.findViewById(R.id.download);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadButton.setEnabled(false);
                    downloadButton.setText("Downloading...");
                    // find some way to check the progress of downloads xD
                    // for now, assume reader only clicks after downloads finish
                    ((MainActivity) getActivity()).downloadEntireModule(bookId, modId);
                }
            });
        }


        // Inflate the layout for this fragment
        return view;
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


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // use this method if you want to do anything once the fragment is back on the screen
        if(showPlaying) showPlaying();
        else hidePlaying();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public String getModule() {
            return modId;
        }


    // when user wants to listen to a new audiobook
    public void setNewModule(String bookId, String modId, String modTitle){

        if(!(this.bookId.equals(bookId))){
            this.bookId = bookId;
            String modified_title= bookId.replaceAll(" ", "_").replaceAll("\\.", "").toLowerCase();
            int drawable_id = getContext().getResources().getIdentifier(modified_title, "drawable", getContext().getPackageName());
            Picasso.with(getContext()).load(drawable_id).into(imageView);
        }

        this.modId = modId;
        this.modTitle = modTitle;

        titleView.setText(modTitle);


    }

    public void showPlaying(){
        showPlaying= true;
        availableLayout.setVisibility(View.VISIBLE);
        String modified_title= bookId.replaceAll(" ", "_").replaceAll("\\.", "").toLowerCase();
        int drawable_id = getContext().getResources().getIdentifier(modified_title, "drawable", getContext().getPackageName());
        Picasso.with(getContext()).load(drawable_id).into(imageView);
        unavailableLayout.setVisibility(View.GONE);
    }


    public void hidePlaying(){
        showPlaying = false;
        availableLayout.setVisibility(View.GONE);
        unavailableLayout.setVisibility(View.VISIBLE);
    }

    public void setShowPlaying(boolean boo){
        showPlaying = boo;
    }



}
