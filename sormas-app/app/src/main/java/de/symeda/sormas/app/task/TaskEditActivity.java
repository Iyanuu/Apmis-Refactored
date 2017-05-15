package de.symeda.sormas.app.task;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.app.R;
import de.symeda.sormas.app.SormasApplication;
import de.symeda.sormas.app.backend.common.DaoException;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.task.Task;
import de.symeda.sormas.app.backend.task.TaskDao;
import de.symeda.sormas.app.component.UserReportDialog;
import de.symeda.sormas.app.util.ConnectionHelper;
import de.symeda.sormas.app.util.ErrorReportingHelper;
import de.symeda.sormas.app.util.SyncCallback;


/**
 * Created by Stefan Szczesny on 26.10.2016.
 */
public class TaskEditActivity extends AppCompatActivity {

    private TaskForm taskForm;

    private String parentCaseUuid;
    private String parentContactUuid;
    private String parentEventUuid;

    private Tracker tracker;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.sormas_root_activity_layout);
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getText(R.string.headline_task) + " - " + ConfigProvider.getUser().getUserRole().toShortString());
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.containsKey(TasksListFragment.KEY_CASE_UUID)) {
                parentCaseUuid = (String) extras.get(TasksListFragment.KEY_CASE_UUID);
            }
            if (extras.containsKey(TasksListFragment.KEY_CONTACT_UUID)) {
                parentContactUuid = (String) extras.get(TasksListFragment.KEY_CONTACT_UUID);
            }
            if (extras.containsKey(TasksListFragment.KEY_EVENT_UUID)) {
                parentEventUuid = (String) extras.get(TasksListFragment.KEY_EVENT_UUID);
            }
        }

        // setting the fragment_frame
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        taskForm = new TaskForm();
        taskForm.setArguments(getIntent().getExtras());
        ft.add(R.id.fragment_frame, taskForm).commit();

        SormasApplication application = (SormasApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskForm.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_action_help,false);
        menu.setGroupVisible(R.id.group_action_add,false);
        menu.setGroupVisible(R.id.group_action_save,true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (parentCaseUuid != null || parentContactUuid != null || parentEventUuid != null) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    // TODO check parent activity intent as soon as the minimum API level has been increased to 16
                    Intent intent = new Intent(this, TasksActivity.class);
                    startActivity(intent);
                }

                return true;

            // Report problem button
            case R.id.action_report:
                Task task = (Task) taskForm.getData();

                UserReportDialog userReportDialog = new UserReportDialog(this, this.getClass().getSimpleName(), task.getUuid());
                AlertDialog dialog = userReportDialog.create();
                dialog.show();

                return true;

            case R.id.action_save:
                task = (Task) taskForm.getData();
                try {
                    TaskDao taskDao = DatabaseHelper.getTaskDao();
                    taskDao.save(task);
                    Snackbar.make(findViewById(R.id.fragment_frame), "Task " + DataHelper.getShortUuid(task.getUuid()) + " saved", Snackbar.LENGTH_LONG).show();

                    if (ConnectionHelper.isConnectedToInternet(getApplicationContext())) {
                        SyncTasksTask.syncTasksWithProgressDialog(this, new SyncCallback() {
                            @Override
                            public void call(boolean syncFailed) {
                                onResume();
                                if (syncFailed) {
                                    Snackbar.make(findViewById(R.id.fragment_frame), String.format(getResources().getString(R.string.snackbar_sync_error_saved), getResources().getString(R.string.entity_task)), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }, null);
                    }
                } catch (DaoException e) {
                    Log.e(getClass().getName(), "Error while trying to save task", e);
                    Snackbar.make(findViewById(R.id.fragment_frame), "Task could not be saved because of an internal error.", Snackbar.LENGTH_LONG).show();
                    ErrorReportingHelper.sendCaughtException(tracker, e, task, true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
