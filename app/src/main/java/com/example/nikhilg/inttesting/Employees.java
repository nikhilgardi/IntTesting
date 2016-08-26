package com.example.nikhilg.inttesting;

/**
 * Created by nikhilg on 8/26/2016.
 */
public class Employees {
    public int id;
    public String name;
    public float salary;
    public void setId(int id)
    {
        this.id=id;
    }
    public int getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setSalary(float salary)
    {
        this.salary=salary;
    }
    public float getSalary()
    {
        return salary;
    }
    @Override
    public String toString()
    {
        return "id="+id+"\nName"+name+"\nSalary"+salary;
    }
}
