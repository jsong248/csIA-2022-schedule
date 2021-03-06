package com.example.schedulegenerator.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.usage.ConfigurationStats;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.schedulegenerator.Model.Project;
import com.example.schedulegenerator.Model.Request;
import com.example.schedulegenerator.Model.User;
import com.example.schedulegenerator.R;
import com.example.schedulegenerator.RequestRecycler.RequestAdapter;
import com.example.schedulegenerator.Utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The profile of a project: the information for a project will all be contained here
 */
public class ProjectProfileActivity extends AppCompatActivity {

    private TextView name, status, capacity, open, sendRq;

    private Button request, editInfo, seeCollab;

    private RecyclerView recycler;

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private ArrayList<Request> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_profile);
        name = findViewById(R.id.Nameans);
        status = findViewById(R.id.StatusAns);
        capacity = findViewById(R.id.CapacityAns);
        open = findViewById(R.id.OpenAns);
        request = findViewById(R.id.requestBtn);
        editInfo = findViewById(R.id.editBtn);
        sendRq = findViewById(R.id.textViewRq);
        recycler = findViewById(R.id.RequestRecycler);
        seeCollab = findViewById(R.id.SeeCollab);

        Intent i = getIntent();
        name.setText(i.getStringExtra(Constants.NAME));
        status.setText(i.getStringExtra(Constants.STATUS));
        capacity.setText(String.valueOf(i.getStringExtra(Constants.SIZE)));
        Boolean openOrNot = i.getBooleanExtra(Constants.OPEN, false);
        //checks if the project is open or not
        if (openOrNot)
        {
            open.setText(Constants.PUBLIC);
        }
        else
        {
            open.setText(Constants.PRIVATE);
        }

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mData = new ArrayList<Request>();

        setUpTheButtonsAndTexts();
        seeAllRequests();
    }

    /**
     * Setting textviews and recyclerviews and buttons for the activity
     */
    private void setUpTheButtonsAndTexts()
    {
        mStore.collection(Constants.USER).document(mAuth.getUid()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            User currUser = task.getResult().toObject(User.class);
                            ArrayList<String> currList = currUser.getProjectList();
                            String currProjectID = getIntent().getStringExtra(Constants.ID);
                            if (!currList.contains(currProjectID))
                            {
                                editInfo.setVisibility(View.GONE);
                                recycler.setVisibility(View.GONE);
                            }
                            else
                            {
                                request.setVisibility(View.GONE);
                                sendRq.setVisibility(View.GONE);
                            }

                        }
                    }
                });
        mStore.collection(Constants.PROJECT).document(getIntent().getStringExtra(Constants.ID))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    Project currProject = task.getResult().toObject(Project.class);
                    if (!currProject.getOwner().equals(mAuth.getUid()))
                    {
                        seeCollab.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    /**
     * Redirect to the collabactivity class
     * @param v of the see collab btn
     */
    public void seeCollab(View v)
    {
        Intent i = new Intent(this, CollabActivity.class);
        i.putExtra(Constants.ID, getIntent().getStringExtra(Constants.ID));
        startActivity(i);
        finish();
    }

    /**
     * Redirect to requestprojectactivity
     * @param v of button redirection
     */
    public void sendRequest(View v)
    {
        Intent i = getIntent();
        Intent request = new Intent(this, RequestProjectActivity.class);
        request.putExtra(Constants.ID, i.getStringExtra(Constants.ID));
        startActivity(request);
        finish();
    }

    /**
     * See all the requests for the current project
     */
    public void seeAllRequests()
    {
        mStore.collection(Constants.RQ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot eachDoc : task.getResult())
                    {
                        if (!eachDoc.toObject(Request.class).isChecked())
                        {
                            Request eachRq = eachDoc.toObject(Request.class);
                            mData.add(eachRq);
                        }
                    }
                    RequestAdapter adapter = new RequestAdapter(mData, getBaseContext());
                    recycler.setAdapter(adapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                }
            }
        });
    }

    /**
     * Add the project information as intent
     * @param v of edit button
     */
    public void editTheInfo(View v)
    {
        Intent editProject = new Intent(this, EditProjectActivity.class);
        Intent i = getIntent();
        editProject.putExtra(Constants.NAME, i.getStringExtra(Constants.NAME));
        editProject.putExtra(Constants.ID, i.getStringExtra(Constants.ID));
        editProject.putExtra(Constants.SIZE, String.valueOf(i.getStringExtra(Constants.SIZE)));
        editProject.putExtra(Constants.STATUS, i.getStringExtra(Constants.STATUS));
        startActivity(editProject);
    }


}