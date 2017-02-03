package ravtrix.foodybuddy.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.findresturant.FindRestaurant;
import ravtrix.foodybuddy.utils.Helpers;

public class CreateEventActivity extends AppCompatActivity {

    @BindView(R.id.activity_create_test) protected LinearLayout linearTest;
    @BindView(R.id.activity_create_event_setTimeLinear) protected LinearLayout setTimeLinear;
    @BindView(R.id.activity_create_event_setDateLinear) protected LinearLayout setDateLinear;
    @BindView(R.id.activity_create_form) protected RelativeLayout relativeLayoutForm;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_create_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.activity_create_etPost) protected EditText editTextPost;
    @BindView(R.id.activity_create_event_tvTime) protected TextView tvTime;
    @BindView(R.id.activity_create_event_tvDate) protected TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);
        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, linearTest);
        Helpers.overrideFonts(this, setTimeLinear);
        Helpers.overrideFonts(this, relativeLayoutForm);
        setTitle("New Event");
        editTextPost.setGravity(Gravity.CENTER);

        editTextPost.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0){
                    // position the text type in the left top corner
                    editTextPost.setGravity(Gravity.START | Gravity.TOP);
                } else {
                    // no text entered. Center the hint text.
                    editTextPost.setGravity(Gravity.CENTER);
                }
            }
        });

        Picasso.with(this)
                .load("http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png")
                .fit()
                .centerCrop()
                .into(profileImage);

        setTimeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String am_pm = "";

                                if (hourOfDay >= 12)
                                    am_pm = "PM";
                                else
                                    am_pm = "AM";

                                String strHrsToShow = "";
                                if (hourOfDay == 0) {
                                   strHrsToShow += "12";
                                } else if (hourOfDay > 12) {
                                   strHrsToShow = hourOfDay - 12 + "";
                                } else {
                                    strHrsToShow = hourOfDay + "";
                                }

                                tvTime.setText(strHrsToShow + ":" + minute + " " + am_pm);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.setTitle("");
                timePickerDialog.show();
            }
        });

        setDateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        linearTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateEventActivity.this, FindRestaurant.class));
            }
        });
    }
}
