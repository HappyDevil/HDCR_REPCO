//package com.repandco.repco;
//
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class RecycleViewNewsAdapter extends RecyclerView.Adapter<RecycleViewNewsAdapter.ViewHolder> {
//
//    private List mList;
//
//    public RecycleViewNewsAdapter() {
////        this.mList = list;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View itemView = inflater.inflate(R.layout.adapter_info_post, parent, false);
////        View itemView1 = inflater.inflate(R.layout.adapter_news, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.textTittle.setText("New Post");
//        holder.textDescription.setText("Test Test Test Test TestTestTest TestTest TestTest TestTestTest Test TestTest Test");
//        holder.textPostuler.setText("Robot Android");
//        holder.textCOO.setText("COO 134");
//        holder.imageView.setImageResource(R.drawable.x2010_1x);
//    }
//
//    @Override
//    public int getItemCount() {
////        return mList.size();
//        int i = 15;
//        return i;
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        private TextView textTittle;
//        private TextView textDescription;
//        private TextView textPostuler;
//        private TextView textCOO;
////        private TextView textDate;
//        private ImageView imageView;
////        private RecyclerView rv1;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            textTittle = (TextView)itemView.findViewById(R.id.textView);
//            textDescription = (TextView)itemView.findViewById(R.id.textView3);
//            textPostuler = (TextView)itemView.findViewById(R.id.textView4);
//            textCOO = (TextView)itemView.findViewById(R.id.textView5);
////            textDate = (TextView)itemView.findViewById(R.id.textView2);
//            imageView = (ImageView)itemView.findViewById(R.id.imageView3);
////            rv1 = (RecyclerView)itemView.findViewById(R.id.rec);
//            itemView.setTag(itemView);
//        }
//    }
//}