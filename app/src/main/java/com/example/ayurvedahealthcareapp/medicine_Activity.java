package com.example.ayurvedahealthcareapp;



import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.SearchView;

        import java.util.ArrayList;

public class medicine_Activity extends AppCompatActivity {
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
                "Komarika","Thigh pain: Pain in the muscles of thighs because of different reasons as by lifting heavy weight, cycling and running. SWEAT IN HAND AND FEET","Nails Problems: nails have moved on, Nails Itching, Nails wound,  corner of the nails of the finger due to inflammation, Inflammation of hands and feet,  Inflammation in feet soles","Problems in Legs: Decomposition of feet finger, Feet Ringworm","MRSA (methicillin resistant staphylococcus aureus)","BURNS:Burns are among the most common household injuries, especially in children","Chest Pain: RIB The veins become tight due to check phlegm in the lungs because of that patient suffer from ribs pain. PNEUMONIA"
                ,"Anger  depression Forgetfullness"
        };
        briefNews=new String[]
                {
                        getString(R.string.med_1),
                        getString(R.string.new_2),
                        getString(R.string.new_3),
                        getString(R.string.new_4),
                        getString(R.string.new_5),
                        getString(R.string.new_6),
                        getString(R.string.new_7),
                        getString(R.string.new_8),
                        "Anger depression forgetfullness",



                };
        imageResourceId=new int[]{
                R.drawable.komarika,
                R.drawable.handlegs,
                R.drawable.nails,
                R.drawable.legs,
                R.drawable.skin1,
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