package courseTracker.Utils;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.DAO.AssessmentDAO;
import courseTracker.Entity.AssessmentEntity;

public class AssessmentRepository {

    AssessmentEntity assessmentEntity =  new AssessmentEntity();

    private AssessmentDAO assessmentDAO;
    private LiveData<List<AssessmentEntity>> allAssessments;
    public AssessmentRepository(Application application) {

        TermRoomDatabase db = TermRoomDatabase.getDatabase(application);
        assessmentDAO = db.assessmentDAO();
        allAssessments = assessmentDAO.getAssociatedAssessments(assessmentEntity.getCourseId());
    }

    public LiveData<List<AssessmentEntity>> getAssociatedAssessments(int courseId) {return assessmentDAO.getAssociatedAssessments(courseId);}

    public void insert(AssessmentEntity assessmentEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.insert(assessmentEntity));
    }

    public LiveData<AssessmentEntity> get(int id) {
        return assessmentDAO.get(id);
    }
    public void update(AssessmentEntity assessmentEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.update(assessmentEntity));
    }
    public void delete(AssessmentEntity assessmentEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.delete(assessmentEntity));
    }

}
