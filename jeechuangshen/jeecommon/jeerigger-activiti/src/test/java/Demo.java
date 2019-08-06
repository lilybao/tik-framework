import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @ClassName: Demo
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/5/17 0017 上午 11:40
 * @version: V1.0
 */
public class Demo {

   public static void main(String[] args){
//       TreeNode treeNode1 = new TreeNode(3);
//       TreeNode treeNode3 = new TreeNode(10);
//       TreeNode treeNode4 = new TreeNode(7);
//       TreeNode treeNode5 = new TreeNode(15);
//       TreeNode treeNode = new TreeNode(5);
//       treeNode.left=treeNode1;
//       treeNode.right=treeNode3;
//       treeNode3.left=treeNode4;
//       treeNode3.right=treeNode5;
//       BSTIterator bstIterator = new BSTIterator(treeNode);
//       while(bstIterator.hasnext()){
//           System.out.println(bstIterator.next());
//           System.out.println(bstIterator.hasnext());
//       }
//       String  a  = "hello word today";
//       String s = reverseWords(a);
//       int a = lengthOfLongestSubstring("");
//       System.out.println(a);
//       String s = "abcabc";
//       int i = s.indexOf(s.charAt(3), 4);
//       System.out.println("abcabc".charAt(0));
//       System.out.println("abcacbcdaa".indexOf("a",4));
       int[] nums={1,2,3,4};
       int target = 6;
       int[] twosum = twosum(nums, target);
       System.out.println(twosum[0]);
       System.out.println(twosum[1]);
   }

    public static String reverseWords(String s) {
        ArrayList<String> list = new ArrayList<String> ();
        String[] attr =  s.split(" ");
        for(int i =0; i<attr.length;i++){
            String a =attr[i].replace(" ","");
            list.add(a);
        }
        String str = "";
        for(int i =list.size();i>0;i--){
            str=str+list.get(i-1)+" ";
        }
        return str;
    }

    public static int[] twosum(int[] nums,int target){

        int[] ss=new int[2];
        for(int i=0;i<nums.length;i++){
            for (int j = i+1; j <nums.length ; j++) {
                if(nums[i]+nums[j]==target){
                    ss[0]=i;
                    ss[1]=j;
                    break;
                }
            }
        }
        return  ss;
    }
    /**
     * 获取不重复字符串的最大长度
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s) {
//        if(s.length()==0){
//            return 0;
//        }else if(s.length()==1){
//            return 1;
//        }
        //存放字符串 abcacbcdaa
        int i =0;
        int length = 0;
        int flag = 0;
        int result = 0;
        while(i <s.length()){
            int port = s.indexOf(s.charAt(i),flag);
            if(port<i){
                if(length>result){
                    result=length;
                }
                if(result>=s.length()-port-1){
                    return  result;
                }
                length=i-port-1;
                flag=port+1;
            }
            i++;
            length++;
        }
        return length;
    }
}
