package ljc.example.com.nestedscrollview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);
        setContentView(R.layout.activity_main);

        NestedStickerHeaderView rootView = findViewById(R.id.root);
        rootView.setMaxScrollTop(dp2px(this, 100));

        RecyclerView recyclerView = findViewById(R.id.recycle);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        final TestAdapter adapter = new TestAdapter(this, 100);
        recyclerView.setAdapter(adapter);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
