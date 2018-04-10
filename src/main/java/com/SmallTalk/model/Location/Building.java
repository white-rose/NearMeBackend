package com.SmallTalk.model.Location;

import java.util.List;

public class Building extends Location {

    public String name;
    private List<Floor> floors;
    public int maxCapacity;

}

class Floor {

    private int maxCapacity;
    private List sections;

}
