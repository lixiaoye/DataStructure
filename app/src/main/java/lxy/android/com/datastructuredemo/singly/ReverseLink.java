package lxy.android.com.datastructuredemo.singly;

import java.net.PortUnreachableException;

/**
 * Created by LIXIAOYE on 2018/4/20.
 */

public class ReverseLink {
    static Node head;

    /**
     * 遍历法：实现反转
     * 思路：从头到尾遍历原链表，每遍历一个节点，将其摘下放在新链表的最前端
     * 时间复杂度为O(n)。
     *
     * @param head
     * @return
     */
    Node reverseNode(Node head) {
        Node current = head;
        Node reverseHead = null;
        Node next = null;
        while (current != null) {
            next = current.next;//暂存当前节点的下一个节点
            current.next = reverseHead;//将当前节点的下一个节点指向新链表的头节点
            reverseHead = current;//将当前节点指向新链表的头结点
            current = next;//操作结束后，current后移
        }
        return reverseHead;
    }

    /**
     * 尾部递归翻转链表
     *
     * @param curr
     * @param prev 初始化为null
     * @return
     */
    Node recursiveReverse(Node curr, Node prev) {
        if (curr.next == null) {//if last node mark it head
            head = curr;
            //update next to pre node
            curr.next = prev;
            return null;
        }
        //save curr.next node for recursive call
        Node next1 = curr.next;
        //and update next..
        curr.next = prev;
        recursiveReverse(next1, curr);
        return head;
    }

    /**
     * 打印结果：
     * Original Singly Linked List
     * 6 7 8 9 10
     * <p>
     * recursive reverse linked list
     * 10 9 8 7 6
     * Process finished with exit code 0
     *
     *
     */

    /**
     * 递归翻转2：
     * @param current
     * @return
     */
    Node recursiveReverse2(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node second = current.next;
        current.next = null;
        Node reverse = recursiveReverse2(second);
        second.next = current;
        return reverse;
    }

    void printList(Node node) {
        while (node != null) {
            System.out.print(node.data + " ");
            node = node.next;
        }
    }

    public static void main(String[] args) {
//        trversalReverse();
//        recursive();
        recursive2();

    }

    private static void recursive2() {
        ReverseLink reverseLink = new ReverseLink();
        reverseLink.head = new Node(11);
        reverseLink.head.next = new Node(22);
        reverseLink.head.next.next = new Node(33);
        reverseLink.head.next.next.next = new Node(44);
        reverseLink.head.next.next.next.next = new Node(55);
        System.out.println("Original Singly Linked List");
        reverseLink.printList(reverseLink.head);
        System.out.println("");
        Node node = reverseLink.recursiveReverse2(head);
        System.out.println("");
        System.out.println("recursive reverse linked list");
        reverseLink.printList(node);
    }

    /**
     * Original Singly Linked List
     11 22 33 44 55

     recursive reverse linked list
     55 44 33 22 11
     Process finished with exit code 0
     */

    private static void recursive() {
        ReverseLink reverseLink = new ReverseLink();
        reverseLink.head = new Node(6);
        reverseLink.head.next = new Node(7);
        reverseLink.head.next.next = new Node(8);
        reverseLink.head.next.next.next = new Node(9);
        reverseLink.head.next.next.next.next = new Node(10);
        System.out.println("Original Singly Linked List");
        reverseLink.printList(reverseLink.head);
        System.out.println("");
        Node node = reverseLink.recursiveReverse(head, null);
        System.out.println("");
        System.out.println("recursive reverse linked list");
        reverseLink.printList(node);

    }


    private static void trversalReverse() {
        ReverseLink reverseLink = new ReverseLink();
        reverseLink.head = new Node(1);
        reverseLink.head.next = new Node(2);
        reverseLink.head.next.next = new Node(3);
        reverseLink.head.next.next.next = new Node(4);
        reverseLink.head.next.next.next.next = new Node(5);
        System.out.println("Given Singly Linked List");
        reverseLink.printList(reverseLink.head);
        System.out.println("");
        System.out.println("Reverse singly linked list");
        Node head = reverseLink.reverseNode(reverseLink.head);
        reverseLink.printList(head);
    }

    /**
     * 打印结果：
     * Given Singly Linked List
     * 1 2 3 4 5
     * Reverse singly linked list
     * 5 4 3 2 1
     * Process finished with exit code 0
     */

    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            next = null;
        }
    }

}
