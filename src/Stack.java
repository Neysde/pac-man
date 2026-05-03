public class Stack<T> {
    private T[] list;
    private int size;

    private final int capacity=10000;

    Stack(){
        size=0;
        list = (T[]) new Object[capacity];
    }

    public void push(T item){
        list[size]=item;
        size+=1; // move to next empty space
    }

    public T pop(){
        size-=1; // need to move from the empty space to the actual item
        return list[size];
    }

    public boolean isEmpty(){
        return size==0;
    }

}