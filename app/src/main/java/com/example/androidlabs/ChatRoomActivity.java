package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        List<chatMessage> chatList = new ArrayList<>();

        MyListAdapter myListAdapter = new MyListAdapter(this,chatList);

        ListView theList = findViewById(R.id.lv_chat_dialog);
        theList.setAdapter( myListAdapter );

        final Button btn1 = (Button)findViewById(R.id.btn_chat_message_send);
        final Button btn2 = (Button)findViewById(R.id.btn_chat_message_receive);


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText typeMessage = (EditText)findViewById(R.id.et_chat_message);
//                if (TextUtils.isEmpty(typeMessage.getText().toString())) {
//                    Toast.makeText(ChatRoomActivity.this, "Cannot be null!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                chatMessage chat = new chatMessage();
                chat.setIsSend(true);
                chat.setMessage(typeMessage.getText().toString());
                chatList.add(chat);
                typeMessage.setText("");
                myListAdapter.notifyDataSetChanged();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextView typeMessage = (TextView)findViewById(R.id.et_chat_message);
//                if (TextUtils.isEmpty(typeMessage.getText().toString())) {
//                    Toast.makeText(ChatRoomActivity.this, "Cannot be null!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                chatMessage chat = new chatMessage();
                chat.setIsSend(false);
                chat.setMessage(typeMessage.getText().toString());
                chatList.add(chat);
                typeMessage.setText("");
                myListAdapter.notifyDataSetChanged();
            }
        });

        theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                AlertDialog.Builder alertDialogBiulder = new AlertDialog.Builder (ChatRoomActivity.this);
                alertDialogBiulder.setTitle((ChatRoomActivity.this).getResources().getString(R.string.delete_title));
                alertDialogBiulder.setMessage((ChatRoomActivity.this).getResources().getString(R.string.delete_message1) + position
                        +"\n"+ (ChatRoomActivity.this).getResources().getString(R.string.delete_message2)+ id);
                alertDialogBiulder.setPositiveButton((ChatRoomActivity.this).getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatList.remove(theList.getItemAtPosition(position));
                        myListAdapter.notifyDataSetChanged();
                    }
                });
                alertDialogBiulder.setNegativeButton((ChatRoomActivity.this).getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                alertDialogBiulder.show();
                return true;
            }
        });

}
    private class MyListAdapter extends BaseAdapter {
        private Context context;
        private List<chatMessage> lists;

        public MyListAdapter(Context context, List<chatMessage> lists) {
            super();
            this.context = context;
            this.lists = lists;
        }

        public int getCount() { return lists.size();} //This function tells how many objects to show

        public Object getItem(int position) { return lists.get(position);}  //This returns the string at position p

        public long getItemId(int p) { return p;} //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {
            chatMessage entity = lists.get(p);
            TextView chatMessage;
            boolean isSend = entity.getIsSend();
            LayoutInflater inflater = getLayoutInflater();
            View newView = recycled;

            if (isSend==true) {
                newView = inflater.inflate(R.layout.right_item,parent,false);
                chatMessage = (TextView) newView
                        .findViewById(R.id.right_item);
                chatMessage.setText(entity.getMessage());
            } else {
                newView = inflater.inflate(R.layout.left_item,parent,false);
                chatMessage = (TextView) newView
                        .findViewById(R.id.left_item);
                chatMessage.setText(entity.getMessage());
            }
            return newView;
        }
    }
}
