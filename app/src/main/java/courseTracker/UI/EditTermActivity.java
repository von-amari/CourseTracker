package courseTracker.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.courseTracker.R;

import java.util.Calendar;

import courseTracker.Entity.TermEntity;
import courseTracker.Entity.TermViewModel;

public class EditTermActivity extends AppCompatActivity {
    public static final String TITLE_REPY = "title_reply";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    private Boolean isEdit = false;
    private int termId = 0;
    //datepicker
    int DATE_PICKER_START = 0;
    int DATE_PICKER_END = 1;

    private EditText enterTitle;
    private EditText enterStartDate;
    private EditText enterEndDate;

    private Button saveInfoButton;
    private Button cancelInfoButton;
    private TermViewModel termViewModel;
    //datepicker
    DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);

        enterTitle = findViewById(R.id.editTermTitleText);
        enterStartDate = findViewById(R.id.editStartTermText);
        enterEndDate = findViewById(R.id.editEndTermText);
        saveInfoButton = findViewById(R.id.saveEditTermBtn);
        cancelInfoButton = findViewById(R.id.cancelEditTermBtn);


        termViewModel = new ViewModelProvider.AndroidViewModelFactory(EditTermActivity.this
                .getApplication())
                .create(TermViewModel.class);

    //copy for info to fill in on page
        if (getIntent().hasExtra(TermDetailActivity.TERM_ID)) {
            termId = getIntent().getIntExtra(TermDetailActivity.TERM_ID, 0);

            termViewModel.get(termId).observe(this, term -> {
                if (term != null) {
                    enterTitle.setText(term.getTitle());
                    enterStartDate.setText(term.getStartDate());
                    enterEndDate.setText(term.getEndDate());
                }
            });
            isEdit = true;
        }


        //datepicker
        findViewById(R.id.pickEditEndTermBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        findViewById(R.id.pickEditStartTermBtn).setOnClickListener(new View.OnClickListener() {
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

                TermEntity newTerm = new TermEntity();
                newTerm.setId(termId);
                newTerm.setTitle(title);
                newTerm.setStartDate(startDate);
                newTerm.setEndDate(endDate);

                TermViewModel.update(newTerm);
                finish();

                replyIntent.putExtra(TITLE_REPY, title);
                replyIntent.putExtra(START_DATE, startDate);
                replyIntent.putExtra(END_DATE, endDate);
                setResult(RESULT_OK, replyIntent);

                Intent intent = new Intent(EditTermActivity.this, TermDetailActivity.class);
                intent.putExtra(TermDetailActivity.TERM_ID, termId);
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
    public void cancelEditTerm(View view) {
        super.onBackPressed();
    }
}