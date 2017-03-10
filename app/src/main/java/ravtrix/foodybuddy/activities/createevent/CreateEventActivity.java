package ravtrix.foodybuddy.activities.createevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.findresturant.FindRestaurant;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.Response;
import ravtrix.foodybuddy.network.NetworkUtil;
import ravtrix.foodybuddy.utils.Helpers;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_create_event_restaurant) protected LinearLayout setRestaurantLinear;
    @BindView(R.id.activity_create_event_setTimeLinear) protected LinearLayout setTimeLinear;
    @BindView(R.id.activity_create_event_setDateLinear) protected LinearLayout setDateLinear;
    @BindView(R.id.activity_create_form) protected RelativeLayout relativeLayoutForm;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_create_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.activity_create_etPost) protected EditText editTextPost;
    @BindView(R.id.activity_create_event_tvTime) protected TextView tvTime;
    @BindView(R.id.activity_create_event_tvDate) protected TextView tvDate;
    @BindView(R.id.activity_create_event_infoLayout) protected LinearLayout infoLayout;
    @BindView(R.id.activity_create_event_tvRestaurantName) protected TextView tvRestaurantName;
    @BindView(R.id.activity_create_event_tvAddress) protected TextView tvAddress;
    @BindView(R.id.activity_create_event_tvRestSelect) protected TextView tvRestSelect;
    @BindView(R.id.activity_create_event_floatingButtonSubmit) protected FloatingActionButton buttonSubmit;

    public static int CREATE_EVENT_REQUEST_CODE = 1;
    private CompositeSubscription mSubscriptions;

    private String eventDescription, restaurantName, restaurantAddress, restaurantID, eventDate, eventTime;
    private double restaurantLongitude, restaurantLatitude, eventTimeUnix;
    private UserLocalStore userLocalStore;
    private int dayChosen, monthChosen, yearChosen, hourChosen, minuteChosen;
    private boolean isRestaurantPicked, isDatePicked, isTimePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        ButterKnife.bind(this);

        initBooleans();

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, setRestaurantLinear);
        Helpers.overrideFonts(this, setDateLinear);
        Helpers.overrideFonts(this, setTimeLinear);
        Helpers.overrideFonts(this, relativeLayoutForm);
        setTitle("New Event");
        editTextPost.setGravity(Gravity.CENTER);

        Picasso.with(this)
                .load("http://media.tumblr.com/tumblr_md3hy6rBJ31ruz87d.png")
                .fit()
                .centerCrop()
                .into(profileImage);

        setTimeLinear.setOnClickListener(this);
        setDateLinear.setOnClickListener(this);
        setRestaurantLinear.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        mSubscriptions = new CompositeSubscription();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_create_event_setTimeLinear:
                showTimePickerDialog();
                break;
            case R.id.activity_create_event_setDateLinear:
                showDatepickerDialog();
                break;
            case R.id.activity_create_event_restaurant:
                startFindRestaurantActivity();
                break;
            case R.id.activity_create_event_floatingButtonSubmit:
                displayToastSubmit();

                long timeUnix = getUnixTimeFromEvent();

                this.eventDescription = editTextPost.getText().toString();
                // Create new event object based on user's selection
                Event event = new Event();
                event.setUser_id(userLocalStore.getLoggedInUser().getUserID());
                event.setRest_id(1111); // TO-DO, change database type to string because ID is string
                event.setAddress(this.restaurantAddress);
                event.setRest_name(this.restaurantName);
                event.setEvent_des(this.eventDescription);
                event.setLat(this.restaurantLatitude);
                event.setLng(this.restaurantLongitude);
                event.setCreate_time(System.currentTimeMillis() / 1000L);
                event.setEvent_time(timeUnix);

                mSubscriptions.add(NetworkUtil.getRawRetrofit().postEvent(event)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Response>() {

                            @Override
                            public void onCompleted() {}

                            @Override
                            public void onError(Throwable e) {}

                            @Override
                            public void onNext(Response response) {
                                Helpers.displayToast(CreateEventActivity.this, response.getMessage());
                            }

                        }));
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            tvRestSelect.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);
            tvRestaurantName.setText(data.getStringExtra("name"));
            tvAddress.setText(data.getStringExtra("address"));

            this.restaurantID = data.getStringExtra("id");
            this.restaurantName = data.getStringExtra("name");
            this.restaurantAddress = data.getStringExtra("address");
            this.restaurantLatitude = data.getDoubleExtra("latitude", 0.0);
            this.restaurantLongitude = data.getDoubleExtra("longitude", 0.0);

        }
    }

    private String getMonthToDisplay(int month) {
        String monthString = "";

        switch (month) {
            case 0:
                monthString = "Jan";
                break;
            case 1:
                monthString = "Feb";
                break;
            case 2:
                monthString = "Mar";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "Aug";
                break;
            case 8:
                monthString = "Sept";
                break;
            case 9:
                monthString = "Oct";
                break;
            case 10:
                monthString = "Nov";
                break;
            case 11:
                monthString = "Dec";
                break;
            default:
                break;
        }
        return monthString;
    }

    private void showTimePickerDialog() {
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

                        isTimePicked = true;

                        hourChosen = hourOfDay;
                        minuteChosen = minute;

                        String minuteDisplay = Integer.toString(minute);

                        String am_pm =  (hourOfDay >= 12) ? "PM" : "AM";

                        String strHrsToShow = "";
                        if (hourOfDay == 0) {
                            strHrsToShow += "12";
                        } else if (hourOfDay > 12) {
                            strHrsToShow = hourOfDay - 12 + "";
                        } else {
                            strHrsToShow = hourOfDay + "";
                        }

                        if (minute < 10) {
                            minuteDisplay = "0" + minuteDisplay;
                        }

                        tvTime.setText(strHrsToShow + ":" + minuteDisplay + " " + am_pm);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.setTitle("");
        timePickerDialog.show();
    }

    private void showDatepickerDialog() {

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

                        isDatePicked = true;

                        yearChosen = year;
                        monthChosen = monthOfYear + 1;
                        dayChosen = dayOfMonth;

                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE", Locale.US);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Date d_name = cal.getTime();
                        String dayOfTheWeek = sdf.format(d_name);

                        String monthToDisplay = getMonthToDisplay(monthOfYear);
                        tvDate.setText(dayOfTheWeek + " " + monthToDisplay + "/" + dayOfMonth + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private long getUnixTimeFromEvent() {
        String time = dayChosen + "-" + monthChosen + "-" + yearChosen + " " + hourChosen + ":" + minuteChosen;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US);
        try {
            Date inputDate = dateFormat.parse(time);
            return  inputDate.getTime() / 1000;
        } catch (ParseException e) {e.printStackTrace();}
        return 0;
    }

    private void startFindRestaurantActivity() {
        startActivityForResult(new Intent(CreateEventActivity.this, FindRestaurant.class), CREATE_EVENT_REQUEST_CODE);
    }

    private void displayToastSubmit() {
        Helpers.displayToast(this, "Clicked Submit...");
        finish(); // go back to screen before
    }

    private void initBooleans() {
        isRestaurantPicked = false;
        isDatePicked = false;
        isTimePicked = false;
    }
}
