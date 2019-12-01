package edu.emory.cs.queue;

class List
{
    int  value;
    List next;
}

public class Test1
{
    /* =====================================================
       InsertList(h,e): write the algorithm in Java first !

       Compile with:  javac HelpList.java
       Run with:      java HelpList

       Correct output:
             List = 30
             List = 10 30
             List = 10 30 50
             List = 10 20 30 50
             List = 10 20 30 40 50
       ===================================================== */
    public static List InsertList(List head, List elem)    {
        elem.next = null;

        if ( head == null )
    {
        head = elem;
        return head;
    }
        else {
            if (head.value < elem.value) {
                head.next = InsertList(head.next, elem);
                //return head;
            }
            else {//ele<head
                List temp = head;
                head.value = elem.value;
                head.next=temp;
                return head;
            }
        }
        return head;
    }
    public static void main(String[] args)
    {
        int[] v = {30, 50, 10, 40, 20};
        List head = null;
        List elem;

        for ( int i = 0; i < v.length; i++ )
        {
            elem = new List();

            elem.value = v[i];
            elem.next  = elem;     // **** Put some junk in next ****

            head = InsertList(head, elem);
            System.out.print("List = ");
            PrintList( head );
            System.out.println();
        }
    }

    public static void PrintList(List h)
    {
        if ( h == null )
            return;
        else
        {
            System.out.print(h.value + " ");
            PrintList(h.next);
        }
    }
}

