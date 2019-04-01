package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

// Displays a list of comments for a particular landmark.
public class CommentFeedActivity extends AppCompatActivity {

    private static final String TAG = CommentFeedActivity.class.getSimpleName();
    private String username;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    FirebaseDatabase database;
    DatabaseReference bear;

    // UI elements
    EditText commentInputBox;
    RelativeLayout layout;
    Button sendButton;
    Toolbar mToolbar;

    /* TODO: right now mRecyclerView is using hard coded comments.
     * You'll need to add functionality for pulling and posting comments from Firebase
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_feed);


        // entry point to access your Firebase database
        database = FirebaseDatabase.getInstance();

        DatabaseReference landMarks = database.getReference("Landmarks");


        Intent intent = getIntent();
        // TODO: replace this with the name of the landmark the user chose
        username = intent.getStringExtra("username");
        String landmarkName = intent.getStringExtra("landmark");
        bear = landMarks.child(landmarkName);

        // hook up UI elements
        layout = (RelativeLayout) findViewById(R.id.comment_layout);
        commentInputBox = (EditText) layout.findViewById(R.id.comment_input_edit_text);
        sendButton = (Button) layout.findViewById(R.id.send_button);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        // sets the app bar's title
        mToolbar.setTitle(landmarkName + ": Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create a new value event listener
        ValueEventListener myDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set a breakpoint in this method and run in debug mode!!
                // this will be called each time `someRef` or one of its children is modified
                HashMap<String, HashMap> children = (HashMap<String, HashMap>) dataSnapshot.getValue();
                SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
                mComments = new ArrayList<>();

                for (String date: children.keySet()) {
                    Date d = null;
                    try {
                        d = df.parse(date);
                        Comment c = new Comment((String) children.get(date).get("comment"), (String) children.get(date).get("username"),d);
                        mComments.add(c);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                setAdapterAndUpdateData();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };

        bear.addValueEventListener(myDataListener);

        // create an onclick for the send button
        setOnClickForSendButton();

        // make some test comment objects that we add to the recycler view
        //makeTestComments();

        // use the comments in mComments to create an adapter. This will populate mRecyclerView
        // with a custom cell (with comment_cell_layout) for each comment in mComments
        //setAdapterAndUpdateData();
    }

    // TODO: delete me
    private void makeTestComments() {
        String randomString = "hello world hello world ";
        Comment newComment = new Comment(randomString, "test_user1", new Date());
        Comment hourAgoComment = new Comment(randomString + randomString, "test_user2", new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        Comment overHourComment = new Comment(randomString, "test_user3", new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
        Comment dayAgoComment = new Comment(randomString, "test_user4", new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)));
        Comment daysAgoComment = new Comment(randomString + randomString + randomString, "test_user5", new Date(System.currentTimeMillis() - (48 * 60 * 60 * 1000)));
        mComments.add(newComment);mComments.add(hourAgoComment); mComments.add(overHourComment);mComments.add(dayAgoComment); mComments.add(daysAgoComment);

    }

    private void setOnClickForSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInputBox.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    commentInputBox.requestFocus();
                } else {
                    // clear edit text, post comment
                    commentInputBox.setText("");
                    postNewComment(comment);
                }
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new CommentAdapter(this, mComments);
        mRecyclerView.setAdapter(mAdapter);

        // scroll to the last comment
        mRecyclerView.smoothScrollToPosition(mComments.size() - 1);
    }

    private void postNewComment(String commentText) {
        Date date = new Date();
        Comment newComment = new Comment(commentText, username, date);
        DatabaseReference childBear = bear.child(date.toString());
        childBear.child("username").setValue(username);
        childBear.child("comment").setValue(commentText);
        mComments.add(newComment);
        setAdapterAndUpdateData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
