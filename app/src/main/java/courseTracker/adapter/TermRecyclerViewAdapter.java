package courseTracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.courseTracker.R;

import java.util.List;
import java.util.Objects;

import courseTracker.Entity.TermEntity;
import courseTracker.UI.AllTermsActivity;

public class TermRecyclerViewAdapter extends RecyclerView.Adapter<TermRecyclerViewAdapter.ViewHolder> {
    private OnTermClickListener termClickListener;

    private List<TermEntity> termList;
    private Context context;

    public TermRecyclerViewAdapter(List<TermEntity> termList, Context context, AllTermsActivity termClickListener) {
        this.termList = termList;
        this.context = context;
        this.termClickListener = termClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_row, parent, false);
        return new ViewHolder(view, termClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TermEntity newTerm = Objects.requireNonNull(termList.get(position));
        holder.title.setText(newTerm.getTitle());

    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(termList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnTermClickListener onTermClickListener;

        public TextView title;
        public TextView num;

        public ViewHolder(@NonNull View itemView, OnTermClickListener onTermClickListener) {
            super(itemView);
           title = itemView.findViewById(R.id.row_TermTitle_textview);
           // num = itemView.findViewById(R.id.test);


           this.onTermClickListener = onTermClickListener;
           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           onTermClickListener.onTermClick(getAdapterPosition());
        }
    }

    public interface OnTermClickListener {
        void onTermClick(int position);
    }
}
