package com.skch.khmd.khmdreader0x;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerItemClickListener implements ListView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }


    private void selectItem(int position) {
        Fragment fragment = new NavigationDrawerFragment();
        Bundle args = new Bundle();

    }

}
