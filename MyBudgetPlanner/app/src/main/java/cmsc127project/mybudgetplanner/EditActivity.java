package cmsc127project.mybudgetplanner;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class EditActivity extends AppCompatActivity {
    private EditText displayActivityName, editActivityMoney, editActivityDescription;
    private TextView displayActivityDate, displayActivityMoney;
    private Button displayActivityUpdateButton;

    DBHelper myDBHelper;
    ActivityUnit activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity_item);
        myDBHelper = new DBHelper(this.getApplicationContext());

        displayActivityMoney = (TextView) findViewById(R.id.displayActivityMoney);
        displayActivityDate = (TextView) findViewById(R.id.displayActivityDate);

        displayActivityName = (EditText) findViewById(R.id.displayActivityName);
        editActivityMoney = (EditText) findViewById(R.id.editActivityMoney);
        editActivityDescription = (EditText) findViewById(R.id.editActivityDescription);

        displayActivityUpdateButton = (Button) findViewById(R.id.displayActivityUpdateButton);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            activity = (ActivityUnit) bundle.get("ACTIVITY");

            if(activity != null){
                displayActivityName.setText(activity.getName());
                editActivityMoney.setText(activity.getMoney());
                editActivityDescription.setText(activity.getNote());
                displayActivityDate.setText(activity.getDateLabel());
            }
        }

        if(activity.getType()==ActivityUnit.MONEY_ADDED){
            displayActivityMoney.setText("Money Added:");
        }else{
            if(activity.getType()==ActivityUnit.MONEY_SPENT){
                displayActivityMoney.setText("Money Spent:");
            }
        }
    }

    @Override
    protected void onDestroy() {
        myDBHelper.close();
        super.onDestroy();
        getDelegate().onDestroy();
    }

    /*
	 * UI Button Callbacks
	 */
    public void activityRespondToUpdate(View v){
        String name = displayActivityName.getText().toString();
        String moneyInvolved = editActivityMoney.getText().toString();
        String note = editActivityDescription.getText().toString();
        boolean moneyError = false;
        boolean nameError = false;

        if(TextUtils.isEmpty(name)){
            displayActivityName.setError("This item cannot be empty.");
            nameError = true;
        }

        if(TextUtils.isEmpty(moneyInvolved)){
            editActivityMoney.setError("This item cannot be empty.");
            moneyError = true;
        }else{
            BigDecimal origMoney = new BigDecimal( moneyInvolved );
            if(origMoney.compareTo(new BigDecimal(0.0)) <= 0){
                editActivityMoney.setError("Your money must be above 0.");
                moneyError = true;
            }
        }

        if(!nameError && !moneyError){
            BigDecimal money = new BigDecimal( moneyInvolved );
            activity.setName(name);
            activity.setNote(note);
            if(activity.getType()==ActivityUnit.MONEY_ADDED){
                activity.setAddedMoney(money);
            }else{
                if(activity.getType()==ActivityUnit.MONEY_SPENT){
                    activity.setSpentMoney(money);
                }
            }
            myDBHelper.updateActivity(activity);
            Toast.makeText(getApplicationContext(),
                    "Edited activity unit!", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }


}
