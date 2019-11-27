package com.makcreation.winnerszone.result_section;

import android.view.View;

public interface RecyclerViewClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
