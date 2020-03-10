package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_isSEND = "isSEND";

    List<chatMessage> chatList = new ArrayList<>();
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        loadDataFromDatabase();
        MyListAdapter myListAdapter = new MyListAdapter(this,chatList);

        ListView theList = findViewById(R.id.lv_chat_dialog);
        theList.setAdapter( myListAdapter );

        final Button btn1 = (Button)findViewById(R.id.btn_chat_message_send);
        final Button btn2 = (Button)findViewById(R.id.btn_chat_message_receive);

        boolean isTablet = findViewById(R.id.frameLayout) != null;

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText typeMessage = (EditText)findViewById(R.id.et_chat_message);
                String s = typeMessage.getText().toString();
//                if (TextUtils.isEmpty(typeMessage.getText().toString())) {
//                    Toast.makeText(ChatRoomActivity.this, "Cannot be null!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                //Get the Data Repository in write mode
                DbHandler dbOpener = new DbHandler(ChatRoomActivity.this);
                db = dbOpener.getWritableDatabase();
                //Create a new map of values, where column names are the keys
                ContentValues cValues = new ContentValues();
                cValues.put(DbHandler.COL_MESSAGE, s);
                cValues.put(DbHandler.COL_SEND, "true");
                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(DbHandler.TABLE_NAME,null, cValues);

                chatMessage chat = new chatMessage();
                chat.setIsSend(true);
                chat.setMessage(s);
                chat.setId(newRowId);
                chatList.add(chat);
                typeMessage.setText("");
                myListAdapter.notifyDataSetChanged();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextView typeMessage = (TextView)findViewById(R.id.et_chat_message);
                String s = typeMessage.getText().toString();
//                if (TextUtils.isEmpty(typeMessage.getText().toString())) {
//                    Toast.makeText(ChatRoomActivity.this, "Cannot be null!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                DbHandler dbOpener = new DbHandler(ChatRoomActivity.this);
                db = dbOpener.getWritableDatabase();
                ContentValues cValues = new ContentValues();
                cValues.put(DbHandler.COL_MESSAGE, s);
                cValues.put(DbHandler.COL_SEND, "false");
                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(DbHandler.TABLE_NAME,null, cValues);

                chatMessage chat = new chatMessage();
                chat.setIsSend(false);
                chat.setMessage(s);
                chat.setId(newRowId);
                chatList.add(chat);
                typeMessage.setText("");
                myListAdapter.notifyDataSetChanged();
            }
        });


        theList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (ChatRoomActivity.this);
                alertDialogBuilder.setTitle((ChatRoomActivity.this).getResources().getString(R.string.delete_title));
                alertDialogBuilder.setMessage((ChatRoomActivity.this).getResources().getString(R.string.delete_message1) + position
                        +"\n"+ (ChatRoomActivity.this).getResources().getString(R.string.delete_message2)+ id);
                alertDialogBuilder.setPositiveButton((ChatRoomActivity.this).getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    chatMessage selected = chatList.get(position);
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessageFromDb(selected);
                        chatList.remove(theList.getItemAtPosition(position));
                        if(isTablet){
                            Fragment fragmentA = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                            getSupportFragmentManager().beginTransaction().remove(fragmentA).commit();
                        }
                        myListAdapter.notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton((ChatRoomActivity.this).getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                alertDialogBuilder.show();
                return true;
            }
        });

        theList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, chatList.get(position).getMessage());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);
            if(chatList.get(position).getIsSend()==true){
                dataToPass.putString(ITEM_isSEND, "true");
            }else{
                dataToPass.putString(ITEM_isSEND, "false");
            }

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

}

    private void loadDataFromDatabase()
    {
        //get a database connection:
        DbHandler dbOpener = new DbHandler(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {DbHandler.COL_ID, DbHandler.COL_SEND , DbHandler.COL_MESSAGE};
        Cursor results = db.query(false, DbHandler.TABLE_NAME, columns, null, null, null,
                null, null, null);

        int Message_ColIndex = results.getColumnIndex(DbHandler.COL_MESSAGE);
        int IsSend_ColIndex = results.getColumnIndex(DbHandler.COL_SEND);
        int idColIndex = results.getColumnIndex(DbHandler.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {    Boolean isSend;
            String message = results.getString(Message_ColIndex);
            String isSend0 = results.getString(IsSend_ColIndex);
            if(isSend0.equals("true")){
            isSend = true;
            }else isSend = false;

            long id = results.getLong(idColIndex);
            //add the new Contact to the array list:
            chatList.add(new chatMessage(message, isSend, id));
        }
            printCursor(results,db.getVersion());

    }

    protected void deleteMessageFromDb(chatMessage c)
    {
        db.delete(DbHandler.TABLE_NAME, DbHandler.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
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

        public long getItemId(int p) { return lists.get(p).getId();} //This returns the database id of the item at position p

        public View getView(int p, View recycled, ViewGroup parent)
        {
            chatMessage entity = lists.get(p);
            TextView chatMessage;
            boolean isSend = entity.getIsSend();
            LayoutInflater inflater = getLayoutInflater();
            View newView = recycled;

            if (isSend==true) {
                newView = inflater.inflate(R.layout.right_item,parent,false);
                chatMessage = (TextView) newView.findViewById(R.id.right_item);
                chatMessage.setText(entity.getMessage());
            } else {
                newView = inflater.inflate(R.layout.left_item,parent,false);
                chatMessage = (TextView) newView.findViewById(R.id.left_item);
                chatMessage.setText(entity.getMessage());
            }
            return newView;
        }
    }

    public void printCursor( Cursor c, int version){
        Log.i("DEBUG_TAG", "Database version number is "+ version
                + " || number of columns " + c.getColumnCount()
                +" || number of results " + c.getCount());

        String rowHeader = "|| ";
        for(int i = 0; i< c.getColumnCount();i++){
            rowHeader = rowHeader.concat(c.getColumnName(i)+" || ");
        }
        Log.i("DEBUG_TAG","Columns "+ rowHeader);

        c.moveToFirst();
        while(c.isAfterLast()==false){
            String rowResults = "|| ";
            for(int i = 0; i< c.getColumnCount();i++){
                rowResults = rowResults.concat(c.getString(i)+" || ");
            }
            Log.i("DEBUG_TAG","Row "+ c.getPosition()+ " : " + rowResults);
            c.moveToNext();
        }
    }
}
