package fauzi.muhammad.infogempa;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

// Created by fauzi on 10/10/2017.

class GempaAdapter extends RecyclerView.Adapter<GempaAdapter.GempaHolder>{

    private List<Gempa> data;

    public void setData(List<Gempa> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public GempaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        return new GempaHolder(view);
    }

    @Override
    public void onBindViewHolder(GempaHolder holder, int position) {
        Gempa gempa = data.get(position);
        View v = holder.itemView;
        if(gempa.magnitude >= 4.5){
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorBackgroundDanger));
            holder.mTextMagnitude.setTextColor(ContextCompat.getColor(v.getContext(),R.color.colorDanger));
        }else{
            v.setBackgroundColor(Color.WHITE);
            holder.mTextMagnitude.setTextColor(Color.BLACK);
        }

        holder.mTextMagnitude.setText(String.format("%3.1f", gempa.magnitude));
        String d = String.valueOf(gempa.depth)+" "+v.getContext().getString(R.string.satuan_jarak);
        holder.mTextDepth.setText(d);
        holder.mTextPlace.setText(gempa.place);
        holder.mTextCoor.setText(gempa.longitude+","+gempa.latitude);
        SimpleDateFormat formatter = new SimpleDateFormat("Y-M-d H:m:s", Locale.getDefault());
        holder.mTextDate.setText(formatter.format(gempa.date));
        //holder.mTsunami.setImageResource(gempa.tsunami == 1 ? R.drawable.ic_tsunami : R.color.putih);
        holder.mTsunami.setVisibility(gempa.tsunami == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        if(data != null) {
            return data.size();
        }
        return 0;
    }


    class GempaHolder extends RecyclerView.ViewHolder{

        TextView mTextMagnitude;
        TextView mTextPlace;
        TextView mTextDate;
        TextView mTextDepth;
        TextView mTextCoor;
        ImageView mTsunami;

        GempaHolder(View itemView) {
            super(itemView);
            mTextMagnitude = itemView.findViewById(R.id.text_magnitude);
            mTextPlace = itemView.findViewById(R.id.text_place);
            mTextDate = itemView.findViewById(R.id.text_date);
            mTextDepth = itemView.findViewById(R.id.text_depth);
            mTextCoor = itemView.findViewById(R.id.textCoor);
            mTsunami = itemView.findViewById(R.id.icTsunami);
        }
    }
}
