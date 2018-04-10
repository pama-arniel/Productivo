package cmsc127project.mybudgetplanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Button addButton, searchButton;
    TextView totalOrigMoney, totalMoneySpent, totalAddedMoney, totalCurrentMoney;
    ListView listViewInMenu;

    private static final int ADDBUDGET_ACTIVITY = 1;
    private static final int DISPLAYBUDGET_ACTIVITY = 2;
    DBHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        myDBHelper = new DBHelper(this.getApplicationContext());
        //addButton = (Button) findViewById(R.id.mainMenuAddButton);
        //searchButton = (Button) findViewById(R.id.mainMenuSearchButton);
        totalOrigMoney = (TextView) findViewById(R.id.totalOrigMoney);
        totalMoneySpent = (TextView) findViewById(R.id.totalMoneySpent);
        totalAddedMoney = (TextView) findViewById(R.id.totalAddedMoney);
        totalCurrentMoney = (TextView) findViewById(R.id.totalCurrentMoney);

        listViewInMenu = (ListView) findViewById(R.id.mainMenuListView);
        populateMainMenuListViewFromDB();
        registerListClickCallback();
    }

    @Override
    public void onDestroy() {
        myDBHelper.close();
        super.onDestroy();
        getDelegate().onDestroy();
    }

    /*
	 * UI Button Callbacks
	 */
    protected void populateMainMenuListViewFromDB(){
        List<Budget> allBudgets = myDBHelper.getAllBudgets();

        if (listViewInMenu.getAdapter() == null) {
            BudgetsCustomAdapter listadapter = new BudgetsCustomAdapter(this.getApplicationContext(), allBudgets);
            listadapter.notifyDataSetChanged();
            listViewInMenu.setAdapter(listadapter);
        } else {
            ((BudgetsCustomAdapter)listViewInMenu.getAdapter()).updateData(allBudgets);
            listViewInMenu.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        }
    }

    private void registerListClickCallback(){
        listViewInMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView budgetName = (TextView) view.findViewById(R.id.budgetNameDisplay);
                        TextView percent = (TextView) view.findViewById(R.id.currentPercent);
                        Toast.makeText(getApplicationContext(),
                                budgetName.getText().toString() + "\n" + percent.getText().toString()
                                , Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void respondToAdd(View v){
        Intent addNewBudget = new Intent(MainActivity.this, AddNewBudget.class);
        startActivityForResult(addNewBudget, ADDBUDGET_ACTIVITY);
    }

    //TODO after finishing updating and adding methods
    /*public void respondToSearch(View v){
        myDBHelper.deleteAllBudgets();
        populateMainMenuListViewFromDB();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TODO Remove redundancy here later
        if(requestCode == ADDBUDGET_ACTIVITY && resultCode == Activity.RESULT_OK){
            populateMainMenuListViewFromDB();
        }
        if(requestCode == DISPLAYBUDGET_ACTIVITY && resultCode == Activity.RESULT_OK){
            populateMainMenuListViewFromDB();
        }
    }

    public class BudgetsCustomAdapter extends ArrayAdapter {
        List<Budget> allBudgets;
        BudgetsCustomAdapter(Context context, List<Budget> allBudgets){
            super(context, R.layout.budgetlist_custom_layout, allBudgets);
            this.allBudgets = allBudgets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.budgetlist_custom_layout, parent, false);
            final Budget budget = allBudgets.get(position);
            TextView budgetName = (TextView) rowView.findViewById(R.id.budgetNameDisplay);
            TextView percent = (TextView) rowView.findViewById(R.id.currentPercent);
            TextView date = (TextView) rowView.findViewById(R.id.budgetTxtDate);
            ImageView editButton = (ImageView) rowView.findViewById(R.id.btnEdit);
            ImageView deleteButton = (ImageView) rowView.findViewById(R.id.btnDelete);

            if(budget!=null){
                budgetName.setText(budget.getText());
                date.setText(budget.getDateLabel());
                percent.setText(percent.getText() + budget.getPercent() + "%");
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editBudgetUnit(budget);
                    }
                });
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteBudgetUnit(budget);
                    }
                });
            }


            return rowView;
        }

        public void updateData(List<Budget> allBudgets){
            this.allBudgets.clear();
            this.allBudgets.addAll(allBudgets);
            notifyDataSetChanged();
        }

        public void editBudgetUnit(Budget budget){
            Intent displayBudgetItem = new Intent(MainActivity.this, EditBudget.class);
            displayBudgetItem.putExtra("BUDGET", budget);
            //displayBudgetItem.putExtra("POSITION", getPosition(budget));
            startActivityForResult(displayBudgetItem, DISPLAYBUDGET_ACTIVITY);
            //startActivity(displayBudgetItem);
        }

        public void deleteBudgetUnit(Budget budget){
            myDBHelper.deleteBudgetUnit(budget);
            BudgetsCustomAdapter listadapter = (BudgetsCustomAdapter) listViewInMenu.getAdapter();
            listadapter.remove(budget);
            listadapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),
                    "Deleted budget unit!"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}
