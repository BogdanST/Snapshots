package a300.cem.RecyclerViewReceiver;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import a300.cem.R;

public class ReceiverViewHolders extends RecyclerView.ViewHolder {
    public TextView mEmail;
    public CheckBox mReceive;

    public ReceiverViewHolders(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);
        mReceive = itemView.findViewById(R.id.receive);
    }

}
