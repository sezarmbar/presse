package tmp.company;


public class Nodes {

//  private ListNode head ;

  private static class ListNode {
    private int data;
    private ListNode next;

    public ListNode(int data) {
      this.data = data;
      this.next = null;
    }
  }

  public static void prindNodeList(ListNode head) {
    if (head == null) {
      System.out.println("No Elements to Print...");
    }
    ListNode current = head;
    while (current != null) {
      System.out.print(current.data + " --> ");
      current = current.next;
    }
    System.out.println(current);
  }


  public static int lengthNodeList(ListNode head) {
    ListNode current = head;
    int cou = 0;
    while (current != null) {
      cou++;
      current = current.next;
    }
    return cou;
  }

  public static ListNode addToFirst(ListNode head, int value) {
    ListNode newListNode = new ListNode(value);
    if(head == null){
      return newListNode;
    }
    newListNode.next = head;
    return newListNode;
  }

  public static ListNode addToEnd(ListNode head, int value){
    ListNode newListNode = new ListNode(value);
    if(head == null){
      head = newListNode;
      return head;
    }
    ListNode curr = head;
    while (curr.next!=null) {
      curr = curr.next;
    }
        curr.next = newListNode;
        return head;
  }

  public static ListNode addToIndex(ListNode head, int index, int value){
    ListNode newListNode = new ListNode(value);
    ListNode tmp = head;
    int count = 0;
    while (count != index-1){
      count++;
      if(tmp.next == null)
      {
        System.out.println("out of range ...");
        return null;
      }
      tmp = tmp.next;

    }
    newListNode.next = tmp.next;
    tmp.next = newListNode;

    return head;
  }



  public static void insertAfterNode(ListNode node, int value){
    ListNode newListNode = new ListNode(value);

    if(node == null){
      System.out.println("node can't be null");
    }

    newListNode.next = node.next;
    node.next = newListNode;

  }

  public static ListNode deleteFirstNode(ListNode head){
    if(head == null){
      System.out.println("head node can't be a null ...");
      return null;
    }
    ListNode newHead = head.next;
    head = null;
    return newHead;
  }
  public static ListNode deleteLastNode(ListNode head){
    ListNode curr = head;
    ListNode prev = null;
    if(head == null){
      System.out.println("head node can't be a null ...");
      return null;
    }
    while (curr.next != null){
      prev = curr;
      curr = curr.next;
    }
    curr = null;
    prev.next = null;
    return head;
  }

  public static ListNode deleteNode(ListNode head, int index){
    ListNode current = head;
    ListNode prev = null;

    int count = 0;
    while (count != index-1){
      count++;
      if(current.next == null)
      {
        System.out.println("out of range ...");
        return null;
      }
      prev = current;
      current = current.next;

    }

    prev.next = current.next;
    current = null;

    return head;
  }

  public static ListNode findeNode(ListNode head,int data ){
    ListNode curr= head;
    while (curr != null){
      if (curr.data == data) return curr;
      curr = curr.next;
    }

    return null;
  }

  public static ListNode reverce(ListNode head){
    ListNode next = null;
    ListNode curr = head;
    ListNode pre = null;
    while (curr != null){
      next = curr.next;
      curr.next = pre;
      pre = curr;
      curr = next;
    }
    return pre;
  }

  public static ListNode findMiddleNode(ListNode head){
    ListNode fasNode  = head, slowNode = head;

    while (fasNode != null && fasNode.next != null){
      fasNode = fasNode.next.next;
      slowNode = slowNode.next;
    }
    return slowNode;
  }

  public static ListNode findNthNode(ListNode head, int n){

    ListNode tmp = head;
    ListNode main = head;
    int plce=0, counter=0;
    while (counter < n){
      tmp = tmp.next;
      counter++;
    }

    while (tmp !=null){
      tmp = tmp.next;
      main = main.next;
    }
    return main;
  }

  public static void removeDupplicates(ListNode head){
    ListNode current = head;

    while (current != null && current.next != null){
      if(current.data == current.next.data){
        current.next = current.next.next;
      }else {
        current = current.next;
      }
    }

  }

//  public static void insertInSortedNodeList(ListNode head, ListNode node){
//
//    ListNode current = head;
//    int counter=0;
//    boolean
//    while (current != null){
//      counter++;
//      if(node.data == current.data){ addToIndex(head,current,node.data); }
//      if(node.data > current.data && node.data < current.next.data )
//      current = current.next;
//
//    }
//
//  }
  public static void main(String[] args) {

    ListNode head = new ListNode(1);
    ListNode second = new ListNode(1);
    ListNode third = new ListNode(2);
    ListNode fourth = new ListNode(3);
    ListNode fivth = new ListNode(4);
    ListNode sexth = new ListNode(4);

    head.next = second;
    second.next = third;
    third.next = fourth;
    fourth.next = fivth;
    fivth.next = sexth;





    prindNodeList(head);
//    System.out.println(lengthNodeList(head));
//
//
    removeDupplicates(head);
    prindNodeList(head);

//   head=  findNthNode(head,2);
//    System.out.println(head.data);
//    insertAfterNode(second,345);
//    prindNodeList(head);
//
//
//    head = addToFirst(head, 22);
//
//    prindNodeList(head);
//    System.out.println(lengthNodeList(head));
//
//
//    head = addToEnd(head, 99);
//
//    prindNodeList(head);
//    System.out.println(lengthNodeList(head));
//
//    head = addToIndex(head,4,789);
//
//    prindNodeList(head);
//    System.out.println(lengthNodeList(head));

//    head = deleteFirstNode(head);
//    prindNodeList(head);

//    head = deleteLastNode(head);
//    prindNodeList(head);

//
//    head = deleteNode(head,2);
//    prindNodeList(head);



  }





}















