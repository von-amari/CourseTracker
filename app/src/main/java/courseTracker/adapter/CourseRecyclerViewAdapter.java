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
import courseTracker.Entity.CourseEntity;
import courseTracker.UI.TermDetailActivity;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "binder";
    private OnCourseClickListener courseClickListener;
    private List<CourseEntity> courseList;
    private Context context;

    public CourseRecyclerViewAdapter(List<CourseEntity> courseList, Context context, TermDetailActivity courseClickListener) {
        this.courseList = courseList;
        this.context = context;
        this.courseClickListener = (OnCourseClickListener) courseClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_row, parent, false);
        return new ViewHolder(view, courseClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseEntity newCourse = Objects.requireNonNull(courseList.get(position));;
        holder.title.setText(newCourse.getTitle());

    }

    public CourseEntity onBindViewHolder(int position) {
        CourseEntity courseEntity = Objects.requireNonNull(courseList.get(position));
        Log.d(TAG, "onBindViewHolder: " + courseEntity.getTitle());
        return courseEntity;
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(courseList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnCourseClickListener oncourseClickListener;

        public TextView title;

        public ViewHolder(@NonNull View itemView, OnCourseClickListener onCourseClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.row_CourseTitle_textview);
            this.oncourseClickListener = onCourseClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            oncourseClickListener.oncourseClick(getAdapterPosition());
        }
    }

    public interface OnCourseClickListener {
        void oncourseClick(int position);
    }
}
