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
import java.util.TreeSet;


public class FindsAdapter extends RecyclerView.Adapter<FindsAdapter.FindHolder> {

    private Iterator<String> iterator;
    private TreeSet<String> finds;
    private ManagerActivity manager;
    private TagsAdapter tagsAdapter;
    private boolean create = false;

    public FindsAdapter(ManagerActivity manager) {
        this.manager = manager;
        this.create = false;
    }

    public FindsAdapter(ManagerActivity manager,boolean create) {
        this.manager = manager;
        this.create = create;
    }

    public TreeSet<String> getFinds() {
        return finds;
    }

    public void setFinds(TreeSet<String> finds) {
        this.finds = finds;
        iterator = finds.iterator();
        notifyDataSetChanged();
    }

    public static class FindHolder extends RecyclerView.ViewHolder {

        public TextView find;
        public CardView card;
        public FindHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card);
            find = (TextView) itemView.findViewById(R.id.find);

        }
    }

    @Override
    public FindsAdapter.FindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_find,parent,false);
        FindsAdapter.FindHolder vh = new FindsAdapter.FindHolder(v1);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FindsAdapter.FindHolder holder, int position) {
        final String findName = iterator.next();
        if(findName!=null){
            if(holder.find!=null) holder.find.setText(findName);
            if(holder.card!=null){
                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!create) manager.openSearh(findName);
                        else {
                            if(tagsAdapter!=null){
                                tagsAdapter.addTag(findName);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (finds!=null) ? finds.size() : 0;
    }

    public void setTagsAdapter(TagsAdapter tagsAdapter) {
        this.tagsAdapter = tagsAdapter;
    }
}
