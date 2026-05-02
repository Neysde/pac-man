import java.awt.*;

public class Queue<T> {

    private T[] list;
    private int front;
    private int rear;
    private int size;

    private final int capacity=10000;

    Queue(){
        front=0;
        rear=0;
        size=0;

        list = (T[]) new Object[capacity];
    }

    public void enqueue(T item){
        list[rear]=item;
        rear=(rear+1)%capacity; //to prevent out of bound error it comes back to start if index becomes more than array last index.
        size+=1;
    }

    public T dequeue(){
        T tempItem = list[front];
        front=(front+1)%capacity;
        size-=1;
        return tempItem;
    }

    public boolean isEmpty(){
        if (size==0){
            return true;
        }
        return false;
    }

    public int size(){
        return size;
    }






}
