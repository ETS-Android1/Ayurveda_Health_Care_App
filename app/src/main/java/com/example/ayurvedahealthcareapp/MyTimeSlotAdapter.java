package com.example.ayurvedahealthcareapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time_slot, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time_slot.setText(new StringBuilder(convertTimeSlotToString(position)).toString());
        //if all slots available show the list
        if(timeSlotList.size() == 0){
            holder.time_slot_text.setText("Available");
            holder.time_slot_text.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
        else {
            for(TimeSlot slotValue:timeSlotList){

                int slot = Integer.parseInt(String.valueOf(slotValue.getSlot()));
                if(slot == position){
                    holder.card_time_slot.setCardBackgroundColor(Color.parseColor("#299125"));
                    holder.time_slot_text.setText("Booked!");
                    holder.time_slot_text.setTextColor(context.getResources().getColor(android.R.color.white));
                    holder.time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                }
            }
        }
    }

    private String convertTimeSlotToString(int slot) {
        switch (slot){
            case 0:
                return "9.30 - 9.45";
            case 1:
                return "9.45 - 10.00";
            case 2:
                return "10.00 - 10.15";
            case 3:
                return "10.15 - 10.30";
            case 4:
                return "10.30 - 10.45";
            case 5:
                return "10.45 - 11.00";
            case 6:
                return "11.00 - 11.15";
            case 7:
                return "11.15 - 11.30";
            case 8:
                return "11.30 - 11.45";
            case 9:
                return "11.45 - 12.00";
            case 10:
                return "12.30 - 12.45";
            case 11:
                return "12.45 - 13.00";
            case 12:
                return "13.00 - 13.15";
            case 13:
                return "13.15 - 13.30";
            case 14:
                return "13.30 - 13.45";
            case 15:
                return "13.45 - 14.00";
            case 16:
                return "14.00 - 14.15";
            case 17:
                return "14.15 - 14.30";
            case 18:
                return "14.30 - 14.45";
            case 19:
                return "14.45 - 15.00";
            case 20:
                return "15.00 - 15.15";
            case 21:
                return "15.15 - 15.30";
            case 22:
                return "15.30 - 15.45";
            case 23:
                return "15.45 - 16.00";
            case 24:
                return "16.00 - 16.15";
            case 25:
                return "16.15 - 16.30";
            case 26:
                return "16.30 - 16.45";
            case 27:
                return "16.45 - 17.00";
            case 28:
                return "17.00 - 17.15";
            case 29:
                return "17.15 - 17.30";
            case 30:
                return "17.30 - 17.45";
            case 31:
                return "17.45 - 18.00";
            case 32:
                return "18.00 - 18.15";
            case 33:
                return "18.15 - 18.30";
            case 34:
                return "18.30 - 18.45";
            case 35:
                return "18.45 - 19.00";
            case 36:
                return "19.00 - 19.15";
            case 37:
                return "19.15 - 19.30";
            case 38:
                return "19.30 - 19.45";
            case 39:
                return "19.45 - 20.00";
            default:
                return "Closed";

        }
    }

    @Override
    public int getItemCount() {
        return 40;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time_slot,time_slot_text;
        CardView card_time_slot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
            time_slot = (TextView) itemView.findViewById(R.id.time);
            time_slot_text = (TextView) itemView.findViewById(R.id.available);
        }
    }
}
