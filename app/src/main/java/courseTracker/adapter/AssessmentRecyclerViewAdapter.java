package courseTracker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.courseTracker.R;

import java.util.List;
import java.util.Objects;

import courseTracker.Entity.AssessmentEntity;
import courseTracker.Entity.CourseEntity;
import courseTracker.Entity.TermEntity;
import courseTracker.UI.CourseDetailActivity;
import courseTracker.UI.TermDetailActivity;
import courseTracker.Utils.CourseRepository;
public class AssessmentRecyclerViewAdapter extends RecyclerView.Adapter<AssessmentRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "bind";
    private OnAssessmentClickListener assessmentClickListener;
    TermEntity newTerm = new TermEntity();
    CourseEntity newCourse = new CourseEntity();
    AssessmentEntity newAssessment = new AssessmentEntity();
    private List<AssessmentEntity> assessmentList;
    private Context context;

    public AssessmentRecyclerViewAdapter(List<AssessmentEntity> assessmentList, Context context, CourseDetailActivity assessmentClickListener) {
        this.assessmentList = assessmentList;
        this.context = context;
        this.assessmentClickListener = assessmentClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_row, parent, false);
        return new ViewHolder(view, assessmentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentRecyclerViewAdapter.ViewHolder holder, int position) {
        AssessmentEntity newAssessment = Objects.requireNonNull(assessmentList.get(position));
        holder.title.setText(newAssessment.getTitle());
        Log.d(TAG, "onBindViewHolder: " + newAssessment.getTitle() +newAssessment.getCourseId());
    }

    public AssessmentEntity onBindViewHolder(int position) {
        AssessmentEntity newAssessment = Objects.requireNonNull(assessmentList.get(position));
        Log.d(TAG, "onBindViewHolder: " + newAssessment.getTitle()+newAssessment.getCourseId());
        return newAssessment;
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(assessmentList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AssessmentRecyclerViewAdapter.OnAssessmentClickListener onAssessmentClickListener;

        public TextView title;

        public ViewHolder(@NonNull View itemView, AssessmentRecyclerViewAdapter.OnAssessmentClickListener onAssessmentClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.row_AsmtTitle_textview);
            this.onAssessmentClickListener = onAssessmentClickListener;
            itemView.setOnClickListener(this);
            Log.d(TAG, "ViewHolder: " + this.title);
        }

        @Override
        public void onClick(View v) {
            onAssessmentClickListener.onAssessmentClick(getAdapterPosition());
        }
    }

    public interface OnAssessmentClickListener {
        void onAssessmentClick(int position);
    }
}
