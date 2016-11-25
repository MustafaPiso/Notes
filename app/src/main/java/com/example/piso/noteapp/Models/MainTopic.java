package com.example.piso.noteapp.Models;

import java.util.List;

/**
 * Created by Piso on 22/11/2016.
 */
public class MainTopic {
    String name ;
    int number_of_notes ;
    List<String> listOfnotes ;

    public int getNumber_of_notes() {
        return number_of_notes;
    }

    public void setNumber_of_notes(int number_of_notes) {
        this.number_of_notes = number_of_notes;
    }

    public List<String> getListOfnotes() {
        return listOfnotes;
    }

    public void setListOfnotes(List<String> listOfnotes) {
        this.listOfnotes = listOfnotes;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
