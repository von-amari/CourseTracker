package courseTracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.courseTracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.TermEntity;
import courseTracker.Entity.TermViewModel;

public class AddTermActivity extends AppCompatActivity{

    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";

    //datepicker
    int DATE_PICKER_START = 0;
    int DATE_PICKER_END = 1;

    private EditText enterTitle;
    private EditText enterStartDate;
    private EditText enterEndDate;
    private Button saveInfoButton;
    private FloatingActionButton addCourse;
    private TermViewModel termViewModel;

    //datepicker
    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        enterTitle = findViewById(R.id.textAddTermTitle);
        enterStartDate = findViewById(R.id.textAddTermStartDate);
        enterEndDate = findViewById(R.id.textAddTermEndDate);
        saveInfoButton = findViewById(R.id.saveAddTermBtn);

        //datepicker
        findViewById(R.id.pickEndDateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showEndDatePickerDialog();
            }
        });

        findViewById(R.id.pickStartDateBtn).setOnClickListener(new View.OnClickListener() {
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
//end date picker

        termViewModel = new ViewModelProvider.AndroidViewModelFactory(AddTermActivity.this
                .getApplication())
                .create(TermViewModel.class);

        saveInfoButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if(!TextUtils.isEmpty(enterTitle.getText())
                    && !TextUtils.isEmpty(enterStartDate.getText())
                    && !TextUtils.isEmpty(enterEndDate.getText())){

                String title = enterTitle.getText().toString();
                String startDate = enterStartDate.getText().toString();
                String endDate = enterEndDate.getText().toString();
                TermEntity newTerm = new TermEntity(title, startDate, endDate);
                TermViewModel.insert(newTerm);
                CourseEntity courseEntity = new CourseEntity();

                courseEntity.setTermId(newTerm.getId());

                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(AddTermActivity.this, AllTermsActivity.class);
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

    protected Dialog onCreateDialog(int id) {
        if(id== DATE_PICKER_START)
                return new DatePickerDialog(this, start_dateListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
         else if(id == DATE_PICKER_END)
            return new DatePickerDialog(this, this.end_dateListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
         else {
            return null;
        }
    }
//end date picker

    public void goToAddCourse(View view) {
        Intent intent = new Intent(AddTermActivity.this, AddCourseActivity.class);
        startActivity(intent);
    }

    public void cancelAddTerm(View view) {
        Intent intent = new Intent(AddTermActivity.this, AllTermsActivity.class);
        startActivity(intent);
    }

}
