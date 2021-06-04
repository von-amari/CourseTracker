package courseTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.courseTracker.R;
import java.util.Calendar;
import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.AssessmentViewModel;
import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;
import courseTracker.Entity.TermEntity;
import courseTracker.Utils.AssessmentRepository;

public class AddAssessmentsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private Spinner testSpinner;
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String TEST_TYPE = "test_type";
    public static final String ASSESSMENT_ID = "assessmentId";
    public static final String ASSESSMENT_COURSE_ID = "assessmentCourseId";
    private EditText enterTitle;
    private EditText enterStartDate;
    private EditText enterEndDate;
    private Button saveInfoButton;
    private Button cancelInfoButton;
    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    public static AssessmentRepository repository;
    public int assessmentId;
    public int courseID;
    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessments);
        testSpinner = findViewById(R.id.addAssmtSpinner);

        testSpinner.setOnItemSelectedListener(this);

        enterTitle = findViewById(R.id.textAddAssmtTitle);
        enterStartDate = findViewById(R.id.textAddAssmtStartDate);
        enterEndDate = findViewById(R.id.textAddAssmntEndDate);
        saveInfoButton = findViewById(R.id.saveAddAssmtBtn);
        cancelInfoButton = findViewById(R.id.cancelAssmntCourseBtn);

        assessmentViewModel = new ViewModelProvider.AndroidViewModelFactory(AddAssessmentsActivity.this
                .getApplication())
                .create(AssessmentViewModel.class);


        findViewById(R.id.pickAddEndAsmtBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        findViewById(R.id.pickAddStartAsmtBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        end_dateListener = (view, year, month, dayOfMonth) -> {
            String date = month+1 +"/"+dayOfMonth+"/"+year;
            enterEndDate.setText(date);
        };
        start_dateListener = (view, year, month, dayOfMonth) -> {
            String date = month+1 +"/"+dayOfMonth+"/"+year;
            enterStartDate.setText(date);
        };


        saveInfoButton.setOnClickListener(view -> {

            Intent replyIntent = new Intent();
            CourseEntity newCourse = new CourseEntity();
            AssessmentEntity newAssessment = new AssessmentEntity();
            TermEntity newTerm = new TermEntity();
            if(!TextUtils.isEmpty(enterTitle.getText())
                    && !TextUtils.isEmpty(enterStartDate.getText())
                    && !TextUtils.isEmpty(enterEndDate.getText())) {

                String title = enterTitle.getText().toString();
                String startDate = enterStartDate.getText().toString();
                String endDate = enterEndDate.getText().toString();
                String test = testSpinner.getSelectedItem().toString();

                courseID = getIntent().getIntExtra(CourseDetailActivity.COURSE_ID, 0);
                assessmentId = newAssessment.getId();

                newAssessment = new AssessmentEntity(title, startDate, endDate, test, courseID, false, false);
                AssessmentViewModel.insert(newAssessment);

                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                replyIntent.putExtra(TEST_TYPE, test);
                replyIntent.putExtra(ASSESSMENT_ID, newAssessment.getId());
                replyIntent.putExtra(ASSESSMENT_COURSE_ID, newAssessment.getCourseId());
                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(AddAssessmentsActivity.this, CourseDetailActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void showStartDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, start_dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void showEndDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, end_dateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void goToAssessmentDetails(View view) {
        Intent intent = new Intent(AddAssessmentsActivity.this, AssessmentDetail.class);
        startActivity(intent);
    }

    public void cancelAddAssessment(View view) {
        //super.onBackPressed();
        Intent intent = new Intent(AddAssessmentsActivity.this, CourseDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected (AdapterView< ? > parent, View view, int position, long id){
        parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
