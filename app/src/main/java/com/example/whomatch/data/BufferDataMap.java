package com.example.whomatch.data;


import com.example.whomatch.Person;
import java.util.ArrayList;
import java.util.HashMap;

public final class BufferDataMap {
    private static final BufferDataMap bufferDataMap = new BufferDataMap();
    private HashMap<String, Integer> map;
    private ArrayList<String> list = new ArrayList<>();
    private int avg;
    private int total;

    private BufferDataMap() {
    }

    public HashMap<String, Integer> getMap() {
        return map;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void addToList(String str) {
        list.add(str);
    }

    public void resetList() {
        list = new ArrayList<>();
    }

    public void removeItemFromList(int index) {
        list.remove(index);
    }

    public int getAvg() {
        return avg;
    }

    public int getTotal() {
        return total;
    }

    public void resetAllParameters() {
        map = null;
        avg = 0;
        total = 0;
    }

    public void setAllParameters() {
        HashMap<String, Integer> persons = new HashMap<>();
        for (String line : list) {
            String[] tmp = line.split(" ");
            Person person = new Person(tmp[0], Integer.parseInt(tmp[1]));
            if (persons.containsKey(person.getName())) {
                persons.put(person.getName(), persons.get(person.getName()) + person.getMoney());
            } else {
                persons.put(person.getName(), person.getMoney());
            }
        }
        map = persons;
        getMoney();
        avg = (int) Math.round((double) total / persons.size());
    }

    private void getMoney() {
        for (String s : map.keySet()) {
            total += map.get(s);
        }
    }

    public static BufferDataMap getInstance() {
        return bufferDataMap;
    }
}
