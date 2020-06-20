package com.example.finalproject;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DetailsFragment extends Fragment {
    private Bundle dataFromActivity;
    private long Nid;
    private String NHeadline;
    private boolean IsTablet;
    private AppCompatActivity parentActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        Nid = dataFromActivity.getLong(SaveddbArticles.F_ID );
        NHeadline = dataFromActivity.getString(SaveddbArticles.F_HEADLINE);
        IsTablet = dataFromActivity.getBoolean("isTablet");
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.details_fragment, container, false);

        TextView Header = result.findViewById(R.id.header);
        //show the message
        TextView ID = result.findViewById(R.id.fID);
        ID.setText("Id = "+ Nid);

        TextView Headline = result.findViewById(R.id.fhead);
        Headline.setText("Headline = "+ NHeadline);



        // get the delete button, and add a click listener:
        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            if(!IsTablet) {
                EmptyActivity ac = (EmptyActivity) getActivity();
                ac.finish();
            }
        });

        return result;}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}

