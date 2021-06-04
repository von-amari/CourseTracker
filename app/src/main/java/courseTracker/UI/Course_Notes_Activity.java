package courseTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.courseTracker.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.CourseViewModel;
import courseTracker.adapter.CourseRecyclerViewAdapter;

public class Course_Notes_Activity extends AppCompatActivity {
    private Button saveInfoButton;
    private Button cancelInfoButton;
    private Button sendEmailButton;
    private EditText emailText;
    private EditText enterTitle;
    int courseId;
    public static final String NOTES = "notes";
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String INSTRUCTOR = "instructor";
    public static final String INS_PHONE = "phone_number";
    public static final String INS_EMAIL = "email";
    public static final String STATUS = "status";
    public static final String COURSE_ID= "course_id";
    public static final String TERM_ID = "term_id";
    private CourseViewModel courseViewModel;
    CourseRecyclerViewAdapter courseRecyclerViewAdapter;

    String text;
    private Boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_notes);


        enterTitle = findViewById(R.id.editTextTextMultiLine);
        saveInfoButton = findViewById(R.id.saveCourseNoteBtn);
        cancelInfoButton = findViewById(R.id.cancelCourseNoteBtn);
        sendEmailButton = findViewById(R.id.sendNoteToEmail);
        emailText = findViewById(R.id.editTextEmailAddress);


        courseViewModel = new ViewModelProvider.AndroidViewModelFactory(Course_Notes_Activity.this
                .getApplication())
                .create(CourseViewModel.class);


            courseViewModel.get(courseId);
//
        if (getIntent().hasExtra(CourseDetailActivity.COURSE_ID)) {
            courseId = getIntent().getIntExtra(CourseDetailActivity.COURSE_ID, 0);
            courseViewModel.get(courseId).observe(this, notes-> {
                if (notes != null) {
                    enterTitle.setText(notes.getNotes());
                }
            });
            isEdit = true;
        }
    }

    public void saveCourseNote(View view) {
        Intent replyIntent = new Intent();
        courseId = getIntent().getIntExtra(CourseDetailActivity.COURSE_ID, 0);
        String title = getIntent().getStringExtra(CourseDetailActivity.TITLE_REPY);
        String startDate = getIntent().getStringExtra(CourseDetailActivity.START_DATE);
        String endDate = getIntent().getStringExtra(CourseDetailActivity.END_DATE);
        String instructor = getIntent().getStringExtra(CourseDetailActivity.INSTRUCTOR);
        String insPhone = getIntent().getStringExtra(CourseDetailActivity.INS_PHONE);
        String insEmail = getIntent().getStringExtra(CourseDetailActivity.INS_EMAIL);
        boolean startRemind = getIntent().getBooleanExtra(CourseDetailActivity.START_REMIND, false);
        boolean endRemind = getIntent().getBooleanExtra(CourseDetailActivity.END_REMIND, false);
        int termId = getIntent().getIntExtra(CourseDetailActivity.TERMID, 0);
        String status = getIntent().getStringExtra(CourseDetailActivity.STATUS);

        CourseEntity courseEntity = new CourseEntity();


        text = enterTitle.getText().toString();
        courseEntity.setNotes(text);
        courseEntity.setId(courseId);
        courseEntity.setTitle(title);
        courseEntity.setStatus(status);
        courseEntity.setTermId(termId);
        courseEntity.setInstructorName(instructor);
        courseEntity.setInstructorPhone(insPhone);
        courseEntity.setInstructorEmail(insEmail);
        courseEntity.setStartDate(startDate);
        courseEntity.setEndDate(endDate);
        courseEntity.setStartReminder(startRemind);
        courseEntity.setEndReminder(endRemind);

        courseViewModel.update(courseEntity);
            finish();

        Intent intent = new Intent(Course_Notes_Activity.this, CourseDetailActivity.class);
        intent.putExtra(COURSE_ID, courseId);
        intent.putExtra(NOTES, text);
        startActivity(intent);
    }

    public void cancelCourseNotes(View view) {

        Intent intent = new Intent(Course_Notes_Activity.this, CourseDetailActivity.class);
        startActivity(intent);
    }
//https://www.javatpoint.com/how-to-send-email-in-android-using-intent
    public void sendToEmail(View view) {
        String email =emailText.getText().toString();
        String subject = getIntent().getStringExtra(CourseDetailActivity.TITLE_REPY) + " course notes";
        String text = enterTitle.getText().toString();

        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ email});
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmail.putExtra(Intent.EXTRA_TEXT, text);

        sendEmail.setType("message/rfc822");

        startActivity(Intent.createChooser(sendEmail, "Choose an Email client :"));
    }


}