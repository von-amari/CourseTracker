package courseTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.courseTracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.AssessmentViewModel;
import courseTracker.Entity.CourseEntity;

public class AssessmentDetail extends AppCompatActivity {

    public static final String COURSE_ID = "course_id";
    public static final int DELETE_DIALOG_ID = 1;
    int courseID;
    private AssessmentViewModel assessmentViewModel;
    private int assessmentId = 0;
    private RecyclerView recyclerView;
    private TextView titleText;
    private TextView startText;
    private TextView endText;
    private TextView testType;
    private String test;
    private Boolean isEdit = false;
    public static final String ASSESSMENT_ID = "assessment_id";
    public static final String TYPE = "assessment_type";
    private FloatingActionButton editAsmtButton;
    private FloatingActionButton deleteAsmntButton;
    private static final String TAG = "clicked";
    AssessmentEntity newAssessment = new AssessmentEntity();

    private String aTitle;
    private String aStart;
    private String aEnd;
    private String aTest;
    private boolean isCheckedStart;
    private boolean isCheckedEnd;
    private CheckBox startCheck;
    private CheckBox endCheck;
    long endMill;
    long startMill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        titleText = findViewById(R.id.asmtTitleText);
        startText = findViewById(R.id.asmtStartText);
        endText = findViewById(R.id.asmtEndText);
        testType = findViewById(R.id.asmtTypeText);
        editAsmtButton = findViewById(R.id.editAsmntBtn);
        deleteAsmntButton = findViewById(R.id.deleteAsmtBtn);
        startCheck = (CheckBox) findViewById(R.id.checkStartAssmt);
        endCheck =(CheckBox) findViewById(R.id.checkEndAssmnt);

        assessmentViewModel = new ViewModelProvider.AndroidViewModelFactory(AssessmentDetail.this
                .getApplication())
                .create(AssessmentViewModel.class);

        if (getIntent().hasExtra(CourseDetailActivity.ASSESSMENT_ID)) {
            assessmentId = getIntent().getIntExtra(CourseDetailActivity.ASSESSMENT_ID, 0);
            courseID = getIntent().getIntExtra(CourseDetailActivity.COURSE_ID, 0);

            assessmentViewModel.get(assessmentId).observe(this, assessment -> {
                if (assessment != null) {
                    titleText.setText(assessment.getTitle());
                    startText.setText(assessment.getStartDate());
                    endText.setText(assessment.getEndDate());
                    testType.setText(assessment.getType());
                    courseID = assessment.getCourseId();
                test = assessment.getType();

                aTitle = titleText.getText().toString();
                aStart = startText.getText().toString();
                aEnd = endText.getText().toString();
                aTest = testType.getText().toString();
                isCheckedEnd = assessment.isEndReminder();
                isCheckedStart = assessment.isStartReminder();
                startCheck.setChecked(assessment.isStartReminder());
                endCheck.setChecked(assessment.isEndReminder());

                }
            });
            isEdit = true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void goToEditAssessment(View view) {

        Intent intent = new Intent(this, EditAssessmentActivity.class);
        intent.putExtra(ASSESSMENT_ID, assessmentId);
        intent.putExtra(COURSE_ID, courseID);
        intent.putExtra(TYPE, test);

        Log.d(TAG, "goToEditAsmt: " + newAssessment.getTitle());
        startActivity(intent);
    }

    //https://suragch.medium.com/making-an-alertdialog-in-android-2045381e2edb  for dialog
    public void deleteAssessment(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Assessment");
        builder.setMessage("Delete " + aTitle + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                deleteAsmntButton.setOnClickListener(view -> {

                    Intent replyIntent = new Intent();

                    if (!TextUtils.isEmpty(titleText.getText())
                            && !TextUtils.isEmpty(startText.getText())
                            && !TextUtils.isEmpty(endText.getText())) {

                        String title = titleText.getText().toString();
                        String startDate = startText.getText().toString();
                        String endDate = endText.getText().toString();
                        String test = testType.getText().toString();

                        AssessmentEntity newAsmt = new AssessmentEntity();
                        newAsmt.setId(assessmentId);
                        newAsmt.setTitle(title);
                        newAsmt.setStartDate(startDate);
                        newAsmt.setEndDate(endDate);
                        newAsmt.setType(test);

                        AssessmentViewModel.delete(newAsmt);

                        Intent intent = new Intent(AssessmentDetail.this, CourseDetailActivity.class);
                        intent.putExtra(AssessmentDetail.ASSESSMENT_ID, assessmentId);
                        startActivity(intent);

                        finish();

                  } else {
                       Toast.makeText(AssessmentDetail.this, R.string.empty, Toast.LENGTH_SHORT)
                                .show();
                   }
            }

        });

        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void setStart(View view) {
        AssessmentEntity assessment = new AssessmentEntity();
        endCheck.setChecked(isCheckedEnd);
        if (startCheck.isChecked()) {
            assessment.setStartReminder(true);

            try {
                Calendar myCalendar = Calendar.getInstance();
                Long dateLong;
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date date;
                date = formatter.parse(startText.getText().toString());
                endMill = date.getTime();
                Intent intent = new Intent(AssessmentDetail.this, MyReceiver.class);
                intent.putExtra("key", "Assessment start reminder for " + aTitle + " on " + aStart);
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, 3, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endMill, sender);

            } catch (ParseException exception) {
                exception.printStackTrace();
            }

        }else {
            assessment.setStartReminder(false);
        }
        assessment.setId(assessmentId);
        assessment.setTitle(aTitle);
        assessment.setStartDate(aStart);
        assessment.setEndDate(aEnd);
        assessment.setType(aTest);

        assessment.setCourseId(courseID);
        assessment.setEndReminder(isCheckedEnd);
        assessmentViewModel.update(assessment);
    }

    public void setEnd(View view) {

        AssessmentEntity assessment = new AssessmentEntity();
        startCheck.setChecked(isCheckedStart);
        //title = course.getTitle();
        if (endCheck.isChecked()) {
           // endCheck.setChecked(isCheckedEnd);
            assessment.setEndReminder(true);

            try {
                Calendar myCalendar = Calendar.getInstance();
                Long dateLong;
                SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                Date date;
                date = formatter.parse(endText.getText().toString());
                endMill = date.getTime();
                Intent intent = new Intent(AssessmentDetail.this, MyReceiver.class);
                intent.putExtra("key", "Assessment end reminder for " + aTitle + " on " + aEnd);
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, 4, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endMill, sender);

            } catch (ParseException exception) {
                exception.printStackTrace();
            }

        }else {
            assessment.setEndReminder(false);
        }
        assessment.setId(assessmentId);
        assessment.setTitle(aTitle);
        assessment.setStartDate(aStart);
        assessment.setEndDate(aEnd);
        assessment.setType(aTest);

        assessment.setCourseId(courseID);
        assessment.setStartReminder(isCheckedStart);
        assessmentViewModel.update(assessment);
    }
}