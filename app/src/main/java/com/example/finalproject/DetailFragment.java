package com.example.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class DetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    AppCompatActivity activity;
    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
    }
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View save = inflater.inflate(R.layout.fragment_details, container, false);
        TextView url = save.findViewById(R.id.frag_url);
        TextView hd = save.findViewById(R.id.frag_hd);
        Button hide = save.findViewById(R.id.hide);

        Bundle data = getArguments();
        String urlString = data.getString(SavedImages.URL);
        String hdString = data.getString(SavedImages.HD);
//        String id = data.getString(ChatRoomActivity.ITEM_ID);
//        Boolean isTablet = data.getBoolean("isTablet");
//        if(isTablet){
            url.setText("URL: "+urlString);
            hd.setText("HD URL: "+hdString);
            hide.setOnClickListener( v -> activity.getSupportFragmentManager().beginTransaction().remove(this).commit());
        return save;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
