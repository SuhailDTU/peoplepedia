package model;

import java.util.ArrayList;
import java.util.Objects;

public class Categories {
    private String Catagoryname;
    private ArrayList<Person> people = new ArrayList<Person>();



    public Categories(String name){
        this.Catagoryname = name;


    }

    public String getCatagoryname() {
        return Catagoryname;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categories category = (Categories) o;
        return Objects.equals(Catagoryname, category.Catagoryname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Catagoryname, people);
    }

    @Override
    public String toString() {
        return "Categories{" +
                "Catagoryname='" + Catagoryname + '\'' +
                ", people=" + people +
                '}';
    }
}
