package de.hdm.dp.bd.chronophage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.hdm.dp.bd.chronophage.models.Task;
import de.hdm.dp.bd.chronophage.models.TaskList;
import de.hdm.dp.bd.chronophage.models.db.TaskListProviderDbImpl;

public class MainActivity extends CommonActivity {
    private TaskList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNavigationBars();

        final ListView listview = (ListView) findViewById(R.id.listView);
        list = getTaskList();
        final ArrayAdapter adapter = new ArrayAdapter(this,
            android.R.layout.simple_list_item_1, list.getAllTasks());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final Task task = (Task) parent.getItemAtPosition(position);
                toggleTask(task);
            }

        });
    }

    private void toggleTask(Task task) {
        String toastMessage;
        if (list.isTaskActive(task)) {
            // task active, stop it and set Toast-message
            list.setTaskInactive(task);
            toastMessage = task.getName() + " stopped.";
        } else {
            // task not active, start it and set Toast-message
            list.setTaskActive(task);
            toastMessage = task.getName() + " started.";
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), toastMessage, duration);
        toast.show();
    }

    private TaskList getTaskList() {
        return new TaskList(new TaskListProviderDbImpl(this));
    }
}
