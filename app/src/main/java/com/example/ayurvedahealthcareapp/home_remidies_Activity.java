package com.example.ayurvedahealthcareapp;



import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;

public class home_remidies_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<News> newsArrayList;
    MyAdapter myAdapter;
    String[] newsHeading;
    String[] briefNews;
    int[] imageResourceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_remidies_);
        recyclerView=findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        newsArrayList =new ArrayList<News>();


        newsHeading=new String[]{
                "Chronic Fever ","Thigh pain ","Cycling and Running","Nails Problems","Problems in Legs","MRSA ","Burns","Chest Pain"
                ,"Anger:Depression:Forgetfullness"
        };
        briefNews=new String[]
                {
                        getString(R.string.new_11),
                        getString(R.string.new_12),
                        getString(R.string.new_13),
                        getString(R.string.new_14),
                        getString(R.string.new_15),
                        getString(R.string.new_16),
                        getString(R.string.new_17),
                        getString(R.string.new_18),
                        getString(R.string.new_19),



                };
        imageResourceId=new int[]{
                R.drawable.fever,
                R.drawable.handlegs,
                R.drawable.bicycle,
                R.drawable.nails,
                R.drawable.legs,
                R.drawable.skin2,
                R.drawable.skin3,
                R.drawable.chestpain,
                R.drawable.mentalhealth

        };
        getData();
    }

    private void getData() {
        for(int i=0;i<newsHeading.length;i++)
        {
            News news=new News(newsHeading[i],briefNews[i],imageResourceId[i]);
            newsArrayList.add(news);

        }
        myAdapter=new MyAdapter(this,newsArrayList);
        recyclerView.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remidy_menu_item,menu);
        MenuItem menuItem=menu.findItem(R.id.search_action);
        SearchView searchView=(SearchView)menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here !");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}