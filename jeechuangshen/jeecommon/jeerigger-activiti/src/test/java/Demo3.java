import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @ClassName: Demo3
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/5/21 0021 下午 2:49
 * @version: V1.0
 */
public class Demo3 {

    public static  void main(String[] args){
        int[] a ={1,1};
        int[] b ={1,2};
        double midNum = findMedianSortedArrays(a, b);
        System.out.println(midNum);

    }

    /**
     * 最长回文查找  （2n-1）个中心
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if(s.length()<1){
            return "";
        }
        int start = 0;
        int end = 0;
        for (int i=0;i<s.length();i++){
            int len1 = expendToCenter(s,i,i);
            int len2 = expendToCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if(len>(end-start)){
                start = i-(len-1)/2;
                end = i+len/2;
            }
        }
        return s.substring(start,end+1);
    }

    private static int expendToCenter(String s, int left, int right) {
        int L = left ;
        int R =right;
        while ((L>=0)&&(R<s.length())&&(s.charAt(L)==s.charAt(R))){
            L--;
            R++;
        }
        return  R-L-1;

    }

    public static double findMedianSortedArrays(int[] nums1,int[] nums2){
        int a = nums1.length;
        int b =nums2.length;
        double result = 0.0;
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0,j=0;(i<a||j<b);){
            if((i<a)&&(j>=b||(nums1[i]<=nums2[j]))){
                list.add(nums1[i]);
                i++;
            }
            if((j<b)&&((i>=a)||(nums2[j]<nums1[i]))){
                list.add(nums2[j]);
                j++;
            }
        }
            if((list.size())%2==0){
            result=  (list.get(list.size()/2)+list.get(list.size()/2-1))/2.0;
        }else {
            result= list.get((list.size()+1)/2-1)*1.0;
        }
        return  result;
    }
}
