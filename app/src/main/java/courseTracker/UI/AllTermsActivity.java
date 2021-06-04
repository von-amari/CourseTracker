package courseTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.courseTracker.R;

import java.util.List;
import java.util.Objects;

import courseTracker.Entity.TermEntity;
import courseTracker.Entity.TermViewModel;
import courseTracker.Utils.CourseRepository;
import courseTracker.adapter.TermRecyclerViewAdapter;

public class AllTermsActivity extends AppCompatActivity  implements TermRecyclerViewAdapter.OnTermClickListener{

    private static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;
    public static final String TERM_ID = "term_id";
    private TermViewModel termViewModel;
    public static int termId;
    private TextView textView;
    private RecyclerView recyclerView;
    private TermRecyclerViewAdapter termRecyclerViewAdapter;
    private LiveData<List<AllTermsActivity>> termList;
    private  static final String TAG = "clicked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_terms);
        //textView = findViewById(R.id.textForTerms);
        recyclerView = findViewById(R.id.termRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        termViewModel = new ViewModelProvider.AndroidViewModelFactory(AllTermsActivity.this
                .getApplication())
                .create(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, terms -> {
            //set up adapter
            termRecyclerViewAdapter = new TermRecyclerViewAdapter(terms, AllTermsActivity.this, this);

            recyclerView.setAdapter(termRecyclerViewAdapter);
        });
    }

    public void goToAddTerm(View view) {
        Intent intent = new Intent(AllTermsActivity.this, AddTermActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTermClick(int position) {
        TermEntity newTerm = Objects.requireNonNull(termViewModel.allTerms.getValue()).get(position);
        Intent intent = new Intent(AllTermsActivity.this, TermDetailActivity.class);
    intent.putExtra(TERM_ID, newTerm.getId());
        startActivity(intent);

        Log.d(TAG, "onTermClick: " + newTerm.getTitle());
    }
}
