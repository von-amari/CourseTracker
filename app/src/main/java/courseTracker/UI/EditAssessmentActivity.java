package courseTracker.UI;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.courseTracker.R;

import java.util.Calendar;

import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.AssessmentViewModel;

public class EditAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String TYPE = "type";
    private Boolean isEdit = false;
    private int assessmentId = 0;
    private int courseId;
    private  static final String TAG = "clicked";
    //datepicker
    int DATE_PICKER_START = 0;
    int DATE_PICKER_END = 1;

    private Spinner testSpinner;
    private EditText enterTitle;
    private EditText enterStartDate;
    private EditText enterEndDate;
    private EditText enterType;
    private String value;
    private Button saveInfoButton;
    private Button cancelInfoButton;
    private AssessmentViewModel assessmentViewModel;
    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;
    AssessmentEntity newAssessment = new AssessmentEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        testSpinner = findViewById(R.id.editAssmtSpinner);
        testSpinner.setOnItemSelectedListener(this);

        enterTitle = findViewById(R.id.editAsmtTitleText);
        enterStartDate = findViewById(R.id.editStartAsmtText);
        enterEndDate = findViewById(R.id.editEndAsmtText);
        saveInfoButton = findViewById(R.id.saveEditAsmtBtn);
        cancelInfoButton = findViewById(R.id.cancelEditAsmtBtn);

        assessmentViewModel = new ViewModelProvider.AndroidViewModelFactory(EditAssessmentActivity.this
                .getApplication())
                .create(AssessmentViewModel.class);

        //copy for info to fill in on page
        if (getIntent().hasExtra(AssessmentDetail.ASSESSMENT_ID)) {
            assessmentId = getIntent().getIntExtra(AssessmentDetail.ASSESSMENT_ID, 0);
            courseId = getIntent().getIntExtra(AssessmentDetail.COURSE_ID, 0);

            assessmentViewModel.get(assessmentId).observe(this, assessment -> {
                if (assessment != null) {
                    enterTitle.setText(assessment.getTitle());
                    enterStartDate.setText(assessment.getStartDate());
                    enterEndDate.setText(assessment.getEndDate());

                    //private method of your class
                 if(assessment.getType().equals("Performance Assessment")) {
                     testSpinner.setSelection(1);
                    }
                 else{
                     testSpinner.setSelection(0);
                 }
                }
            });
            isEdit = true;
        }

        //datepicker
        findViewById(R.id.pickEditEndAsmtBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        findViewById(R.id.pickEditStartAsmtBtn).setOnClickListener(new View.OnClickListener() {
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

            if(!TextUtils.isEmpty(enterTitle.getText())
                    && !TextUtils.isEmpty(enterStartDate.getText())
                    && !TextUtils.isEmpty(enterEndDate.getText())){

                String title = enterTitle.getText().toString();
                String startDate = enterStartDate.getText().toString();
                String endDate = enterEndDate.getText().toString();
                String testType = testSpinner.getSelectedItem().toString();

                AssessmentEntity newAsmt = new AssessmentEntity();
                newAsmt.setId(assessmentId);
                newAsmt.setTitle(title);
                newAsmt.setStartDate(startDate);
                newAsmt.setEndDate(endDate);
                newAsmt.setType(testType);
                newAsmt.setCourseId(courseId);

                AssessmentViewModel.update(newAsmt);
                finish();

                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                replyIntent.putExtra(TYPE, testType);
                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(EditAssessmentActivity.this, AssessmentDetail.class);
            intent.putExtra(AssessmentDetail.ASSESSMENT_ID, assessmentId);
                startActivity(intent);

            }else{
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    //start date picker
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cancelEditAssessment(View view) {
        Intent intent = new Intent(this, AssessmentDetail.class);
        startActivity(intent);
    }
}