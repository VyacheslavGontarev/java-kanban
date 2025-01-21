public class Node {

    public Task data;
    public Integer next;
    public Integer prev;

    public Node(Task data) {
        this.data = data;
        this.next = -1;
        this.prev = -1;
    }



    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public Integer getPrev() {
        return prev;
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }
}
