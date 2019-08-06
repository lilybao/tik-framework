import java.util.LinkedList;

/**
 * @ClassName: BSTIterator
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/5/17 0017 下午 3:05
 * @version: V1.0
 */
public class BSTIterator {
    public LinkedList<TreeNode> queue =  new LinkedList<TreeNode>();

    public  BSTIterator(TreeNode root){
        save(root);
    }
    public void save(TreeNode root){
        if(null == root){
            return;
        }
        save(root.left);
        if(null != root){
            queue.add(root);
        }
        save(root.right);
    }

    public int next(){
        return queue.poll().val;
    }
    public boolean hasnext(){
        if(!queue.isEmpty()){
            return  true;
        }
        return false;
    }
}
