package io.github.keep2iron;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    class ListNode {
        int val;

        public ListNode(int x) {
            this.val = x;
        }

        public ListNode next;
    }

    @Test
    public void addition_isCorrect() throws Exception {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);

        node1.next = node2;
        node2.next = node3;

        ListNode node = node1;
        System.out.print("{");
        while (node != null) {
            node = node.next;
            if(node != null){
                System.out.print(node.val);

                if(node.next != null){
                    System.out.print("->");
                }
            }
        }
        System.out.print("}");

    }
}