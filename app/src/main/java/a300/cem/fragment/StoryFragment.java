package a300.cem.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import a300.cem.R;
import a300.cem.RecyclerViewStory.StoryAdapter;
import a300.cem.RecyclerViewStory.StoryObject;
import a300.cem.UserInformation;

public class StoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public static StoryFragment newInstance(){
        StoryFragment fragment = new StoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_fragment , container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);

        //The recyclerView should not have a fixed size since the list can go up. A user can follow multiple 'friends'
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new StoryAdapter(getDataSet(),getContext());
        mRecyclerView.setAdapter(mAdapter);

        Button mRefresh = view.findViewById(R.id.refresh);
        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                mAdapter.notifyDataSetChanged();
                listenForData();
            }
        });

        return view;

    }


    //Clear the database
    public void clear(){
        int size = this.results.size();
        this.results.clear();
        mAdapter.notifyItemRangeChanged(0,size);
    }

    //Get the results
    private ArrayList<StoryObject> results = new ArrayList<>();
    public ArrayList<StoryObject> getDataSet(){
        listenForData();
        return results;
    }

    //Check the story database for each followed user
    public void listenForData(){
        for(int i = 0; i<UserInformation.listFollowing.size(); i++){
            DatabaseReference followingStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(UserInformation.listFollowing.get(i));
            followingStoryDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.child("email").getValue().toString();
                    String uid = dataSnapshot.getRef().getKey();
                    long timestampBeg = 0;
                    long timestampEnd = 0;

                    for(DataSnapshot storySnapshot: dataSnapshot.child("story").getChildren()){
                        if(storySnapshot.child("timestampBeg").getValue()!=null){
                            timestampBeg = Long.parseLong(storySnapshot.child("timestampBeg").getValue().toString());
                        }
                        if(storySnapshot.child("timestampEnd").getValue()!=null){
                            timestampEnd = Long.parseLong(storySnapshot.child("timestampEnd").getValue().toString());
                        }

                        long timestampCurrent = System.currentTimeMillis();
                        if(timestampCurrent >= timestampBeg && timestampCurrent <= timestampEnd){
                            //add to recyclerView Story
                            StoryObject obj = new StoryObject(email,uid,"story");
                            if(!results.contains(obj)){
                                results.add(obj);
                                mAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}