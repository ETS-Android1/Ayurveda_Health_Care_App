package com.example.ayurvedahealthcareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends FirestoreRecyclerAdapter<Doctor, DoctorAdapter.docotorViewholder> {

    public DoctorAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull docotorViewholder holder, int position, @NonNull Doctor model) {

        holder.dName.setText(model.getfName());
        holder.rate.setRating(model.getRating());
        Glide.with(holder.img.getContext()).load(model.getpUrl()).into(holder.img);
    }

    @NonNull
    @Override
    public docotorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doctor, parent,false);
        return new docotorViewholder(view);
    }

    class docotorViewholder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView dName;
        RatingBar rate;
        public docotorViewholder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.imageView2);
            dName = (TextView)itemView.findViewById(R.id.textView1);
            rate = (RatingBar)itemView.findViewById(R.id.doc_rating);
        }
    }
}
