package ffoc.campuseats;

import android.app.Dialog;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import android.view.View;


import java.util.Calendar;

/**
 * Created by Will on 9/6/2016.
 */
public class EndTimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        ((SubmitActivity)getActivity()).setEndTimeButtonText(hourOfDay ,minute);
        ((SubmitActivity)getActivity()).post.endHour = hourOfDay;
        ((SubmitActivity)getActivity()).post.endMinute = minute;
        ((SubmitActivity)getActivity()).post.endTimeSet = true;

    }
}