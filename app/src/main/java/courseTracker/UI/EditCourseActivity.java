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

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;

public class EditCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner statusSpinner;
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String INSTRUCTOR = "instructor";
    public static final String INS_PHONE = "phone_number";
    public static final String INS_EMAIL = "email";
    public static final String STATUS = "status";
    private int courseId = 0;
    private Boolean isEdit = false;

    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;

    private EditText enterTitle;
    private EditText enterStartDate;
    private EditText enterEndDate;
    private EditText enterInstructor;
    private EditText enterInsNum;
    private EditText enterInsEmail;
    private Button saveInfoButton;
    private Button cancelInfoButton;
    private boolean startRemind;
    private boolean endRemind;
    private int termId;

    private CourseViewModel courseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        statusSpinner = findViewById(R.id.editCourseStatusSpin3);
        statusSpinner.setOnItemSelectedListener(this);

        enterTitle = findViewById(R.id.editCourseTitleText);
        enterStartDate = findViewById(R.id.textEditCourseStart);
        enterEndDate = findViewById(R.id.textEditCourseEnd);
        enterInstructor = findViewById(R.id.editCourseInsNameText);
        enterInsNum = findViewById(R.id.editCourseInsPhoneText);
        enterInsEmail = findViewById(R.id.editCourseInsEmailText);
        saveInfoButton = findViewById(R.id.saveEditCourseBtn);
        cancelInfoButton = findViewById(R.id.cancelEditCourseBtn);

        courseViewModel = new ViewModelProvider.AndroidViewModelFactory(EditCourseActivity.this
                .getApplication())
                .create(CourseViewModel.class);


        if (getIntent().hasExtra(CourseDetailActivity.COURSE_ID)) {
            courseId = getIntent().getIntExtra(CourseDetailActivity.COURSE_ID, 0);

            courseViewModel.get(courseId).observe(this, course-> {
                if (course != null) {
                    enterTitle.setText(course.getTitle());
                    enterStartDate.setText(course.getStartDate());
                    enterEndDate.setText(course.getEndDate());
                    enterInstructor.setText(course.getInstructorName());
                    enterInsNum.setText(course.getInstructorPhone());
                    enterInsEmail.setText(course.getInstructorEmail());
                   startRemind = course.isStartReminder();
                   endRemind = course.isEndReminder();
                   termId = course.getTermId();
                }
            });
            isEdit = true;
        }

        //datepicker
        findViewById(R.id.editCourseEndBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        findViewById(R.id.editCourseStartBtn).setOnClickListener(new View.OnClickListener() {
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
                    && !TextUtils.isEmpty(enterEndDate.getText())
                    && !TextUtils.isEmpty(enterInstructor.getText())
                    && !TextUtils.isEmpty(enterInsNum.getText())
                    && !TextUtils.isEmpty(enterInsEmail.getText())){

                String title = enterTitle.getText().toString();
                String startDate = enterStartDate.getText().toString();
                String endDate = enterEndDate.getText().toString();
                String instructorName = enterInstructor.getText().toString();
                String instructorPhone = enterInsNum.getText().toString();
                String instructorEmail = enterInsEmail.getText().toString();
                String status = statusSpinner.getSelectedItem().toString();

                CourseEntity courseEntity = new CourseEntity();

                courseEntity.setId(courseId);
                courseEntity.setTitle(title);
                courseEntity.setStartDate(startDate);
                courseEntity.setEndDate(endDate);
                courseEntity.setStatus(status);
                courseEntity.setInstructorEmail(instructorEmail);
                courseEntity.setInstructorName(instructorName);
                courseEntity.setInstructorPhone(instructorPhone);
                courseEntity.setStartReminder(startRemind);
                courseEntity.setEndReminder(endRemind);
                courseEntity.setTermId(termId);

                CourseViewModel.update(courseEntity);
                finish();

                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                replyIntent.putExtra(INSTRUCTOR, instructorName);
                replyIntent.putExtra(INS_PHONE, instructorPhone);
                replyIntent.putExtra(INS_EMAIL, instructorEmail);
                replyIntent.putExtra(STATUS, status);
                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(EditCourseActivity.this, CourseDetailActivity.class);
                intent.putExtra(CourseDetailActivity.COURSE_ID, courseId);
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
}