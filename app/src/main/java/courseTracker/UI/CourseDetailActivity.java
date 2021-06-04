package courseTracker.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.courseTracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.AssessmentViewModel;
import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;
import courseTracker.Entity.TermViewModel;
import courseTracker.adapter.AssessmentRecyclerViewAdapter;

public class CourseDetailActivity extends AppCompatActivity implements AssessmentRecyclerViewAdapter.OnAssessmentClickListener{
    private NotificationManagerCompat notificationManager;

    private static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;

    public static final String COURSE_ID = "course_id";
    public static final String ASSESSMENT_ID = "assessment_id";
    public  int courseId;
     public  int termId;
    public static int spinnerPick;
    private  static final String TAG = "clicked";
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String INSTRUCTOR = "instructor";
    public static final String INS_PHONE = "phone_number";
    public static final String INS_EMAIL = "email";
    public static final String STATUS = "status";
    public static final String TERMID = "term_id";
    public static final String SETTING_CHECK_BOX = "checkbox_setting";
    public static final String NOTES = "notes";
    public static final String COURSE = "course";
    public static final String TITLE = "title";
    public static final String START = "start";
    public static final String END = "end";
    public static final String START_REMIND = "start reminder";
    public static final String END_REMIND = "end reminder";

    private Boolean isEdit = false;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private AssessmentViewModel assessmentViewModel;

    private TextView startText;
    private TextView endText;
    private TextView insName;
    private TextView insPhone;
    private TextView insEmail;
    private TextView courseStatus;
    private TextView titleText;
    private String notes;
    private String title;
    private String startDate;
    private String endDate;
    private String stat;
    private boolean isCheckedStart;
    private boolean isCheckedEnd;

 private String instructorName;
   private String instructorPhone;
   private String instructorEmail;

    private FloatingActionButton addCourseButton;
    private FloatingActionButton editCourseButton;
    private FloatingActionButton deleteCourseButton;
    AssessmentRecyclerViewAdapter assessmentRecyclerViewAdapter;
    private CheckBox startCheck;
    private CheckBox endCheck;

    private SimpleDateFormat dateFormat;
    String dateF;
    Calendar myCalendar;
    long startMill;
    long endMill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        notificationManager = NotificationManagerCompat.from(this);
        RecyclerView recyclerView;
            recyclerView = findViewById(R.id.assmtRecyclerView);
                recyclerView.setHasFixedSize(true);
               recyclerView.setLayoutManager(new LinearLayoutManager(this));


        titleText = findViewById(R.id.courseTitleText);
        startText = findViewById(R.id.courseStartDateText);
        endText = findViewById(R.id.courseEndDateText);
        insName = findViewById(R.id.courseInsNameText);
        insPhone = findViewById(R.id.courseInsNumText);
        insEmail = findViewById(R.id.courseInsEmailText);
        courseStatus = findViewById(R.id.courseStatusText);
        editCourseButton = findViewById(R.id.editCourseDetailBtn);
        deleteCourseButton = findViewById(R.id.deleteCourseBtn);
        startCheck =(CheckBox) findViewById(R.id.setStartCheck);
        endCheck = (CheckBox)findViewById (R.id.setEndCheck);

        //today
        myCalendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("M/dd/yyyy");
        dateF = dateFormat.format(myCalendar.getTime());

        courseViewModel = new ViewModelProvider.AndroidViewModelFactory(CourseDetailActivity.this
                .getApplication())
                .create(CourseViewModel.class);

        termViewModel = new ViewModelProvider.AndroidViewModelFactory(CourseDetailActivity.this
                .getApplication())
                .create(TermViewModel.class);

        assessmentViewModel = new ViewModelProvider.AndroidViewModelFactory(CourseDetailActivity.this
                .getApplication())
                .create(AssessmentViewModel.class);


        if (getIntent().hasExtra(TermDetailActivity.COURSE_ID)) {
            courseId = getIntent().getIntExtra(TermDetailActivity.COURSE_ID, 0);
            termId = getIntent().getIntExtra(TermDetailActivity.TERM_ID, 0);
            notes = getIntent().getStringExtra(Course_Notes_Activity.NOTES);

            courseViewModel.get(courseId).observe(this, course -> {
                if ((course != null)) {
                    Intent replyIntent = new Intent();
                    titleText.setText(course.getTitle());
                    startText.setText(course.getStartDate());
                    endText.setText(course.getEndDate());
                    insName.setText(course.getInstructorName());
                    insPhone.setText(course.getInstructorPhone());
                    insEmail.setText(course.getInstructorEmail());
                    courseStatus.setText(course.getStatus());
                    isCheckedStart=course.isStartReminder();
                    isCheckedEnd = course.isEndReminder();
                    startCheck.setChecked(course.isStartReminder());
                    endCheck.setChecked(course.isEndReminder());

                    title = titleText.getText().toString();
                    startDate = startText.getText().toString();
                    endDate = endText.getText().toString();
                    stat = courseStatus.getText().toString();
                    instructorName = insName.getText().toString();
                    instructorPhone = insPhone.getText().toString();
                    instructorEmail = insEmail.getText().toString();
                    notes = course.getNotes();

                    replyIntent.putExtra(TITLE_REPY, course.getTitle());
                    replyIntent.putExtra(START_DATE, course.getStartDate());
                    replyIntent.putExtra(END_DATE, course.getEndDate());
                    replyIntent.putExtra(STATUS, course.getStatus());
                    replyIntent.putExtra(COURSE_ID, courseId);

                    replyIntent.putExtra(INSTRUCTOR, course.getInstructorName());
                    replyIntent.putExtra(INS_PHONE, course.getInstructorPhone());
                    replyIntent.putExtra(INS_EMAIL, course.getInstructorEmail());
                    replyIntent.putExtra(TERMID, termId);

                    replyIntent.putExtra(Course_Notes_Activity.NOTES, notes);
                    setResult(RESULT_OK, replyIntent);
                }
            });
            isEdit = true;
        }

        assessmentViewModel.getAllAssessments(courseId).observe(this, assessments -> {
            //set up adapter
            assessmentRecyclerViewAdapter = new AssessmentRecyclerViewAdapter(assessments, CourseDetailActivity.this, this);

            recyclerView.setAdapter(assessmentRecyclerViewAdapter);
        });
        }//end onCreate

    @Override
    public void onPause() {
        super.onPause();
    }

    public void oncourseClick(int position) {
        CourseEntity newCourse = courseViewModel.allCourses.getValue().get(position);
            //staticCourseEntity = newCourse;
        Intent intent = new Intent(CourseDetailActivity.this, EditCourseActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        startActivity(intent);
        Log.d(TAG, "onCourseClick:" + newCourse.getTitle());
    }

    public void goToCourseNotes(View view) {

            Intent intent = new Intent(CourseDetailActivity.this, Course_Notes_Activity.class);
            intent.putExtra(TITLE_REPY, title);
            intent.putExtra(START_DATE, startDate);
            intent.putExtra(END_DATE, endDate);
            intent.putExtra(STATUS, stat);
            intent.putExtra(COURSE_ID, courseId);

            intent.putExtra(INSTRUCTOR, instructorName);
            intent.putExtra(INS_PHONE, instructorPhone);
            intent.putExtra(INS_EMAIL, instructorEmail);
            intent.putExtra(START_REMIND, isCheckedStart);
            intent.putExtra(END_REMIND, isCheckedEnd);
            intent.putExtra(TERMID, termId);
            intent.putExtra(Course_Notes_Activity.NOTES, notes);

            startActivity(intent);
    }

    public void goToAddAssessment(View view) {
        Intent intent = new Intent(CourseDetailActivity.this, AddAssessmentsActivity.class);
        intent.putExtra(COURSE_ID, this.courseId);
        startActivity(intent);
    }

    @Override
    public void onAssessmentClick(int position) {

        AssessmentEntity newAssessment = assessmentRecyclerViewAdapter.onBindViewHolder(position);
        Intent intent = new Intent (CourseDetailActivity.this, AssessmentDetail.class);
            intent.putExtra(ASSESSMENT_ID, newAssessment.getId());
            intent.putExtra(COURSE_ID, this.courseId);
        Log.d(TAG, "onAssessmentClick: " + newAssessment.getTitle() + newAssessment.getId());
        startActivity(intent);
    }

    public void goToEditCourse(View view) {

        Intent intent = new Intent(this, EditCourseActivity.class);
        intent.putExtra(COURSE_ID,courseId);
        Log.d(TAG, "goToEditCourse: " );
        startActivity(intent);
    }

    public void goToDeleteCourse(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Course");
        builder.setMessage("Delete " + title + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            Intent replyIntent = new Intent();
            if ( assessmentRecyclerViewAdapter.getItemCount() == 0) {

                String title = titleText.getText().toString();
                String startDate = startText.getText().toString();
                String endDate = endText.getText().toString();
                String iName = insName.getText().toString();
                String iPhone = insPhone.getText().toString();
                String iEmail = insEmail.getText().toString();
                String progress = courseStatus.getText().toString();

                CourseEntity newCourse = new CourseEntity();
                newCourse.setId(courseId);
                newCourse.setTitle(title);
                newCourse.setStartDate(startDate);
                newCourse.setEndDate(endDate);
                newCourse.setStatus(progress);

                CourseViewModel.delete(newCourse);
                finish();

                Intent intent = new Intent(CourseDetailActivity.this, TermDetailActivity.class);
                intent.putExtra(AssessmentDetail.COURSE_ID, courseId);
                intent.putExtra(TERMID, termId);
                startActivity(intent);

            } else {
                Toast.makeText(CourseDetailActivity.this, "Please delete associated assessments before deleting course.", Toast.LENGTH_SHORT)
                        .show();
            }
            }
        });

        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setEndReminder(View view) {
        CourseEntity course = new CourseEntity();
        startCheck.setChecked(isCheckedStart);
        if (endCheck.isChecked()) {

            course.setEndReminder(true);

            try {
                Calendar myCalendar = Calendar.getInstance();
                Long dateLong;
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date date;
                date = formatter.parse(endText.getText().toString());
                endMill = date.getTime();
                Intent intent = new Intent(CourseDetailActivity.this, MyReceiver.class);
                intent.putExtra("key", "Course end reminder for " + title + " on " + endDate);
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, 2, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endMill, sender);
            } catch (ParseException exception) {
                exception.printStackTrace();
            }

        }else {
            course.setEndReminder(false);
        }
        course.setId(courseId);
        course.setTitle(title);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setStatus(stat);
        course.setInstructorName(instructorName);
        course.setInstructorEmail(instructorEmail);
        course.setInstructorPhone(instructorPhone);
        course.setTermId(termId);
        course.setStartReminder(isCheckedStart);

        course.setNotes(notes);
        courseViewModel.update(course);
    }



    public void setStartReminder(View view) {
        CourseEntity course = new CourseEntity();
    endCheck.setChecked(isCheckedStart);
        if ((startCheck.isChecked())) {

            course.setStartReminder(true);

            try {
                Calendar myCalendar = Calendar.getInstance();
                Long dateLong;
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date date;
                date = formatter.parse(startText.getText().toString());
                startMill = date.getTime();
                Intent intent = new Intent(CourseDetailActivity.this, MyReceiver.class);
                intent.putExtra("key", "Course start reminder for " + title + " on " + startDate);
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, 1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startMill, sender);
            } catch (ParseException exception) {
                exception.printStackTrace();
            }

        }else {
            course.setStartReminder(false);
        }
        course.setId(courseId);
        course.setTitle(title);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setStatus(stat);
        course.setInstructorName(instructorName);
        course.setInstructorEmail(instructorEmail);
        course.setInstructorPhone(instructorPhone);
        course.setTermId(termId);
        course.setEndReminder(isCheckedEnd);
        course.setNotes(notes);
        courseViewModel.update(course);
    }
    }
