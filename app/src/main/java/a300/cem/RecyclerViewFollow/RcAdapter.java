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


public class RcAdapter extends RecyclerView.Adapter<RcViewHolders> {

    private List<UsersObject> usersList;
    private Context context;

    public RcAdapter(List<UsersObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;

    }

    @Override
    public RcViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_followers_item,null);
        RcViewHolders rcv = new RcViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHolders rcViewHolders, int i) {
        rcViewHolders.mEmail.setText(usersList.get(i).getEmail());

        rcViewHolders.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(rcViewHolders.mFollow.getText().equals("Follow")){
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
