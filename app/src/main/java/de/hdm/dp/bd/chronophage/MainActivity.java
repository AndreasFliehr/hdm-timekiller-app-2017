package de.hdm.dp.bd.chronophage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.hdm.dp.bd.chronophage.models.TaskList;
import de.hdm.dp.bd.chronophage.models.Task;
import de.hdm.dp.bd.chronophage.models.db.DbCalls;

/**
 * MainActivity ist die Aktivität, die beim Start der App bzw. beim Klick auf den Eintrag
 * "Data input" im Drawer-Menü aufgerufen wird
 */
public class MainActivity extends CommonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        final TaskList list = getListElements();

        /**
         * Der Adapter bildet die Elememnte aus der Liste "list" auf Einträge des Listen-Widgets (listview)
         * in der GUI ab
         */
        final ArrayAdapter adapter = new ArrayAdapter(this,
            android.R.layout.simple_list_item_1, list.getAllTasks(this));
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
                final Task task = (Task) parent.getItemAtPosition(position);
                toggleTask(task);
            }

        });
    }

    private void toggleTask(Task task) {
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

    private TaskList getListElements() {
        return new TaskList(new DbCalls());
    }
}
