package com.repandco.repco.adapter;

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


public class TagDeleteAdapter extends RecyclerView.Adapter<TagDeleteAdapter.TagDeleteHolder> {

    private final Iterator<String> iterator;
    private Map<String, Boolean> tags;
    private ManagerActivity manager;

    public TagDeleteAdapter(ManagerActivity manager,Map<String, Boolean> tags) {
        this.tags = tags;
        this.manager = manager;
        iterator = tags.keySet().iterator();
    }

    public static class TagDeleteHolder extends RecyclerView.ViewHolder {

        public TextView tag;
        public CardView card;
        public TagDeleteHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card);
            tag = (TextView) itemView.findViewById(R.id.tag);

        }
    }

    @Override
    public TagDeleteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tag,parent,false);
        TagDeleteHolder vh = new TagDeleteHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(TagDeleteHolder holder, int position) {
        final String tagName = iterator.next();
        if(tagName!=null){
            if(holder.tag!=null) holder.tag.setText(tagName);
            if(holder.card!=null){
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manager.openSearh(tagName);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

}
