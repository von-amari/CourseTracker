package courseTracker.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.courseTracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;
import courseTracker.Entity.TermEntity;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //
    private NotificationManagerCompat notificationManager;

    private final static String default_notification_channel_id = "default" ;

    public static int numIntents = 0;

    private Spinner statusSpinner;
    private Spinner statusSpinner2;
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String INSTRUCTOR = "instructor";
    public static final String INS_PHONE = "phone_number";
    public static final String INS_EMAIL = "email";
    public static final String STATUS = "status";
    public static final String TERMID = "term_id";
    private int termId;
    //datepicker
    int DATE_PICKER_START = 0;
    int DATE_PICKER_END = 1;
    //datepicker
    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;
    private SimpleDateFormat dateFormat;
    String dateF;

    Calendar myCalendar;

    private EditText enterTitle;
    private EditText enterStartDate;
    private String start;
    private String end;
    private EditText enterEndDate;
    private EditText enterInstructor;
    private EditText enterInsNum;
    private EditText enterInsEmail;
    private Button saveInfoButton;
    private Button cancelInfoButton;
    private CourseViewModel courseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        notificationManager = NotificationManagerCompat.from(this);

        statusSpinner = findViewById(R.id.addCourseStatusSpin);
        statusSpinner2 = findViewById(R.id.addCourseStatusSpin2);

    myCalendar = Calendar.getInstance();
    dateFormat = new SimpleDateFormat("M/dd/yyyy");
    dateF = dateFormat.format(myCalendar.getTime());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.courseStatus, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        enterTitle = findViewById(R.id.textAddCourseTitle);
        enterStartDate = findViewById(R.id.editCourseStartText);
        enterEndDate = findViewById(R.id.editCourseEndText);
        enterInstructor = findViewById(R.id.textAddCourseInsName);
        enterInsNum = findViewById(R.id.textAddCourseInsNum);
        enterInsEmail = findViewById(R.id.textAddCourseInsEmail);
        saveInfoButton = findViewById(R.id.saveAddCourseBtn);
        cancelInfoButton = findViewById(R.id.cancelAddCourseBtn);

        //datepicker
        findViewById(R.id.pickEndCourseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        findViewById(R.id.pickStartCourseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        end_dateListener = (view, year, month, dayOfMonth) -> {
            String datef = month+1 +"/"+dayOfMonth+"/"+year;
            enterEndDate.setText(datef);
            end = datef;
        };

        start_dateListener = (view, year, month, dayOfMonth) -> {
            String datef = month+1 +"/"+dayOfMonth+"/"+year;
            enterStartDate.setText(datef);
            start= datef;
        };

//end date picker
        courseViewModel = new ViewModelProvider.AndroidViewModelFactory(AddCourseActivity.this
                .getApplication())
                .create(CourseViewModel.class);

        saveInfoButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            CourseEntity newCourse = new CourseEntity();
            TermEntity newTerm = new TermEntity();
            if(!TextUtils.isEmpty(enterTitle.getText())
                    && !TextUtils.isEmpty(enterStartDate.getText())
                    && !TextUtils.isEmpty(enterEndDate.getText())
                    && !TextUtils.isEmpty(enterInstructor.getText())
                    && !TextUtils.isEmpty(enterInsNum.getText())
                    && !TextUtils.isEmpty(enterInsEmail.getText())
                    &&  newCourse.getTermId() == newTerm.getId()) {

                String title = enterTitle.getText().toString();
                String startDate = enterStartDate.getText().toString();
                String endDate = enterEndDate.getText().toString();
                String instructor = enterInstructor.getText().toString();
                String insPhone = enterInsNum.getText().toString();
                String insEmail = enterInsEmail.getText().toString();
                String status = statusSpinner2.getSelectedItem().toString();

                int termID = getIntent().getIntExtra(TermDetailActivity.TERM_ID, 0);
                newCourse = new CourseEntity(title, startDate, endDate, status, instructor, insPhone, insEmail,  termID, "", false, false  );
                CourseViewModel.insert(newCourse);
                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                replyIntent.putExtra(INSTRUCTOR, instructor);
                replyIntent.putExtra(INS_PHONE, insPhone);
                replyIntent.putExtra(INS_EMAIL, insEmail);
                replyIntent.putExtra(STATUS, status);

                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(AddCourseActivity.this, TermDetailActivity.class);
                intent.putExtra(TERMID, termID);
                startActivity(intent);


            }else{
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }//end onCreate

    //start date picker  https://www.youtube.com/watch?v=AdTzD96AhE0
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

    protected Dialog onCreateDialog(int id) {
        if(id== DATE_PICKER_START)
            return new DatePickerDialog(this, this.start_dateListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        else if(id == DATE_PICKER_END)
            return new DatePickerDialog(this, this.end_dateListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        else {
            return null;
        }
    }

    public void cancelAddCourse(View view) {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
        parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}