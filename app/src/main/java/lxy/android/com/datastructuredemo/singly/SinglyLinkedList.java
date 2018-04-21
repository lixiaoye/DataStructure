package lxy.android.com.datastructuredemo.singly;


import android.support.annotation.NonNull;

/**
 * Created by LIXIAOYE on 2018/4/20.
 */


public class SinglyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;

    public void add(T element) {
        Node<T> node = new Node<T>();
        node.setValue(element);
        System.out.println("Adding: " + element);
        /**
         * check if the list is empty
         */
        if (head == null) {
            //since there is only one element,both head and
            //tail points to the same object.
            head = node;
            tail = node;
        } else {
            //set current tail next link to new node.
            tail.setNextRef(node);
            //set tail as newly created node
            tail = node;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addAfter(T element, T after) {
        Node<T> tmp = head;
        Node<T> refNode = null;
        System.out.println("Traversing to all nodes...");
        /**
         * Traverse till given element.
         */
        while (true) {
            if (tmp == null) {
                break;
            }
            if (tmp.compareTo(after) == 0) {
                refNode = tmp;
                break;
            }
            tmp = tmp.getNextRef();
        }
        if (refNode != null) {
            //add element after the target node
            Node<T> node = new Node<>();
            node.setValue(element);
            node.setNextRef(tmp.getNextRef());
            if (tmp == tail) {
                tail = node;
            }
            tmp.setNextRef(node);
        } else {
            System.out.println("Unable to find the given element...");
        }
    }

    public void deleteFront() {//删除头结点
        if (head == null) {
            System.out.println("Underflow...");
            return;//notes：必须添加

        }
        Node<T> tmp = head;
        head = tmp.getNextRef();
        if (head == null) {
            tail = null;
        }
        System.out.println("Deleted:" + tmp.getValue());
    }

    public void deleteAfter(T after) {
        Node<T> tmp = head;
        Node<T> refNode = null;
        System.out.println("Traversing to all nodes...");
        /**
         * Traverse till given element.
         */
        while (true) {
            if (tmp == null) {
                break;
            }
            if (tmp.compareTo(after) == 0) {
                //fount the target node,add after this node
                refNode = tmp;
                break;
            }
            tmp = tmp.getNextRef();
        }
        if (refNode != null) {
            tmp = refNode.getNextRef();
            refNode.setNextRef(tmp.getNextRef());
            if (refNode.getNextRef() == null) {
                tail = refNode;
            }
            System.out.println("Deleted:" + tmp.getValue());
        } else {
            System.out.println("Unable to find the given element...");
        }
    }

    public void traverse() {
        Node<T> tmp = head;
        while (true) {
            if (tmp == null) {
                break;
            }
            System.out.println(tmp.getValue());
            tmp = tmp.getNextRef();
        }
    }

    public static void main(String[] args) {
//        listInteger();
//        listString();
        reverseList();

    }

    private static void reverseList() {
        SinglyLinkedList<Integer> singlyLinkedList = new SinglyLinkedList<>();
        singlyLinkedList.add(1);
        singlyLinkedList.add(2);
        singlyLinkedList.add(3);
        singlyLinkedList.add(4);
        singlyLinkedList.add(5);
        singlyLinkedList.traverse();

    }

    public void insertNode() {

    }

    private static void listString() {
        SinglyLinkedList<String> singlyLinkedList = new SinglyLinkedList<>();
        singlyLinkedList.add("a");
        singlyLinkedList.add("b");
        singlyLinkedList.add("c");
        singlyLinkedList.add("d");
        singlyLinkedList.add("e");
        singlyLinkedList.add("f");
        singlyLinkedList.addAfter("c", "c");
        singlyLinkedList.deleteFront();
        singlyLinkedList.deleteAfter("e");
        singlyLinkedList.traverse();
        /**
         * 打印结果：
         * Adding: a
         Adding: b
         Adding: c
         Adding: d
         Adding: e
         Adding: f
         Traversing to all nodes...
         Deleted:a
         Traversing to all nodes...
         Deleted:f
         b
         c
         c
         d
         e
         */}

    private static void listInteger() {
        SinglyLinkedList<Integer> singlyLinkedList = new SinglyLinkedList<>();
        singlyLinkedList.add(1);
        singlyLinkedList.add(2);
        singlyLinkedList.add(3);
        singlyLinkedList.add(4);
        singlyLinkedList.add(5);
        singlyLinkedList.add(6);
        singlyLinkedList.addAfter(3, 3);
        singlyLinkedList.deleteFront();
        singlyLinkedList.deleteAfter(5);
        singlyLinkedList.traverse();
        /**
         * 打印结果：
         * Adding: 1
         Adding: 2
         Adding: 3
         Adding: 4
         Adding: 5
         Adding: 6
         Traversing to all nodes...
         Deleted:1
         Traversing to all nodes...
         Deleted:6
         2
         3
         3
         4
         5
         */
    }

    /**
     * 思路：从头到尾遍历原链表，每遍历一个节点，将其摘下放在新链表的最前端
     * 时间复杂度为O(n)
     *
     * @param head
     * @return
     */
    public Node reverse(Node head) {
        if (head == null || head.nextRef == null) {
            return head;
        }
        Node current = head;//当前节点
        Node next = null;
        Node reverseHead = null;
        while (current != null) {
            next = current.nextRef;//暂存当前节点的下一个节点
            current.nextRef = reverseHead;//将当前节点的下一个节点指向新链表的头结点
            reverseHead = current;//
            current = next;//操作结束后，current节点后移
        }
        return reverseHead;
    }

    private class Node<T> implements Comparable<T> {
        T value;
        Node nextRef;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNextRef() {
            return nextRef;
        }

        public void setNextRef(Node nextRef) {
            this.nextRef = nextRef;
        }

        @Override
        public int compareTo(@NonNull T o) {
            if (o == this.value) {
                return 0;
            }
            return 1;
        }
    }
}