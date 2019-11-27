package com.makcreation.winnerszone.wallet_stuff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makcreation.winnerszone.R;

import java.util.List;

public class transAdpter extends RecyclerView.Adapter<transAdpter.transctionViewHolder> {
    private Context mContx;
    private List<transList>transLists;

    public transAdpter(Context mContx, List<transList> transLists) {
        this.mContx = mContx;
        this.transLists = transLists;
    }

    @NonNull
    @Override
    public transctionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContx).inflate(R.layout.transaction_layout,viewGroup,false);
        return new transctionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull transctionViewHolder holder, int i) {
    transList list = transLists.get(i);
        holder.sn.setText(list.getSno());
        holder.amt.setText("₹"+list.getAmt());
        holder.id.setText(list.getId());
    }

    @Override
    public int getItemCount() {
        Log.e("pancham", "getItemCount: "+transLists.size() );
        return transLists.size();
    }

    class transctionViewHolder extends RecyclerView.ViewHolder{
        TextView sn,amt,id;
        public transctionViewHolder(@NonNull View itemView) {
            super(itemView);
            sn = itemView.findViewById(R.id.transSN);
            amt = itemView.findViewById(R.id.transRupee);
            id = itemView.findViewById(R.id.transId);

        }
    }
}
