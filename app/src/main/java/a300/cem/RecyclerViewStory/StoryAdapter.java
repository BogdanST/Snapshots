package a300.cem.RecyclerViewStory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import a300.cem.R;
import a300.cem.UserInformation;


public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolders> {

    private List<StoryObject> usersList;
    private Context context;

    public StoryAdapter(List<StoryObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;

    }

    @Override
    public StoryViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_story_item,null);
        StoryViewHolders rcv = new StoryViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolders rcViewHolders, int i) {
        rcViewHolders.mEmail.setText(usersList.get(i).getEmail());
        rcViewHolders.mEmail.setTag(usersList.get(i).getUid());

        rcViewHolders.mLayout.setTag(usersList.get(i).getChatOrStory());



    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
