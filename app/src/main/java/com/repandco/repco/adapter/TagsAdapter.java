package com.repandco.repco.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.repandco.repco.ManagerActivity;
import com.repandco.repco.R;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Даниил on 16.09.2017.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagHolder> {

    private final Iterator<String> iterator;
    private Map<String, Boolean> tags;
    private ManagerActivity manager;

    public TagsAdapter(ManagerActivity manager,Map<String, Boolean> tags) {
        this.tags = tags;
        this.manager = manager;
        iterator = tags.keySet().iterator();
    }

    public static class TagHolder extends RecyclerView.ViewHolder {

        public TextView tag;
        public CardView card;
        public TagHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card);
            tag = (TextView) itemView.findViewById(R.id.tag);
        }
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tag,parent,false);
        TagHolder vh = new TagHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(TagHolder holder, int position) {
        final String tagName = iterator.next();
        if(tagName!=null){
            if(holder.tag!=null) holder.tag.setText(tagName);
            if(holder.card!=null){
//                Random rnd = new Random();
//                int color = Color.argb(255, 56+rnd.nextInt(200), 56+rnd.nextInt(200), 56+rnd.nextInt(200));
//
//                holder.card.setCardBackgroundColor(color);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
