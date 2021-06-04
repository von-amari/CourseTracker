package courseTracker.Entity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import courseTracker.Utils.TermRepository;

public class TermViewModel extends AndroidViewModel {

    public static TermRepository repository;
    public final LiveData<List<TermEntity>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllData();
    }

    public LiveData<List<TermEntity>> getAllTerms() { return allTerms; }
    public static void insert(TermEntity termEntity) { repository.insert(termEntity); }

    public LiveData<TermEntity> get(int id) { return repository.get(id);}
    public static void update(TermEntity termEntity) { repository.update(termEntity);}
    public static void delete(TermEntity termEntity) { repository.delete(termEntity);}
}
