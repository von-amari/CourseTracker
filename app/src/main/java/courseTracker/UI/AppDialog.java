package courseTracker.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

import com.courseTracker.R;

public class AppDialog extends DialogFragment {

    public static final String TAG = "appDialog:";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE= "message";
    public static final String DIALOG_POSITIVE_RID="positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    public void show(FragmentManager fragmentManager, Object o) {
    }

    interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);
        void onNegativeDialogResult(int dialogId, Bundle args);
        void onDialogCancelledI(int dialogId);
    }

    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);

        if(!(context instanceof  DialogEvents)){
            throw new ClassCastException((context.toString() + "must implement AppData "));
        }
        mDialogEvents = (DialogEvents) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: Entering ... ");
        super.onDetach();

        mDialogEvents = null;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.d(TAG, "onCreateDialog: starts");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final Bundle arguments = getArguments();
            final int dialogId;
            String messageString;
            int positiveStringId;
            int negativeStringId;

            if(arguments !=null) {
                dialogId = arguments.getInt(DIALOG_ID);
                messageString = arguments.getString(DIALOG_MESSAGE);

                if(dialogId == 0 || messageString == null){
                    throw new IllegalArgumentException(("Dialog_id and/or"));
                }
                positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
                // negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
                if (positiveStringId == 0) {
                    positiveStringId = R.string.ok;
                }
                negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
                if (negativeStringId == 0) {
                    negativeStringId = R.string.cancel;
                }
            }
                else {
                    throw new IllegalArgumentException(",dsfr");
                }

        builder.setMessage(messageString)
                .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //callback positive result method
                        if(mDialogEvents != null) {
                            mDialogEvents.onPositiveDialogResult(dialogId, arguments);
                        }
                    }
                })
                .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //callback negative result method
                        if(mDialogEvents != null) {
                            mDialogEvents.onNegativeDialogResult(dialogId, arguments);
                        }
                    }
                });
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public  void onCancel(DialogInterface dialog){
        //super.onCancel(dialog);

        Log.d(TAG, "onCancel: called");
        if (mDialogEvents !=null) {
            int dialogId = getArguments().getInt((DIALOG_ID));
            mDialogEvents.onDialogCancelledI(dialogId);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.d(TAG, "onDismiss: called");
    }

}
