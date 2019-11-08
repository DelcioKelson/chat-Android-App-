package pmd.di.ubi.pt.chatamigavel;

import android.app.Activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private EditText editText;
    private ListView messagesView;
    private ArrayList b;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        b = new ArrayList<String>();
        mAdapter = new MyAdapter(b);
        recyclerView.setAdapter(mAdapter);

    }

    public void sendMessage(View view) {
        editText = (EditText) findViewById(R.id.enter_message);

            if (b==null){
                b = new ArrayList<String>();
                b.add(editText.getText().toString());

            }else {
                b.add(editText.getText().toString());
            }
        mAdapter = new MyAdapter(b);
        recyclerView.setAdapter(mAdapter);

            editText.getText().clear();
        }

    }

