import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @ClassName: Demo1
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/5/20 0020 下午 6:16
 * @version: V1.0
 */
public class Demo1 {

    public static  void main(String[] args){
        ListNode node1 = new ListNode(5);
        ListNode node4 = new ListNode(5);

//        String s = sumNode(node1, node4, "", false);
        //        ListNode node1 = new ListNode(9);
//        ListNode node4 = new ListNode(0);

        ListNode listNode2 = addTwoNumbers(node1, node4);
        System.out.println(listNode2);

    }
    //l1 "1,2,3,9"=9321        l2 "4,5,6"=654  l1+l2=9321+654=9975   flag=false;
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode headNode = new ListNode(0);
        ListNode curr = headNode;
        ListNode p = l1;
        ListNode q =l2;
        int flag=0;
        while (p!=null || q!=null){
            int x = p==null?0:p.val;
            int y = q==null?0:q.val;
            int sum = x+y+flag;
            flag = sum/10;
            curr.next=new ListNode(sum%10);
            curr=curr.next;
            if(null !=p){
                p=p.next;
            }
            if(q!=null){
                q=q.next;
            }
        }
        if(flag>0){
            curr.next=new ListNode(flag);
        }
        return headNode.next;
    }

    private static String add(String s1, String s2) {
       if(s1.length()>=s2.length()){

       }else {

       }

        return null;
    }

    public static  ListNode saveNode(ListNode k,String s,int i){
        if(i++>=s.length()){
            k=new ListNode(Integer.valueOf(String.valueOf(s.charAt(0))));
           return k;
        }
         k = saveNode(k, s, i);
        if(i<=1){
            return k;
        }
        if(null !=k){
            i--;
            ListNode listNode = new ListNode(Integer.valueOf(String.valueOf(s.charAt(s.length()-i))));
            listNode.next=k;
            return  listNode;
        }
        return k;
    }

    public static String getNode(ListNode l,String s){
        while(null ==l){
            return s;
        }
        String a = getNode(l.next, s);
        if(l.val>=0){
            a=a+l.val;
        }
        return a;
    }
    //l1 "1,2,3,9"=9321        l2 "4,5,6"=654  l1+l2=9321+654=9975   flag=false;
    public static String sumNode(ListNode l1,ListNode l2,String s,boolean flag){
        if (null == l1&&null == l2){
            return "";
        }
        int a =(l1==null?0:l1.val)+(l2==null?0:l2.val);
        if(flag){
            a++;
        }
        if(a>9){
            s = String.valueOf(String.valueOf(a).charAt(1));
            flag=true;
        }else {
            s=String.valueOf(a);
        }
         String aa = sumNode(l1==null?null:l1.next, l2==null?null:l2.next, s,flag);
        if(null !=s){
            aa=aa+s;
        }
        return aa;
    }
}
