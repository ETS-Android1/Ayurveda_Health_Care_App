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
                "Komarika","Kohomba","Cumin","Bitter","Ashwagandha","Boswellia","Tumeric","Licorice","Brahmi"
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
                        getString(R.string.new_9)




                };
        imageResourceId=new int[]{
                R.drawable.komarika,
                R.drawable.kohomba,
                R.drawable.cumin,
                R.drawable.bitter,
                R.drawable.ashwagandha,
                R.drawable.boswellia,
                R.drawable.tumeric,
                R.drawable.licorice,
                R.drawable.brahmi

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