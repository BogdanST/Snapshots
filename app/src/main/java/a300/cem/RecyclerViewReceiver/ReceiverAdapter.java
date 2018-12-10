package a300.cem.RecyclerViewReceiver;

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


public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverViewHolders> {

    private List<ReceiverObject> usersList;
    private Context context;

    public ReceiverAdapter(List<ReceiverObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;

    }

    @Override
    public ReceiverViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_receiver_item,null);
        ReceiverViewHolders rcv = new ReceiverViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReceiverViewHolders rcViewHolders, int i) {
        rcViewHolders.mEmail.setText(usersList.get(i).getEmail());

        rcViewHolders.mReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean receiveState = !usersList.get(rcViewHolders.getLayoutPosition()).getReceive();
                usersList.get(rcViewHolders.getLayoutPosition()).setReceive(receiveState);

            }
        });


    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
