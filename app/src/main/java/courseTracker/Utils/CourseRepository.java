package courseTracker.Utils;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.DAO.CourseDAO;
import courseTracker.Entity.CourseEntity;

public class CourseRepository {

    private CourseDAO courseDAO;
    private LiveData<List<CourseEntity>>allCourses;
    CourseEntity courseEntity = new CourseEntity();
    public CourseRepository(Application application) {
        TermRoomDatabase db = TermRoomDatabase.getDatabase(application);

        courseDAO = db.courseDAO();

        allCourses = courseDAO.getAssociatedCourses(courseEntity.getTermId());
    }

    public LiveData<List<CourseEntity>> getTermCourses(int termId) {return courseDAO.getAssociatedCourses(termId);}

    public void insert(CourseEntity courseEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> courseDAO.insert(courseEntity));
    }

    public LiveData<CourseEntity> get(int id) {
        return courseDAO.get(id);
    }
    public void update(CourseEntity courseEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> courseDAO.update(courseEntity));
    }
    public void delete(CourseEntity courseEntity) {
        TermRoomDatabase.databaseWriteExecutor.execute(() -> courseDAO.delete(courseEntity));
    }
}
