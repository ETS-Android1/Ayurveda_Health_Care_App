package com.example.ayurvedahealthcareapp;


import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;

public class Medicine_MyAdapter extends RecyclerView.Adapter<Medicine_MyAdapter.Medicine_MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Medicine_News> mnewsArrayList;
    ArrayList<Medicine_News> mnewsArrayListFull;

    public Medicine_MyAdapter(Context context, ArrayList<News> newsArrayList) {
        this.context = context;
        this.mnewsArrayListFull = mnewsArrayList;
        this.mnewsArrayList=new ArrayList<>(mnewsArrayListFull);
    }

    @NonNull
    @Override
    public Medicine_MyAdapter.Medicine_MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.medicine_list_item,parent,false);
        return new Medicine_MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Medicine_MyAdapter.Medicine_MyViewHolder holder, int position) {
        Medicine_News mnews=mnewsArrayList.get(position);
        holder.mtvHeading.setText(mnews.mheading);
        holder.mtitleImage.setImageResource(mnews.mtitleImage);
        holder.mbriefNews.setText(mnews.mbreifNews);

        boolean isVisible = mnews.visibility;
        holder.mconstraintLayout.setVisibility(isVisible ? View.VISIBLE: View.GONE);
    }

    @Override
    public int getItemCount() {
        return mnewsArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }
    public  final Filter newsFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Medicine_News> filteredNewsList=new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filteredNewsList.addAll(mnewsArrayListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Medicine_News mnews : mnewsArrayListFull)
                {
                    if(mnews.mheading.toLowerCase().contains(filterPattern))
                        filteredNewsList.add(mnews);

                }
            }
            FilterResults results =new FilterResults();
            results.values=filteredNewsList;
            results.count=filteredNewsList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mnewsArrayList.clear();
            mnewsArrayList.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };


    public  class Medicine_MyViewHolder extends RecyclerView.ViewHolder{
        TextView mtvHeading;
        TextView mbriefNews;
        ImageView mtitleImage;
        ConstraintLayout mconstraintLayout;
        public Medicine_MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mtvHeading=itemView.findViewById(R.id.mtvHeading);
            mtitleImage=itemView.findViewById(R.id.mtitle_image);
            mbriefNews=itemView.findViewById(R.id.mbriefNews);
            mconstraintLayout=itemView.findViewById(R.id.mexpandedLayout);
            mtvHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Medicine_News mnews=mnewsArrayList.get(getAbsoluteAdapterPosition());
                    mnews.setVisibility(!mnews.isVisibility());
                    notifyItemChanged(getAbsoluteAdapterPosition());;
                }
            });
        }
    }
}
