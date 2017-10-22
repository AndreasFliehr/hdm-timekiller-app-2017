package de.hdm.dp.bd.chronophage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdm.dp.bd.chronophage.database.TaskDatabaseInMemoryMock;
import de.hdm.dp.bd.chronophage.models.TaskListModel;
import de.hdm.dp.bd.chronophage.models.TaskModel;

/**
 * MainActivity ist die Aktivität, die beim Start der App bzw. beim Klick auf den Eintrag
 * "Data input" im Drawer-Menü aufgerufen wird
 */
public class MainActivity extends CommonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create TaskList
        TaskDatabaseInMemoryMock.taskListModel.createTaskList();

        /**
         * Nach dem Start der App wird die Ansicht angezeigt, die in der Ressource
         * activity_main.xml definiert ist. In dieser Anwendung ist es die MainActivity.
         */
        setContentView(R.layout.activity_main);

        /**
         * Hier wird das Drawer-Menü aufgebaut
         * Dieser Aufruf muss im Rahmen der Vorlesung nicht geändert werden
         */
        createNavigationBars();


        /**
         * Hier wird die Liste der Tätigkeiten aufgebaut. listview ist die Referenz auf das
         * Listenelement in der GUI, das die Namen der Tätigkeiten anzeigt
         */
        final ListView listview = (ListView) findViewById(R.id.listView);

        /**
         * Aufruf der Methode getListElements(), welche die Inhalte der dargestellten Liste (listview)
         * liefert
         * Für die Inhalte der ArrayList können Sie später Ihre Klasse Task statt String benutzen.
         * Dafür muss jedoch in der Klasse Task die Methode toString() anwendungs-
         * spezifisch implementiert sein und diejenigen Informationen über eine Task zurückliefern,
         * die dem Benutzer angezeigt werdne sollen, z.B. den Task-Namen.
         * TODO: hier ggf. den Rückgabetyp ändern - abhängig davon, wie Sie die Methode getListElements
         * implementieren
         */
        final TaskListModel list = getListElements();

        /**
         * Der Adapter bildet die Elememnte aus der Liste "list" auf Einträge des Listen-Widgets (listview)
         * in der GUI ab
         */
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list.getAllTasks());
        listview.setAdapter(adapter);

        /**
         * Hier ist der Listener definiert, der auf Benutzerklicks reagiert
         */
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                /**
                 * Abfrage, welches Listenelement vom benutzer geklickt wurde. "item" enthält den String
                 * (oder später ggf. die Task), der im angeklickten
                 * im Listenelement dargestellt wird
                 * TODO: hier ggf. den Typ von String auf Task ändern
                 */
                final TaskModel task = (TaskModel) parent.getItemAtPosition(position);
                toggleTask(task);
            }

        });
    }

    private void toggleTask(TaskModel task) {
        String toastMessage;
        if (task.isActive()) {
            // task active, stop it and set Toast-message
            task.stop();
            toastMessage = task.getName() + " stopped.";
        } else {
            // task not active, start it and set Toast-message
            task.start();
            toastMessage = task.getName() + " started.";
        }

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), toastMessage, duration);
        toast.show();
    }

    /**
     * TODO: diese Methode muss anwendungsspezifisch implementiert werden. Momentan liefert sie
     * hard kodierte Namen von Tätigkeiten. Im ersten Schritt der Anwendung sollen diese Namen aus
     * weiteren Objekten, z.B. Instanzen einer Klasse Task, ausgelesen und hier zu einer ArrayList
     * zusammengebaut werden
     * Sie können hier den Rückgabetyp auch in eine ArrayList<Task> umwandeln, wenn Sie in der Klasse
     * Task die Methode toString() so implementieren, dass sie den anzuzeigenden Task-Namen
     * zurückliefert.
     * In dem Fall müssen auch die Zeilen
     * 50 (momentan: final ArrayList<String> list = getListElements();) und
     * 74 (momentan: final String item = (String) parent.getItemAtPosition(position);)
     * ebenfalls geändert werden
     */
    private TaskListModel getListElements() {
        return TaskDatabaseInMemoryMock.taskListModel;
    }
}
