import java.awt.*;

public class Queue<T> {

    private T[] list; // stores queue elements
    private int front; //front pointer
    private int rear; // rear pointer
    private int size; // element count in the list

    private final int capacity=10000;

    Queue(){
        // by default because there is no element inside yet, 0
        front=0;
        rear=0;
        size=0;

        list = (T[]) new Object[capacity]; // declare a list with very high capacity
    }

    // adds element from rear to fulfill fifo
    public void enqueue(T item){
        list[rear]=item;
        // increments the rear pointer by 1 each time when a new element added to point the available index properly
        rear=(rear+1)%capacity; //to prevent out of bound error it comes back to start if index becomes more than array last index.
        size+=1; // increases size
    }

    // removes(returns) element from front to fulfill fifo
    public T dequeue(){
        T tempItem = list[front];
        // increments the front pointer by 1 each time when a new element added to point the available index properly
        front=(front+1)%capacity; //to prevent out of bound error it comes back to start if index becomes more than array last index.
        size-=1; // decreases size
        return tempItem;
    }

    // checks whether the queue is empty or not
    public boolean isEmpty(){
        if (size==0){
            return true;
        }
        return false;
    }

    // returns the element count in the queue
    public int size(){
        return size;
    }






}
