package courseTracker.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.courseTracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;
import courseTracker.Entity.TermEntity;
import courseTracker.Entity.TermViewModel;
import courseTracker.adapter.CourseRecyclerViewAdapter;

public class TermDetailActivity extends AppCompatActivity implements CourseRecyclerViewAdapter.OnCourseClickListener{
    private static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;
    public static final String TERM_ID = "term_id";
    public static final String COURSE_ID = "course_id";
    private  static final String TAG = "clicked";
    public static final String TITLE_REPLY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public  int termId = 0;
    public int courseId;
    private Boolean isEdit = false;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private RecyclerView recyclerView;
    private TextView startText;
    private TextView endText;
    private TextView titleText;
    private FloatingActionButton addCourseButton;
    private FloatingActionButton editCourseButton;
    private FloatingActionButton deleteTermButton;
    private List<CourseEntity> filteredList;
    private CourseRecyclerViewAdapter courseRecyclerViewAdapter;
    TermEntity newTerm = new TermEntity();
    int position;
    CourseEntity newCourse = new CourseEntity();

    public  TermDetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        recyclerView=findViewById(R.id.termDetailsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         titleText = findViewById(R.id.termTitleText);
        startText = findViewById(R.id.termStartDateText);
        endText = findViewById(R.id.termEndDateText);
        editCourseButton = findViewById(R.id.editTermBtn);
        deleteTermButton = findViewById(R.id.deleteTermBtn);
       addCourseButton = findViewById(R.id.addCourseBtn);

        courseViewModel = new ViewModelProvider.AndroidViewModelFactory(TermDetailActivity.this
                .getApplication())
                .create(CourseViewModel.class);

        termViewModel = new ViewModelProvider.AndroidViewModelFactory(TermDetailActivity.this
                .getApplication())
                .create(TermViewModel.class);


        if (getIntent().hasExtra(AllTermsActivity.TERM_ID)) {
            termId = getIntent().getIntExtra(AllTermsActivity.TERM_ID, 0);

            termViewModel.get(termId).observe(this, term -> {
                if (term != null) {
                    titleText.setText(term.getTitle());
                    startText.setText(term.getStartDate());
                    endText.setText(term.getEndDate());
                }
            });
            isEdit = true;
        }

        if (getIntent().hasExtra(AddCourseActivity.TERMID)) {
            termId = getIntent().getIntExtra(AddCourseActivity.TERMID, 0);

            termViewModel.get(termId).observe(this, term -> {
                if (term != null) {
                    titleText.setText(term.getTitle());
                    startText.setText(term.getStartDate());
                    endText.setText(term.getEndDate());
                }
            });
            isEdit = true;
        }

        if (getIntent().hasExtra(CourseDetailActivity.TERMID)) {
            termId = getIntent().getIntExtra(CourseDetailActivity.TERMID, 0);

            termViewModel.get(termId).observe(this, term -> {
                if (term != null) {
                    titleText.setText(term.getTitle());
                    startText.setText(term.getStartDate());
                    endText.setText(term.getEndDate());
                }
            });
            isEdit = true;
        }



    courseViewModel.getAllTermCourses(termId).observe(this, courses -> {
            courseRecyclerViewAdapter = new CourseRecyclerViewAdapter(courses, TermDetailActivity.this, this);
            recyclerView.setAdapter(courseRecyclerViewAdapter);
        System.out.println(termId);
        });

}

    @Override
    public void oncourseClick(int position) {
        CourseEntity newCourse = courseRecyclerViewAdapter.onBindViewHolder(position);

        Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
        courseId = newCourse.getId();
        intent.putExtra(COURSE_ID, newCourse.getId());
        intent.putExtra(TERM_ID, newCourse.getTermId());
        startActivity(intent);
        Log.d(TAG, "onCourseClick:" + newCourse.getTitle());
    }

    public void goToAddCourse(View view) {
        super.onResume();
        Intent intent = new Intent(TermDetailActivity.this, AddCourseActivity.class);
        intent.putExtra(TERM_ID, termId);
        intent.putExtra(TITLE_REPLY, titleText.getText().toString());
        intent.putExtra(START_DATE, startText.getText().toString());
        intent.putExtra(END_DATE, startText.getText().toString());
        startActivity(intent);

    }

    public void goToEditTerm(View view) {
            Intent intent = new Intent(this, EditTermActivity.class);
           intent.putExtra(TERM_ID, termId);
            Log.d(TAG, "goToEditTerm: " + newTerm.getTitle());
            startActivity(intent);
        }


    public void goToDeleteTerm(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Term");
        builder.setMessage("Delete " + titleText.getText().toString() + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent replyIntent = new Intent();

                if (courseRecyclerViewAdapter.getItemCount()==0) {

                    String title = titleText.getText().toString();
                    String startDate = startText.getText().toString();
                    String endDate = endText.getText().toString();

                    TermEntity newTerm = new TermEntity();
                    newTerm.setId(termId);
                    newTerm.setTitle(title);
                    newTerm.setStartDate(startDate);
                    newTerm.setEndDate(endDate);

                    TermViewModel.delete(newTerm);

                    Intent intent = new Intent(TermDetailActivity.this, AllTermsActivity.class);
                    intent.putExtra(TERM_ID, termId);
                    startActivity(intent);

                    finish();

                } else {
                    Toast.makeText(TermDetailActivity.this, "Please delete associated courses before deleting term.", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        });

        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
