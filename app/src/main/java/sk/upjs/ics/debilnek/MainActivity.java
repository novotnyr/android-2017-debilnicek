package sk.upjs.ics.debilnek;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Handles task-related UI.
 * <ul>
 *     <li><b>ListView:</b> shows a list of tasks that need to be done</li>
 *     <li><b>FloatingActionButton:</b> allows adding of new a task. A dialog will
 *     be shown with a custom EditText
 *     </li>
 * </ul>
 * <p>
 *     The data are saved into <code>SharedPreferences</code>. The tasks are organized
 *     in a very simple way: <i>keys</i> of the SharedPreferences contain
 *     the task name. The mapped boolean indicates a completion status.
 * </p>
 * <p>
 *     An example: <pre>Implement an Android app -> false, Have a coffee -> true
 *     </pre>
 * </p>
 */
public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCE_NAME = "Debilnicek2017";

    public static final boolean STATUS_INCOMPLETE = false;
    public static final boolean STATUS_DONE = true;

    private FloatingActionButton fabButton;

    private ListView taskListView;

    private ArrayAdapter<String> taskListViewAdapter;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        this.fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
        this.fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabButtonOnClick(view);
            }
        });

        this.taskListViewAdapter = createTaskListViewAdapter();
        refreshListViewAdapter();

        this.taskListView = (ListView) findViewById(R.id.taskListView);
        this.taskListView.setAdapter(this.taskListViewAdapter);
        this.taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listViewOnItemClickListener(adapterView, view, i, l);
            }
        });
    }

    /**
     * Creates a list view adapter that handles strike-thru of completed tasks.
     * <p>
     *     Array adapter is implemented over <code>simple_list_item1</code>
     *     layouts in the list view items.
     * </p>
     * <p>
     *     The layout customization is done via overriding <code>getView()</code>
     *     method and the customization of paint flags.
     * </p>
     * @return an array adapter
     */
    @NonNull
    private ArrayAdapter<String> createTaskListViewAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (isDone(textView.getText().toString())) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                return textView;
            }
        };
    }

    /**
     * Marks the selected task as a completed.
     * @param adapterView the {@link ListView} of the tasks
     * @param view the view that holds the selected item
     * @param position 0-based position of the item in the adapter
     * @param id not used
     */
    protected void listViewOnItemClickListener(AdapterView<?> adapterView, View view, int position, long id) {
        String task = this.taskListViewAdapter.getItem(position);
        saveOrUpdateTask(task, STATUS_DONE);
        refreshListViewAdapter();
    }


    /**
     * Refreshes data in the adapter of the {@link ListView}.
     */
    protected void refreshListViewAdapter() {
        Collection<String> tasks = getTasks();
        this.taskListViewAdapter.clear();
        this.taskListViewAdapter.addAll(tasks);
    }

    /**
     * Handles clicks on the Floating Action Button.
     * <p>
     *     Spawns a new dialog with a textfield. This textfield
     *     is used to get a description of a task.
     *     That task shall be saved into the shared preferences.
     * </p>
     * @param view
     */
    private void fabButtonOnClick(View view) {
        final EditText editText = new EditText(this);

        DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onDialogOkClick(editText.getText().toString());
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Insert a new task")
                .setView(editText)
                .setPositiveButton("OK", okButtonListener)
                .show();
    }

    /**
     * Handles clicks on the dialog OK button.
     * @param textFieldValue the task description
     */
    private void onDialogOkClick(String textFieldValue) {
        saveOrUpdateTask(textFieldValue, STATUS_INCOMPLETE);
        refreshListViewAdapter();
    }

    // -- Persistence methods

    /**
     * Return all tasks that are saved in the shared preferences.
     * @return collection of task names
     */
    private Collection<String> getTasks() {
        return this.sharedPreferences.getAll().keySet();
    }

    /**
     * Saves the task into the shared preferences
     * @param task task description
     * @param status task status. <code>true</code> means a completed task.
     */
    private void saveOrUpdateTask(String task, boolean status) {
        this.sharedPreferences
                .edit()
                .putBoolean(task, status)
                .apply();
    }

    /**
     * Checks if the task is done.
     * @param task task represented by the name
     * @return <code>true</code> if the task is completed
     */
    private boolean isDone(String task) {
        return this.sharedPreferences.getBoolean(task, STATUS_INCOMPLETE);
    }
}
