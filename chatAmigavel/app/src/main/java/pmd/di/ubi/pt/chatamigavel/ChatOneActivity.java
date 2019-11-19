package pmd.di.ubi.pt.chatamigavel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatOneActivity extends AppCompatActivity  {

    public static final String MESSAGES_CHILD = "messages";
    private EditText messageToSend;
    private ImageButton sendBtt, sendTimeBtt;
    private int myId;
    private int comId;
    private String concIds;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseReference databaseReference;
    private final Context context = this;
    public int counter;
    String idAux;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        myId = 102;
        comId = 101;
        concIds =""+(myId*comId);

        sendBtt = (ImageButton)findViewById(R.id.send_message);
        messageToSend = (EditText)findViewById(R.id.enter_message);
        sendTimeBtt = (ImageButton)findViewById(R.id.send_message_time);

        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    message.setId(dataSnapshot.getKey());
                    idAux = message.getId();
                }
                return message;
            }
        };

        DatabaseReference messagesRef = databaseReference.child(MESSAGES_CHILD).child(concIds);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.messagem_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message message) {
                        holder.setDetails(message,myId,context,concIds);                          }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int MessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (MessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(mFirebaseAdapter);


        sendBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new
                        Message(messageToSend.getText().toString(), comId);
                databaseReference.child(MESSAGES_CHILD).child(concIds)
                        .push().setValue(message);
                messageToSend.setText("");


            }
        });

        sendTimeBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Messagem com tempo");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int auxTime = Integer.parseInt(input.getText().toString());
                        Message message = new
                                Message(messageToSend.getText().toString(), comId);
                        databaseReference.child(MESSAGES_CHILD).child(concIds)
                                .push().setValue(message);
                        messageToSend.setText("");
                        counter = 0;

                        new CountDownTimer(1000*auxTime, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                counter++;
                            }

                            @Override
                            public void onFinish() {
                                databaseReference.child("messages").child(concIds).child(idAux).removeValue();

                            }
                        }.start();
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Put actions for CANCEL button here, or leave in blank
                    }
                });
                alert.show();


            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        mFirebaseAdapter.startListening();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
