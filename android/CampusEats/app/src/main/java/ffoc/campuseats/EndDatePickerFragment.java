package ffoc.campuseats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Will on 11/29/2016.
 */
public class EndDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        ((SubmitActivity)getActivity()).setEndDateButtonText(year,month,day);
        //((SubmitActivity)getActivity()).post.date = month + "/" + day + "/" + year;
        ((SubmitActivity)getActivity()).post.endMonth = month;
        ((SubmitActivity)getActivity()).post.endDay = day;
        ((SubmitActivity)getActivity()).post.endYear = year;
        ((SubmitActivity)getActivity()).post.endDateSet = true;

    }
}
