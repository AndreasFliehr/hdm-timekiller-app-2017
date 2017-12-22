package de.hdm.dp.bd.chronophage;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdm.dp.bd.chronophage.database.TaskDatabaseInMemoryMock;
import de.hdm.dp.bd.chronophage.models.Task;


/**
 * EvalActivity ist die Aktivität, die beim Klick auf den Eintrag
 * "Evaluation" im Drawer-Menü aufgerufen wird
 */
public class EvalActivity extends CommonActivity {
    private EditText startDateEditText;
    private EditText endDateEditText;

    /**
     * diese Methode muss nicht verändert werden, sie baut das Kuchendiagramm auf
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval);
        createNavigationBars();

        // Date filter
        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);

        //Content of the pie chart
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        PieDataSet dataset = new PieDataSet(getEntries(), "Time spent");

        //Set the data
        PieData data = new PieData(getLabels(), dataset); // initialize PieData
        pieChart.setData(data); //set data into chart

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
        dataset.setValueTextSize(16);

        setupDatePicker();
    }


    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // implement the date picker dialog
        final DatePickerDialog startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //TODO hier wird die Reaktion auf as vom Benutzer eeingegebene Datum reagiert
                //Hier: Anzeige des Datums im Textfeld
                openDatePicker(year, month, day, startDateEditText);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
            .DAY_OF_MONTH));

        final DatePickerDialog endDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //TODO hier wird die Reaktion auf as vom Benutzer eeingegebene Datum reagiert
                //Hier: Anzeige des Datums im Textfeld
                openDatePicker(year, month, day, endDateEditText);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
            .DAY_OF_MONTH));

        // prevent showing keyboard
        startDateEditText.setInputType(InputType.TYPE_NULL);
        endDateEditText.setInputType(InputType.TYPE_NULL);

        startDateEditText.setText("Start date");
        endDateEditText.setText("End date");

        // register from edit text listener
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EvalActivity", "onclick gefeuert");

                startDatePicker.show();
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EvalActivity", "onclick gefeuert");

                endDatePicker.show();
            }
        });
    }

    private void openDatePicker(int year, int month, int day, EditText dateEditText) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        dateEditText.setText(simpleDateFormat.format(date.getTime()));
    }

    /**
     * TODO: diese Methode muss anwendungsspezifisch überschrieben werden.
     * Sie liefert eine Liste von Zahlen zurück, die im Kuchendiagramm angezeigt werden.
     * Die Zahlen werden in einem Entry-Objekt gespeichert: es enthält die darzustellende
     * Zahl sowie einen eindeutigen Index. Der Index dient zur Festlegung der Reihenfolge
     * der Entries
     * In der Zeitfresser-Anwendung entsprechen die Zahlen den Task-Dauern,
     * die im Kuchendiagramm angezeigt werden sollen
     */
    private ArrayList<Entry> getEntries() {
        // creating data values

        ArrayList<Entry> entries = new ArrayList<>();

        for (Task task : TaskDatabaseInMemoryMock.TASK_LIST.getAllTasksWithRecords()) {
            entries.add(new Entry(task.getOverallDuration(), (int) task.getId()));
        }

        return entries;
    }

    /**
     * TODO: diese Methode muss anwendungsspezifisch überschrieben werden.
     * Sie liefert eine Liste von Labels zurück, mit denen die Zahlen aus der Methode "getEntries"
     * im Kuchendiagramm beschriftet werden. Die Reihenfolge der Labels muss zu der Reihenfolge
     * der Entries passen
     */
    private ArrayList<String> getLabels() {
        // creating labels

        ArrayList<String> labels = new ArrayList<>();

        for (Task task : TaskDatabaseInMemoryMock.TASK_LIST.getAllTasksWithRecords()) {
            labels.add(task.getName());
        }

        return labels;
    }
}
