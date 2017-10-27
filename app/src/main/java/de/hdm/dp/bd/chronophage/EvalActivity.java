package de.hdm.dp.bd.chronophage;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import de.hdm.dp.bd.chronophage.database.TaskDatabaseInMemoryMock;
import de.hdm.dp.bd.chronophage.models.TaskListModel;
import de.hdm.dp.bd.chronophage.models.TaskModel;


/**
 * EvalActivity ist die Aktivität, die beim Klick auf den Eintrag
 * "Evaluation" im Drawer-Menü aufgerufen wird
 */
public class EvalActivity extends CommonActivity {

    /**
     * diese Methode muss nicht verändert werden, sie baut das Kuchendiagramm auf
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval);
        createNavigationBars();

        //Content of the pie chart
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        PieDataSet dataset = new PieDataSet(getEntries(), "Time spent");

        //Set the data
        PieData data = new PieData(getLabels(), dataset); // initialize PieData
        pieChart.setData(data); //set data into chart

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); // set the color
        dataset.setValueTextSize(16);

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

        for (TaskModel task : TaskDatabaseInMemoryMock.taskListModel.getAllTasks()) {
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

        for (TaskModel task : TaskDatabaseInMemoryMock.taskListModel.getAllTasks()) {
            labels.add(task.getName());
        }

        return labels;
    }
}
