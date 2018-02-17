package de.hdm.dp.bd.chronophage;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import de.hdm.dp.bd.chronophage.models.Task;
import de.hdm.dp.bd.chronophage.models.TaskList;
import de.hdm.dp.bd.chronophage.models.db.TaskListProviderDbImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EvalActivity extends CommonActivity {
    public static final String START_DATE_DEFAULT_TEXT = "Start date";
    public static final String END_DATE_DEFAULT_TEXT = "End date";
    private EditText startDateEditText;
    private EditText endDateEditText;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private PieChart pieChart;
    public static final int[] OUR_COLORS = {
        Color.rgb(193, 37, 82), Color.rgb(255, 102, 0),
        Color.rgb(245, 199, 0), Color.rgb(106, 150, 31),
        Color.rgb(179, 100, 53), Color.rgb(66, 155, 244)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eval);
        createNavigationBars();

        // Date filter
        startDateEditText = (EditText) findViewById(R.id.startDateEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);

        // Content of the pie chart
        pieChart = (PieChart) findViewById(R.id.chart);

        PieDataSet dataset = new PieDataSet(getEntries(), "Time spent");

        //Set the data
        PieData data = new PieData(getLabels(), dataset); // initialize PieData
        pieChart.setData(data); //set data into chart

        dataset.setColors(OUR_COLORS); // set the color
        dataset.setValueTextSize(16);

        setupDatePicker();
    }


    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();

        // implement the date picker dialog
        final DatePickerDialog startDatePicker = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    if (beforeEnd(year, month, day)) {
                        openDatePicker(year, month, day, startDateEditText);
                        updatePieChart();
                    } else {
                        final Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "Start Date must be before end!",
                            Toast.LENGTH_LONG
                        );
                        toast.show();
                    }
                }
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
            Calendar.DAY_OF_MONTH)
        );

        final DatePickerDialog endDatePicker = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    if (afterStart(year, month, day)) {
                        openDatePicker(year, month, day, endDateEditText);
                        updatePieChart();
                    } else {
                        final Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "End Date must be after start!",
                            Toast.LENGTH_LONG
                        );
                        toast.show();
                    }
                }
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar
            .DAY_OF_MONTH)
        );

        // prevent showing keyboard
        startDateEditText.setInputType(InputType.TYPE_NULL);
        endDateEditText.setInputType(InputType.TYPE_NULL);

        startDateEditText.setText(START_DATE_DEFAULT_TEXT);
        endDateEditText.setText(END_DATE_DEFAULT_TEXT);

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

    private boolean afterStart(int year, int month, int day) {
        try {
            final Date start = dateFormat.parse(startDateEditText.getText().toString());
            Calendar date = Calendar.getInstance();
            date.set(year, month, day);
            Date end = date.getTime();
            return end.after(start);
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(),
                "Parsing date from startDateText failed " + e
            );
            return true;
        }
    }

    private boolean beforeEnd(int year, int month, int day) {
        try {
            final Date end = dateFormat.parse(endDateEditText.getText().toString());
            Calendar date = Calendar.getInstance();
            date.set(year, month, day);
            Date start = date.getTime();
            return start.before(end);
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(),
                "Parsing date from startDateText failed " + e
            );
            return true;
        }
    }

    private void openDatePicker(int year, int month, int day, EditText dateEditText) {
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        dateEditText.setText(dateFormat.format(date.getTime()));
    }

    private void updatePieChart() {
        PieDataSet dataset = new PieDataSet(getEntries(), "Time spent");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(16);
        PieData data = new PieData(getLabels(), dataset);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();
        for (Task task : getTasksInFilter()) {
            entries.add(new Entry(task.getOverallDuration(), (int) task.getId()));
        }
        return entries;
    }

    private List<Task> getTasksInFilter() {
        TaskList taskList = getTaskList();
        final String startDateString = startDateEditText.getText().toString();
        if (!startDateString.equals(START_DATE_DEFAULT_TEXT)) {
            try {
                Date startDate = dateFormat.parse(startDateString);
                taskList = taskList.getFilteredTasksWithRecordsAfter(startDate);
            } catch (ParseException e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
        }
        final String endDateString = endDateEditText.getText().toString();
        if (!endDateString.equals(END_DATE_DEFAULT_TEXT)) {
            try {
                Date endDate = dateFormat.parse(endDateString);
                taskList = taskList.getFilteredTasksWithRecordsBefore(endDate);
            } catch (ParseException e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
        }
        return taskList.getAllTasksWithRecords();
    }

    @NonNull
    private TaskList getTaskList() {
        return new TaskList(new TaskListProviderDbImpl(this));
    }

    private ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        for (Task task : getTasksInFilter()) {
            labels.add(task.getName());
        }
        return labels;
    }
}
