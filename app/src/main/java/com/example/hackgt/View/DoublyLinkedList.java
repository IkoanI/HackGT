package com.example.hackgt.View;

import java.util.NoSuchElementException;

public class DoublyLinkedList<T> {

    private DoublyLinkedListNode<T> head;
    private DoublyLinkedListNode<T> tail;
    private int size;

    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index must be above 0 or below " + size);
        }
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        DoublyLinkedListNode<T> temp = new DoublyLinkedListNode<>(data);

        if (index == 0) {
            if (head == null) {
                this.head = temp;
                this.tail = temp;
            } else {
                this.head.setPrevious(temp);
                temp.setNext(this.head);
                this.head = temp;
            }
        } else if (index == size) {
            tail.setNext(temp);
            temp.setPrevious(this.tail);
            this.tail = temp;
        } else {
            DoublyLinkedListNode<T> temp2;
            if (index <= size / 2) {
                temp2 = this.head;
                for (int i = 1; i < index; i++) {
                    temp2 = temp2.getNext();
                }
            } else {
                temp2 = this.tail;
                for (int i = size; i > index; i--) {
                    temp2 = temp2.getPrevious();
                }
            }
            temp.setNext(temp2.getNext());
            temp.setPrevious(temp2);
            temp2.getNext().setPrevious(temp);
            temp2.setNext(temp);
        }
        size++;
    }

    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        DoublyLinkedListNode<T> temp = new DoublyLinkedListNode<>(data);

        if (head == null) {
            this.head = temp;
            this.tail = temp;
        } else {
            this.head.setPrevious(temp);
            temp.setNext(this.head);
            this.head = temp;
        }
        size++;
    }

    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        DoublyLinkedListNode<T> temp = new DoublyLinkedListNode<>(data);

        if (tail == null) {
            this.head = temp;
        } else {
            tail.setNext(temp);
            temp.setPrevious(this.tail);
        }
        this.tail = temp;
        size++;
    }

    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index cannot be less than zero,or greater or equal to " + size);
        }

        DoublyLinkedListNode<T> temp;

        if (index == 0) {
            temp = this.head;
            if (this.head.getNext() == null) {
                this.head = null;
                size--;
                return temp.getData();
            }
            this.head = this.head.getNext();
            this.head.setPrevious(null);
            size--;
            return temp.getData();
        }

        if (index == (size - 1)) {
            temp = this.tail;
            if (this.tail.getPrevious() == null) {
                this.tail = null;
                size--;
                return temp.getData();
            }
            this.tail = this.tail.getPrevious();
            this.tail.setNext(null);
            size--;
            return temp.getData();
        }

        if (index <= size / 2) {
            temp = this.head;
            for (int i = 0; i < index; i++) {
                temp = temp.getNext();
            }
            temp.getPrevious().setNext(temp.getNext());
            temp.getNext().setPrevious(temp.getPrevious());
            size--;
            return temp.getData();
        } else {
            temp = this.tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.getPrevious();
            }
            temp.getPrevious().setNext(temp.getNext());
            temp.getNext().setPrevious(temp.getPrevious());
            size--;
            return temp.getData();
        }
    }

    public T removeFromFront() {
        if (this.head == null) {
            throw new NoSuchElementException("This list is null");
        }
        return removeAtIndex(0);
    }

    public T removeFromBack() {
        if (this.head == null) {
            throw new NoSuchElementException("This list is null");
        }
        return removeAtIndex(size - 1);
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index cannot be less than 0 or greater or equal to " + size);
        }

        if (index == 0) {
            return this.head.getData();
        }

        if (index == (size - 1)) {
            return this.tail.getData();
        }

        DoublyLinkedListNode<T> temp = null;

        if (index <= size / 2) {
            temp = this.head;
            for (int i = 0; i < index; i++) {
                temp = temp.getNext();
            }
            return temp.getData();
        } else {
            temp = this.tail;
            for (int i = size - 1; i > index; i--) {
                temp = temp.getPrevious();
            }
            return temp.getData();
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (this.tail.getData().equals(data)) {
            return removeFromBack();
        }
        for (int i = size - 1; i >= 0; i--) {
            if (this.get(i).equals(data)) {
                return removeAtIndex(i);
            }
        }
        throw new NoSuchElementException("The data you inputted was not found in the list!");
    }

    public Object[] toArray() {
        T[] temp = (T[]) new Object[size];
        if (size == 0) {
            return temp;
        }

        for (int i = 0; i < size; i++) {
            temp[i] = this.get(i);
        }
        return temp;
    }

    public DoublyLinkedListNode<T> getHead() {
        return head;
    }

    public void setHead(DoublyLinkedListNode<T> x) { this.head = x; }

    public DoublyLinkedListNode<T> getTail() {
        return tail;
    }

    public int size() {
        return size;
    }
}