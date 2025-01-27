package org.example;

import java.util.List;
import java.util.TreeSet;

public interface Tree<E> extends Iterable{
    boolean add(E e);
    List<E> get();
    int size();
    boolean contains(E e);
    boolean remove(E e);
    void clear();
    E first();
    E last();
    boolean containsAll(TreeSet<E> e); // for unit test
}
