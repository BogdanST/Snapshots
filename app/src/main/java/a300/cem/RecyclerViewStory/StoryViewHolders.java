package a300.cem.RecyclerViewStory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import a300.cem.DisplayImageActivity;
import a300.cem.R;

public class StoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mEmail;

    public StoryViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        mEmail = itemView.findViewById(R.id.email);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), DisplayImageActivity.class);

        Bundle b = new Bundle();
        b.putString("userId", mEmail.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
