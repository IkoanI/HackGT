package com.example.hackgt.View;

public class DoublyLinkedListNode<T> {

    private T data;
    private DoublyLinkedListNode<T> previous;
    private DoublyLinkedListNode<T> next;

    DoublyLinkedListNode(T data, DoublyLinkedListNode<T> previous, DoublyLinkedListNode<T> next) {
        this.data = data;
        this.previous = previous;
        this.next = next;
    }

    DoublyLinkedListNode(T data) {
        this(data, null, null);
    }

    T getData() {
        return data;
    }

    DoublyLinkedListNode<T> getPrevious() {
        return previous;
    }

    DoublyLinkedListNode<T> getNext() {
        return next;
    }

    void setPrevious(DoublyLinkedListNode<T> previous) {
        this.previous = previous;
    }

    void setNext(DoublyLinkedListNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node containing: " + data;
    }
}