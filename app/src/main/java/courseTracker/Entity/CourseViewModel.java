package courseTracker.Entity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.Utils.CourseRepository;


public class CourseViewModel extends AndroidViewModel {

    CourseEntity newCourse = new CourseEntity();

    public static CourseRepository repository;
    public final LiveData<List<CourseEntity>> allCourses;


    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getTermCourses(newCourse.getTermId());
    }

    //public LiveData<List<CourseEntity>> getAllCourses() { return allCourses; }
   public LiveData<List<CourseEntity>> getAllTermCourses(int termId) { return repository.getTermCourses(termId); }
    public static void insert(CourseEntity courseEntity) { repository.insert(courseEntity); }

    public LiveData<CourseEntity> get(int id) { return repository.get(id);}
    public static void update(CourseEntity courseEntity) { repository.update(courseEntity);}
    public static void delete(CourseEntity courseEntity) { repository.delete(courseEntity);}
}