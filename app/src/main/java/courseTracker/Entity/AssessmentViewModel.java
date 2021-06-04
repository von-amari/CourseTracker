package courseTracker.Entity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.UI.AddAssessmentsActivity;
import courseTracker.UI.CourseDetailActivity;
import courseTracker.Utils.AssessmentRepository;
import courseTracker.Utils.CourseRepository;

public class AssessmentViewModel extends AndroidViewModel {

    CourseEntity newCourse = new CourseEntity();
    public static AssessmentRepository repository;
    public final LiveData<List<AssessmentEntity>> allAssessments;


    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAssociatedAssessments(newCourse.getId());
    }
    public LiveData<List<AssessmentEntity> > getAllAssessments(int id) {
        return repository.getAssociatedAssessments(id); }

    public static void insert(AssessmentEntity assessmentEntity) { repository.insert(assessmentEntity); }

    public LiveData<AssessmentEntity> get(int id) { return repository.get(id);}
    public static void update(AssessmentEntity assessmentEntity) { repository.update(assessmentEntity);}
    public static void delete(AssessmentEntity assessmentEntity) { repository.delete(assessmentEntity);}

}
