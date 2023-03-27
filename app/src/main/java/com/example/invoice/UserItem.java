package com.example.invoice;


import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

class UserItem extends Item<GroupieViewHolder> {

    public String NAME;

    public UserItem(String name) {
        this.NAME = name;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView textView = viewHolder.itemView.findViewById(R.id.item_user_username);
        textView.setText(NAME);
    }

    @Override
    public int getLayout() {
        return R.layout.item_user;
    }
}