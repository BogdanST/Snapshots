package a300.cem.RecyclerViewFollow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onBindViewHolder(@NonNull RcViewHolders rcViewHolders, int i) {
        rcViewHolders.mEmail.setText(usersList.get(i).getEmail());

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
