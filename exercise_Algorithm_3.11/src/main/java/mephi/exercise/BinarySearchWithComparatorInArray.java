package mephi.exercise;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class BinarySearchWithComparatorInArray {

    public static void main(String[] args) {
        Person[] people = new Person[3];
        people[0] = new Person("Alice", 30);
        people[1] = new Person("Charlie", 35);
        people[2] = new Person("Bob", 25);
        // Сортировка списка по имени
        Arrays.sort(people, new NameComparator());
        // Объект для поиска
        Person searchPerson = new Person("Bob", 0); // Возраст не имеет значения для сравнения по имени
        // Выполнение бинарного поиска
        int index = MyArrays.binarySearch(people, searchPerson, new NameComparator());
        if (index >= 0) {
            System.out.println("Person found at index: " + index);
            System.out.println("Found person: " + people[index].getName());
        } else {
            System.out.println("Person not found");
        }
    }
}

class NameComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getName().compareTo(p2.getName());
    }
}

@AllArgsConstructor
class Person {
    @Getter
    private String name;
    private int age;
}