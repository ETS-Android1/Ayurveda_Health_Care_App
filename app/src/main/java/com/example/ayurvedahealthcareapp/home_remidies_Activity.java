package com.example.ayurvedahealthcareapp;



import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;

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
        myAdapter=new MyAdapter(this,newsArrayList);
        recyclerView.setAdapter(myAdapter);
        newsHeading=new String[]{
                "geigdowiheohoeihfepojfeop","ihwiuchewiuchwihcihw","iudgwiuwuhoiwhcoiwhon","biuebvebvu","coniovcnoe","oneocinoimvemnvnnonnnmcowe","cenoinbbiecbncweic"
                ,"dioniviio"
        };
        briefNews=new String[]
                {
                        getString(R.string.new_1),
                        getString(R.string.new_2),
                        getString(R.string.new_3),
                        getString(R.string.new_4),
                        getString(R.string.new_5),
                        getString(R.string.new_6),
                        getString(R.string.new_7),
                        getString(R.string.new_8),



                };
        imageResourceId=new int[]{
                R.drawable.apple,
                R.drawable.avacado,
                R.drawable.cherry,
                R.drawable.mango,
                R.drawable.passion,
                R.drawable.pearss,
                R.drawable.orange,
                R.drawable.banana

        };
        getData();
    }

    private void getData() {
        for(int i=0;i<newsHeading.length;i++)
        {
            News news=new News(newsHeading[i],briefNews[i],imageResourceId[i]);
            newsArrayList.add(news);

        }
        myAdapter.notifyDataSetChanged();

    }
}