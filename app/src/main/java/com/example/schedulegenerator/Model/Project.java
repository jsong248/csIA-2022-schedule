package com.example.schedulegenerator.Model;

import java.util.ArrayList;

public class Project {
    private ArrayList<String> collaborators;



    private boolean open;
    private String projectID;
    private int capacity;
    private String status;
    private String name;



    public Project() {
        collaborators = new ArrayList<String>();
        open = false;
        projectID = "";
        capacity = 0;
        status = "default";
        name = "default";
    }

    public Project(ArrayList<String> collaborators, boolean open, String projectID,
                   int capacity, String status, String name) {
        this.collaborators = collaborators;
        this.open = open;
        this.projectID = projectID;
        this.capacity = capacity;
        this.status = status;
        this.name = name;
    }

    public ArrayList<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(ArrayList<String> collaborators) {
        this.collaborators = collaborators;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}