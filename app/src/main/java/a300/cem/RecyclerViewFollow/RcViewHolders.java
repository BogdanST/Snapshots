package a300.cem.RecyclerViewFollow;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a300.cem.R;

public class RcViewHolders extends RecyclerView.ViewHolder {
    public TextView mEmail;
    public Button mFollow;

    public RcViewHolders(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);
        mFollow = itemView.findViewById(R.id.follow);
    }

}
