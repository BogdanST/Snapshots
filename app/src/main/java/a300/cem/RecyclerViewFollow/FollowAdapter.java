package a300.cem.RecyclerViewFollow;

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


public class FollowAdapter extends RecyclerView.Adapter<FollowViewHolders> {

    private List<FollowObject> usersList;
    private Context context;

    public FollowAdapter(List<FollowObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;

    }

    @Override
    public FollowViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_followers_item,null);
        FollowViewHolders rcv = new FollowViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowViewHolders rcViewHolders, int i) {
        rcViewHolders.mEmail.setText(usersList.get(i).getEmail());

        //!Change button from follow to following!
        if(UserInformation.listFollowing.contains(usersList.get(rcViewHolders.getLayoutPosition()).getUid())){
            rcViewHolders.mFollow.setText("following");
        }else{
            rcViewHolders.mFollow.setText("follow");
        }

        rcViewHolders.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(rcViewHolders.mFollow.getText().equals("follow")){
                    rcViewHolders.mFollow.setText("following");
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following").child(usersList.get(rcViewHolders.getLayoutPosition()).getUid()).setValue(true);
                }else{
                    rcViewHolders.mFollow.setText("follow");
                    FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("following").child(usersList.get(rcViewHolders.getLayoutPosition()).getUid()).removeValue();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
