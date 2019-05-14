package com.SmallTalk.model.Location;

import java.util.List;

public class Building extends Location {

  public String name;
  public int maxCapacity;
  private List<Floor> floors;
}

class Floor {

  private int maxCapacity;
  private List sections;
}
