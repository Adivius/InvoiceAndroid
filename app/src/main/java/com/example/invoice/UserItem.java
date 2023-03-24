package com.example.invoice;


import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

class UserItem extends Item<GroupieViewHolder> {

    public User user;

    public UserItem(User user) {
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView textView = viewHolder.itemView.findViewById(R.id.item_user_username);
        textView.setText(user.NAME);
    }

    @Override
    public int getLayout() {
        return R.layout.item_user;
    }
}