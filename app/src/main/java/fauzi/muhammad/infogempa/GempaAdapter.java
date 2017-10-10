package fauzi.muhammad.infogempa;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by fauzi on 10/10/2017.
 */

public class GempaAdapter extends RecyclerView.Adapter<GempaAdapter.GempaHolder>{

    List<Gempa> data;

    public void setData(List<Gempa> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public GempaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        GempaHolder holder = new GempaHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GempaHolder holder, int position) {
        Gempa gempa = data.get(position);
        View v = holder.itemView;
        if(gempa.magnitude >= 4.5){
            v.setBackgroundColor(
                    v.getResources().getColor(R.color.colorBackgroundDanger));
            holder.mTextMagnitude.setTextColor(
                    v.getResources().getColor(R.color.colorDanger));
        }else{
            v.setBackgroundColor(Color.WHITE);
            holder.mTextMagnitude.setTextColor(Color.BLACK);
        }

        holder.mTextMagnitude.setText(String.format("%3.1f", gempa.magnitude));
        holder.mTextDepth.setText(String.valueOf(gempa.depth) + " km");
        holder.mTextPlace.setText(gempa.place);
        holder.mTextCoor.setText(gempa.longitude+","+gempa.latitude);
        SimpleDateFormat formatter = new SimpleDateFormat("Y-M-d H:m:s");
        holder.mTextDate.setText(formatter.format(gempa.date));
        holder.mTsunami.setImageResource(gempa.tsunami == 1 ? R.drawable.ic_tsunami : R.color.putih);
    }

    @Override
    public int getItemCount() {
        if(data != null) {
            return data.size();
        }
        return 0;
    }


    public class GempaHolder extends RecyclerView.ViewHolder{

        TextView mTextMagnitude;
        TextView mTextPlace;
        TextView mTextDate;
        TextView mTextDepth;
        TextView mTextCoor;
        ImageView mTsunami;

        public GempaHolder(View itemView) {
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
