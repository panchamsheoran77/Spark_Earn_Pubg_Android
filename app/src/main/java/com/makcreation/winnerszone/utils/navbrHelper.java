package com.makcreation.winnerszone.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import com.makcreation.winnerszone.play_secion.MainActivity;
import com.makcreation.winnerszone.R;
import com.makcreation.winnerszone.profile_Activity;
import com.makcreation.winnerszone.result_section.result_Activity;

public class navbrHelper {
    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_result :
                        Intent result = new Intent(context,result_Activity.class);
                     context.startActivity(result);
                        break;
                    case R.id.ic_play :
                        Intent play = new Intent(context,MainActivity.class);
                        context.startActivity(play);
                        break;
                    case R.id.ic_account :
                        Intent profile = new Intent(context,profile_Activity.class);
                        context.startActivity(profile);
                        break;

                }

                return false;
            }
        });
    }
}
