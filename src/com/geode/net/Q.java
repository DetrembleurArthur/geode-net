package com.geode.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Q implements Serializable
{
    private String type;
    private Category category;
    private ArrayList<Serializable> args;

    public enum Category
    {
        NORMAL,
        DISCOVERY
    }

    public static final Object SUCCESS = new Object();
    public static final Object FAILED = new Object();

    public static Q simple(String type)
    {
        return new Q(type);
    }

    public static Q success(String type)
    {
        return new Q(type + "_success");
    }

    public static Q failed(String type)
    {
        return new Q(type + "_failed");
    }

    public Q()
    {
        this("q");
    }

    public Q(String type)
    {
        this(type, new ArrayList<>());
    }

    public Q(String type, Serializable ... args)
    {
        this.args = (ArrayList<Serializable>) Arrays.asList(args);
        setType(type);
        category = Category.NORMAL;
    }

    public Q(String type, ArrayList<Serializable> args)
    {
        this.args = args;
        setType(type);
        category = Category.NORMAL;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type.toLowerCase();
    }

    public ArrayList<Serializable> getArgs()
    {
        return args;
    }

    public Serializable[] getArgsArray()
    {
        return (Serializable[]) args.toArray();
    }

    public void setArgs(ArrayList<Serializable> args)
    {
        this.args = args;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    @Override
    public String toString()
    {
        return "Q [" + type + "::" + args + "]";
    }
}