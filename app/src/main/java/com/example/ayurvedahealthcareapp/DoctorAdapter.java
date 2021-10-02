package com.example.ayurvedahealthcareapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.docotorViewholder> implements Filterable {

    Context context;
    List<Doctor> doctorList;
    List<Doctor> doctorListFull;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        doctorListFull = new ArrayList<>(doctorList);
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public docotorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctor, parent,false);
        return new docotorViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull docotorViewholder holder, int position) {
        holder.dName.setText(doctorList.get(position).getfName());
        holder.phone.setText(doctorList.get(position).getPhone());
        holder.rate.setRating((float)doctorList.get(position).getRating());
        Glide.with(holder.img.getContext()).load(doctorList.get(position).getpUrl()).into(holder.img);

        if(!cardViewList.contains(holder.cardView)){
            cardViewList.add(holder.cardView);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));

                Context context = view.getContext();

                Intent intent2 = new Intent(context,Booking.class);
                intent2.putExtra("Doctor Selected",doctorList.get(pos).getDoctorId());
                localBroadcastManager.sendBroadcast(intent2);
                context.startActivity(intent2);

            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    @Override
    public Filter getFilter() {
        return docfilter;
    }


    class docotorViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        TextView dName,phone;
        RatingBar rate;
        CardView cardView;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public docotorViewholder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.imageView2);
            dName = (TextView)itemView.findViewById(R.id.textView1);
            phone = (TextView)itemView.findViewById(R.id.phoneNum);
            rate = (RatingBar)itemView.findViewById(R.id.doc_rating);
            cardView = itemView.findViewById(R.id.doc_card);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v,getBindingAdapterPosition());
        }
    }

    public Filter docfilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Doctor> filteredList =  new ArrayList<>();

            if(constraint.toString() == null || constraint.toString().length() == 0){
                filteredList.addAll(doctorListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Doctor item: doctorListFull){
                    if(item.getfName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            doctorList.clear();
            doctorList.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };
}
