package a300.cem.RecyclerViewStory;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a300.cem.R;

public class StoryViewHolders extends RecyclerView.ViewHolder {
    public TextView mEmail;

    public StoryViewHolders(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);

    }

}
