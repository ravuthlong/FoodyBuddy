package ravtrix.foodybuddy.activities.restaurantfilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.utils.Helpers;

public class RestaurantFilterActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_restaurant_distanceDrop) protected Spinner distanceSpinner;
    @BindView(R.id.activity_restaurant_priceDrop) protected Spinner priceSpinner;
    @BindView(R.id.activity_restaurant_sortDrop) protected Spinner sortSpinner;
    @BindView(R.id.activity_restaurant_tvReset) protected TextView tvReset;
    @BindView(R.id.activity_restaurant_bApplyFilter) protected Button bApplyFilter;
    @BindView(R.id.activity_restaurant_filter_layoutMain) protected LinearLayout layoutMain;
    @BindView(R.id.activity_restaurant_bAll) protected Button bAll;
    @BindView(R.id.activity_restaurant_bAmerican) protected Button bAmerican;
    @BindView(R.id.activity_restaurant_bBurgers) protected Button bBurgers;
    @BindView(R.id.activity_restaurant_bBrazilian) protected Button bBrazilian;
    @BindView(R.id.activity_restaurant_bCambodian) protected Button bCambodian;
    @BindView(R.id.activity_restaurant_bChinese) protected Button bChinese;
    @BindView(R.id.activity_restaurant_bFilipino) protected Button bFilipino;
    @BindView(R.id.activity_restaurant_bIndian) protected Button bIndian;
    @BindView(R.id.activity_restaurant_bItalian) protected Button bItalian;
    @BindView(R.id.activity_restaurant_bJapanese) protected Button bJapanese;
    @BindView(R.id.activity_restaurant_bKorean) protected Button bKorean;
    @BindView(R.id.activity_restaurant_bLaotian) protected Button bLaotian;
    @BindView(R.id.activity_restaurant_bMexican) protected Button bMexican;
    @BindView(R.id.activity_restaurant_bMoroccan) protected Button bMoroccan;
    @BindView(R.id.activity_restaurant_bPeruvian) protected Button bPeruvian;
    @BindView(R.id.activity_restaurant_bPortuguese) protected Button bPortuguese;
    @BindView(R.id.activity_restaurant_bRussian) protected Button bRussian;
    @BindView(R.id.activity_restaurant_bScottish) protected Button bScottish;
    @BindView(R.id.activity_restaurant_bTaiwanese) protected Button bTaiwanese;
    @BindView(R.id.activity_restaurant_bThai) protected Button bThai;
    @BindView(R.id.activity_restaurant_bVietnamese) protected Button bVietnamese;
    @BindView(R.id.activity_restaurant_bBakeries) protected Button bBakeries;
    @BindView(R.id.activity_restaurant_bBubbleTea) protected Button bBubbleTea;
    @BindView(R.id.activity_restaurant_bDesserts) protected Button bDesserts;

    private int distanceSpinnerSelected = 0;
    private int priceSpinnerSelected = 0;
    private int sortSpinnerSelected = 0;
    private String keywordSelected = "";
    private Button lastSelectedButton = null;
    private Button pastSelectedButton = null; // saved by this activity, passed to FindRestaurant activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_filter);
        ButterKnife.bind(this);

        Helpers.setToolbar(this, toolbar);
        Helpers.overrideFonts(this, layoutMain);

        fillSpinners();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            distanceSpinner.setSelection(bundle.getInt("distanceSelected"));
            priceSpinner.setSelection(bundle.getInt("priceSelected"));
            sortSpinner.setSelection(bundle.getInt("sortSelected"));

            // first time opening filter will be empty
            if (!bundle.getString("termSelected").isEmpty()) {
                setPastButtonSelected(bundle.getString("termSelected"));
                keywordSelected = bundle.getString("termSelected");
            }
        }

        // set listeners
        distanceSpinner.setOnItemSelectedListener(this);
        priceSpinner.setOnItemSelectedListener(this);
        sortSpinner.setOnItemSelectedListener(this);
        tvReset.setOnClickListener(this);
        bApplyFilter.setOnClickListener(this);

        // set filter buttons listeners
        setFilterButtonListeners();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int selected, long l) {

        switch (adapterView.getId()) {
            case R.id.activity_restaurant_distanceDrop:
                this.distanceSpinnerSelected = selected;
                break;
            case R.id.activity_restaurant_priceDrop:
                this.priceSpinnerSelected = selected;
                break;
            case R.id.activity_restaurant_sortDrop:
                this.sortSpinnerSelected = selected;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_restaurant_tvReset:
                // reset all the value
                distanceSpinner.setSelection(0);
                sortSpinner.setSelection(0);
                priceSpinner.setSelection(0);
                if (null != lastSelectedButton) {
                    deselectLastButton(lastSelectedButton);
                }
                break;
            case R.id.activity_restaurant_bApplyFilter:

                /**
                 * When user clicks filter, collect all information selected on filter page
                 */
                Intent resultIntent = new Intent();
                resultIntent.putExtra("distance", getDistanceSpinnerValue());
                resultIntent.putExtra("price", getPriceSpinnerValue());
                resultIntent.putExtra("sort", getSortSpinnerValue());
                resultIntent.putExtra("distanceSelected", this.distanceSpinnerSelected);
                resultIntent.putExtra("priceSelected", this.priceSpinnerSelected);
                resultIntent.putExtra("sortSelected", this.sortSpinnerSelected);
                resultIntent.putExtra("term", this.keywordSelected);


                System.out.println("DISTANCE: " + getDistanceSpinnerValue());
                System.out.println("PRICE: " + getPriceSpinnerValue());
                System.out.println("SORT: " + getSortSpinnerValue());
                System.out.println("DISTANCE SELECTED: " + this.distanceSpinnerSelected);
                System.out.println("PRICE SELECTED: " + this.priceSpinnerSelected);
                System.out.println("SORT SELECTED: " + this.sortSpinnerSelected);
                System.out.println("TERM: " + this.keywordSelected);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            case R.id.activity_restaurant_bAmerican:
                keywordSelected = "american";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bAmerican;
                setButtonSelected(bAmerican);
                break;
            case R.id.activity_restaurant_bAll:
                keywordSelected = "all";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bAll;
                setButtonSelected(bAll);
                break;
            case R.id.activity_restaurant_bBurgers:
                keywordSelected = "burgers";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bBurgers;
                setButtonSelected(bBurgers);
                break;
            case R.id.activity_restaurant_bBrazilian:
                keywordSelected = "brazilian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bBrazilian;
                setButtonSelected(bBrazilian);
                break;
            case R.id.activity_restaurant_bCambodian:
                keywordSelected = "cambodian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bCambodian;
                setButtonSelected(bCambodian);
                break;
            case R.id.activity_restaurant_bChinese:
                keywordSelected = "chinese";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bChinese;
                setButtonSelected(bChinese);
                break;
            case R.id.activity_restaurant_bFilipino:
                keywordSelected = "filipino";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bFilipino;
                setButtonSelected(bFilipino);
                break;
            case R.id.activity_restaurant_bIndian:
                keywordSelected = "indian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bIndian;
                setButtonSelected(bIndian);
                break;
            case R.id.activity_restaurant_bItalian:
                keywordSelected = "italian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bItalian;
                setButtonSelected(bItalian);
                break;
            case R.id.activity_restaurant_bJapanese:
                keywordSelected = "japanese";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bJapanese;
                setButtonSelected(bJapanese);
                break;
            case R.id.activity_restaurant_bKorean:
                keywordSelected = "korean";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bKorean;
                setButtonSelected(bKorean);
                break;
            case R.id.activity_restaurant_bLaotian:
                keywordSelected = "laotian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bLaotian;
                setButtonSelected(bLaotian);
                break;
            case R.id.activity_restaurant_bMexican:
                keywordSelected = "mexican";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bMexican;
                setButtonSelected(bMexican);
                break;
            case R.id.activity_restaurant_bMoroccan:
                keywordSelected = "moroccan";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bMoroccan;
                setButtonSelected(bMoroccan);
                break;
            case R.id.activity_restaurant_bPeruvian:
                keywordSelected = "peruvian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bPeruvian;
                setButtonSelected(bPeruvian);
                break;
            case R.id.activity_restaurant_bPortuguese:
                keywordSelected = "portuguese";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bPortuguese;
                setButtonSelected(bPortuguese);
                break;
            case R.id.activity_restaurant_bRussian:
                keywordSelected = "russian";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bRussian;
                setButtonSelected(bRussian);
                break;
            case R.id.activity_restaurant_bScottish:
                keywordSelected = "scottish";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bScottish;
                setButtonSelected(bScottish);
                break;
            case R.id.activity_restaurant_bTaiwanese:
                keywordSelected = "taiwanese";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bTaiwanese;
                setButtonSelected(bTaiwanese);
                break;
            case R.id.activity_restaurant_bThai:
                keywordSelected = "thai";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bThai;
                setButtonSelected(bThai);
                break;
            case R.id.activity_restaurant_bVietnamese:
                keywordSelected = "vietnamese";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bVietnamese;
                setButtonSelected(bVietnamese);
                break;
            case R.id.activity_restaurant_bBakeries:
                keywordSelected = "bakeries";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bBakeries;
                setButtonSelected(bBakeries);
                break;
            case R.id.activity_restaurant_bBubbleTea:
                keywordSelected = "bubbletea";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bBubbleTea;
                setButtonSelected(bBubbleTea);
                break;
            case R.id.activity_restaurant_bDesserts:
                keywordSelected = "desserts";
                deselectLastButton(lastSelectedButton);
                lastSelectedButton = bDesserts;
                setButtonSelected(bDesserts);
                break;
            default:
                break;
        }
    }

    private String getDistanceSpinnerValue() {

        String distanceValue = "";

        switch (distanceSpinner.getSelectedItemPosition()){
            case 0: //All
                distanceValue = "";
                break;
            case 1: // 5 miles
                distanceValue = "5";
                break;
            case 2: // 10 miles
                distanceValue = "10";
                break;
            case 3: // 15 miles
                distanceValue = "15";
                break;
            case 4: // 20 miles
                distanceValue = "20";
                break;
            default:
                break;
        }
        return distanceValue;
    }

    private String getPriceSpinnerValue() {
        String priceValue = "";

        switch (priceSpinner.getSelectedItemPosition()){
            case 0:
                priceValue = "";
                break;
            case 1:
                priceValue = "1";
                break;
            case 2:
                priceValue = "2";
                break;
            case 3:
                priceValue = "3";
                break;
            case 4:
                priceValue = "4";
                break;
            default:
                break;
        }
        return priceValue;
    }

    private String getSortSpinnerValue() {
        String sortValue = "";

        switch (sortSpinner.getSelectedItemPosition()){
            case 0:
                sortValue = "best_match";
                break;
            case 1:
                sortValue = "distance";
                break;
            case 2:
                sortValue = "rating";
                break;
            case 3:
                sortValue = "review_count";
                break;
            default:
                break;
        }
        return sortValue;
    }

    private void deselectLastButton(Button lastButton) {
        if (null != lastButton) {
            lastButton.setBackgroundResource(R.drawable.button_filter);
        }
    }

    private void setButtonSelected(Button button) {
        button.setBackgroundResource(R.color.greenLighter);
    }

    private void setPastButtonSelected(String buttonName) {
        switch (buttonName) {
            case "american":
                lastSelectedButton = bAmerican;
                setButtonSelected(bAmerican);
                break;
            case "all":
                lastSelectedButton = bAll;
                setButtonSelected(bAll);
                break;
            case "burgers":
                lastSelectedButton = bBurgers;
                setButtonSelected(bBurgers);
                break;
            case "brazilian":
                lastSelectedButton = bBrazilian;
                setButtonSelected(bBrazilian);
                break;
            case "cambodian":
                lastSelectedButton = bCambodian;
                setButtonSelected(bCambodian);
                break;
            case "chinese":
                lastSelectedButton = bChinese;
                setButtonSelected(bChinese);
                break;
            case "filipino":
                lastSelectedButton = bFilipino;
                setButtonSelected(bFilipino);
                break;
            case "indian":
                lastSelectedButton = bIndian;
                setButtonSelected(bIndian);
                break;
            case "italian":
                lastSelectedButton = bItalian;
                setButtonSelected(bItalian);
                break;
            case "japanese":
                lastSelectedButton = bJapanese;
                setButtonSelected(bJapanese);
                break;
            case "korean":
                lastSelectedButton = bKorean;
                setButtonSelected(bKorean);
                break;
            case "laotian":
                lastSelectedButton = bLaotian;
                setButtonSelected(bLaotian);
                break;
            case "mexican":
                lastSelectedButton = bMexican;
                setButtonSelected(bMexican);
                break;
            case "moroccan":
                lastSelectedButton = bMoroccan;
                setButtonSelected(bMoroccan);
                break;
            case "peruvian":
                lastSelectedButton = bPeruvian;
                setButtonSelected(bPeruvian);
                break;
            case "portuguese":
                lastSelectedButton = bPortuguese;
                setButtonSelected(bPortuguese);
                break;
            case "russian":
                lastSelectedButton = bRussian;
                setButtonSelected(bRussian);
                break;
            case "scottish":
                lastSelectedButton = bScottish;
                setButtonSelected(bScottish);
                break;
            case "taiwanese":
                lastSelectedButton = bTaiwanese;
                setButtonSelected(bTaiwanese);
                break;
            case "thai":
                lastSelectedButton = bThai;
                setButtonSelected(bThai);
                break;
            case "vietnamese":
                lastSelectedButton = bVietnamese;
                setButtonSelected(bVietnamese);
                break;
            case "bakeries":
                lastSelectedButton = bBakeries;
                setButtonSelected(bBakeries);
                break;
            case "bubbletea":
                lastSelectedButton = bBubbleTea;
                setButtonSelected(bBubbleTea);
                break;
            case "desserts":
                lastSelectedButton = bDesserts;
                setButtonSelected(bDesserts);
                break;
            default:
                break;
        }
    }

    private void fillSpinners() {
        ArrayAdapter<CharSequence> arrayAdapterDistance =  ArrayAdapter.createFromResource(this, R.array.distance_categories, android.R.layout.simple_spinner_item);
        arrayAdapterDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(arrayAdapterDistance);

        ArrayAdapter<CharSequence> arrayAdapterPrice =  ArrayAdapter.createFromResource(this, R.array.price_categories, android.R.layout.simple_spinner_item);
        arrayAdapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(arrayAdapterPrice);

        ArrayAdapter<CharSequence> arrayAdapterRating =  ArrayAdapter.createFromResource(this, R.array.sort_categories, android.R.layout.simple_spinner_item);
        arrayAdapterRating.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(arrayAdapterRating);
    }

    private void setFilterButtonListeners() {
        bAmerican.setOnClickListener(this);
        bAll.setOnClickListener(this);
        bBurgers.setOnClickListener(this);
        bBrazilian.setOnClickListener(this);
        bCambodian.setOnClickListener(this);
        bChinese.setOnClickListener(this);
        bFilipino.setOnClickListener(this);
        bIndian.setOnClickListener(this);
        bItalian.setOnClickListener(this);
        bJapanese.setOnClickListener(this);
        bKorean.setOnClickListener(this);
        bLaotian.setOnClickListener(this);
        bMexican.setOnClickListener(this);
        bMoroccan.setOnClickListener(this);
        bPeruvian.setOnClickListener(this);
        bPortuguese.setOnClickListener(this);
        bRussian.setOnClickListener(this);
        bScottish.setOnClickListener(this);
        bTaiwanese.setOnClickListener(this);
        bThai.setOnClickListener(this);
        bVietnamese.setOnClickListener(this);
        bBakeries.setOnClickListener(this);
        bBubbleTea.setOnClickListener(this);
        bDesserts.setOnClickListener(this);
    }

}
