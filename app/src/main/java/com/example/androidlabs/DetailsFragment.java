package com.example.androidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class DetailsFragment extends Fragment {
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_details, container, false);
        dataFromActivity = getArguments();

        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );
        // Inflate the layout for this fragment
        TextView message = (TextView)result.findViewById(R.id.TextView_f_1);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        TextView idView = (TextView)result.findViewById(R.id.TextView_f_2);
        idView.setText("ID=" + id);

        CheckBox checkBox = (CheckBox)result.findViewById(R.id.CheckBox_f);
        if(dataFromActivity.getString(ChatRoomActivity.ITEM_isSEND).equals("true")){
            checkBox.setChecked(!checkBox.isChecked());
        }else {
            checkBox.setChecked(checkBox.isChecked());
        }

        Button finishButton = (Button)result.findViewById(R.id.hide_button);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

}
