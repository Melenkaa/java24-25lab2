package org.example;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.LinkedList;

public class CustomTreeSet<E> implements Tree<E> {
    private Leaf<E> root;
    private int size = 0;
    private List<E> list;

    public CustomTreeSet(){
        root = new Leaf<>(null);
        list = new LinkedList<>();
    }

    @Override
    public boolean add(E e)
    {
        if(size == 0){
            return initRootNode(e);
        }

        Leaf<E> newNode = new Leaf<>(e);
        Leaf<E> lastNode = findLastLeaf(root, newNode);
        if(lastNode == null){
            return false;
        }
        size++;
        newNode.parent = lastNode;

        if(lastNode.compareTo(newNode) < 0){
            lastNode.right = newNode;
            return true;
        }else{
            lastNode.left = newNode;
            return true;
        }


    }

    @Override
    public boolean containsAll(TreeSet<E> e){
        for (E element : e){
            if(!contains(element)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean remove(E e){
        Leaf<E> eLeaf = new Leaf<>(e);
        Leaf<E> nodeToRemove = search(root, eLeaf);

        if(nodeToRemove == null)
            return false;
        removeNode(nodeToRemove);
        size--;
        return true;
    }

    private void removeNode(Leaf<E> node) {
        if(node.left == null && node.right == null){
            if(node.parent != null){
                if(node.parent.left == node)
                    node.parent.left = null;
                else
                    node.parent.right = null;
            }else{
                root = null;
            }
        }else if(node.left != null && node.right != null){
            Leaf<E> successor = findSuccesseor(node);
            node.element = successor.element;
            removeNode(successor);
        }else {
            Leaf<E> child = (node.left != null) ? node.left : node.right;
            child.parent = node.parent;

            if(node.parent != null){
                if(node.parent.left == node)
                    node.parent.left = child;
                else
                    node.parent.right = child;
            }else{
                root = child;
            }
        }

    }

    private Leaf<E> findSuccesseor(Leaf<E> node) {
        Leaf<E> successor = node.right;
        while(successor.left != null){
            successor = successor.left;
        }
        return successor;
    }

    @Override
    public List<E> get() {

        if(size == 0)
            return null;

        Iterator<E> iterator = iterator();
        while (iterator.hasNext()) {
            E nextElement = iterator.next();
            list.add(nextElement);
        }
        return list;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear(){
        root = null;
        size = 0;
    }

    @Override
    public E first(){
        if(size == 0){
            return null;
        }

        Leaf<E> current = root;
        while(current.left != null){
            current = current.left;
        }
        return current.element;
    }

    @Override
    public E last(){
        if(size == 0){
            return null;
        }

        Leaf<E> current = root;
        while(current.right != null){
            current = current.right;
        }
        return current.element;
    }

    @Override
    public boolean contains(E e) {
        Leaf<E> eLeaf = new Leaf<>(e);
        Leaf<E> find = search(root, eLeaf);
        if(find != null){
            return true;
        }else
            return false;
    }

    private Leaf<E> search(Leaf<E> leaf, Leaf<E> eLeaf){
        int compare = leaf.compareTo(eLeaf);

        if(compare < 0 && leaf.right != null){
            return search(leaf.right, eLeaf);
        }
        if(compare > 0 && leaf.left != null){
            return search(leaf.left, eLeaf);
        }
        if (compare == 0) {
            return leaf;
        }
        return null;
    }

    private boolean initRootNode(E e)
    {
        root.element = e;
        size ++;
        return true;
    }
    private Leaf<E> findLastLeaf(final Leaf<E> oldLeaf, final Leaf<E> newLeaf)
    {

        Leaf<E> lastLeaf = oldLeaf;
        int compare = oldLeaf.compareTo(newLeaf);
        if(compare < 0 && oldLeaf.right != null){
            lastLeaf = findLastLeaf(oldLeaf.right, newLeaf);
            return lastLeaf;
        }
        if(compare > 0 && oldLeaf.left != null){
            lastLeaf = findLastLeaf(oldLeaf.left, newLeaf);
            return lastLeaf;
        }
        if (compare == 0)
        {
            return null;
        }
        return lastLeaf;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int count = 0;
            final Iterator<Leaf<E>> iterator = new TreeIterator<>(root);
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                count++;
                return iterator.next().element;
            }
        };
    }

    private class TreeIterator<E> implements Iterator<Leaf<E>> {
        private Leaf<E> next;
        private TreeIterator(Leaf<E> root){
            next = root;
            goToLeftMost();
        }
        private void  goToLeftMost(){
            while(next.left != null)
                next = next.left;
        }
        @Override
        public boolean hasNext() {
            return next != null;
        }


        @Override
        public Leaf<E> next(){
            Leaf<E> current = next;
            if (next.right != null) {
                return goRight(current);
            }
            return goUp(current);
        }

        private Leaf<E> goRight(Leaf<E> right){
            next = next.right;
            while(next.left != null){
                next = next.left;
            }
            return right;
        }
        private Leaf<E> goUp(Leaf<E> obj){
            while(true){
                if(next.parent == null){
                    next = null;
                    return obj;
                }
                if(next.parent.left == next){
                    next = next.parent;
                    return obj;
                }
                next = next.parent;
            }
        }
    }

    class Leaf<E> implements Comparable<E>{
        private Leaf<E> parent;
        private Leaf<E> right;
        private Leaf<E> left;
        private E element;
        private Leaf(E element){
            this.element = element;
        }

        public E getElement(){
            return element;
        }

        @Override
        public int compareTo(Object obj){
            Leaf<E> node = (Leaf<E>) obj;
            return this.hashCode() - node.hashCode();
        }

        @Override
        public int hashCode(){
            int hash = 31;
            hash = hash * 17 + element.hashCode();
            return hash;
            //
        }

    }
}
