package lesson_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Box<T extends Fruit> {

    private ArrayList<T> list;

    public Box() {
        this.list = new ArrayList<>();
    }
    public Box(T... fruits) {
        this.list = new ArrayList<>(Arrays.asList(fruits));
    }

    public Box(ArrayList<T> fruit){
        this.list = fruit;
    }

    public float getWeight() {
        float weight = 0.0f;

        for (T a : list) {
            weight += a.getWeight();
        }

        return weight;
    }
    public void moveToAnotherBox(Box<T> another) {
        another.list.addAll(list);
        list.clear();
    }

    public void add(T fruit) {
        list.add(fruit);
    }
    public void add(Collection<T> fruit) {
        list.addAll(fruit);
    }

    public boolean compare(Box<?> a) {
        return Math.abs(this.getWeight() - a.getWeight()) < 0.001;
    }
}