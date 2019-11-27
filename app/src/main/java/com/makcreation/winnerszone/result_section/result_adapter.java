package com.makcreation.winnerszone.result_section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.makcreation.winnerszone.R;
import java.util.List;

public class result_adapter extends RecyclerView.Adapter<result_adapter.MyViewholder> {
    private List<result_recycler_stuff> result_recycler_stuffs;
    private Context mContext;
    private RecyclerViewClickListener recyclerViewClickListener;

    public void setOnItemClickListener(RecyclerViewClickListener recyclerViewItemClickListener){
        this.recyclerViewClickListener=recyclerViewItemClickListener;
    }

    public result_adapter(List<result_recycler_stuff> result_recycler_stuffs,Context mContext) {
        this.result_recycler_stuffs = result_recycler_stuffs;
        this.mContext = mContext;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_result_design,parent,false);

        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
result_recycler_stuff result_recycler_stuff = result_recycler_stuffs.get(position);
holder.position = position;
holder.matchno.setText(result_recycler_stuff.getMatchno());
holder.date_txt.setText(result_recycler_stuff.getDate_time());
holder.map.setText(result_recycler_stuff.getMap().toUpperCase());
        switch(result_recycler_stuff.getMap()){
            case "erangle":
                holder.iv.setImageResource(R.drawable.maprev);
                break;
            case "sanhok":
                holder.iv.setImageResource(R.drawable.map3rev);
                break;
            case "miramar":
                holder.iv.setImageResource(R.drawable.map2rev);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return result_recycler_stuffs.size();
    }

    public class MyViewholder  extends RecyclerView.ViewHolder {
         TextView matchno, date_txt, map;
        ImageView iv;
        public int position = 0;

         MyViewholder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickListener.onItemClick(view, position);
                }
            });
            matchno = itemView.findViewById(R.id.result_match_no);
            date_txt = itemView.findViewById(R.id.result_date_time);
            map = itemView.findViewById(R.id.result_map_text);
            iv = itemView.findViewById(R.id.result_imageView);
        }
        }
    }