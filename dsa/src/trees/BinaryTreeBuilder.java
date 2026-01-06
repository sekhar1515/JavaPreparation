package trees;

import java.util.*;

public class BinaryTreeBuilder {

    protected static class BinaryTreeNode {
        int val;
        BinaryTreeNode left;
        BinaryTreeNode right;

        BinaryTreeNode() {}

        BinaryTreeNode(int val) {
            this.val = val;
        }

        BinaryTreeNode(int val, BinaryTreeNode left, BinaryTreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static BinaryTreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }

        BinaryTreeNode root = new BinaryTreeNode(arr[0]);
        Queue<BinaryTreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int i = 1;

        while (i < arr.length) {
            BinaryTreeNode current = queue.poll();

            // Left child
            if (arr[i] != null) {
                current.left = new BinaryTreeNode(arr[i]);
                queue.offer(current.left);
            }
            i++;

            // Right child
            if (i < arr.length && arr[i] != null) {
                current.right = new BinaryTreeNode(arr[i]);
                queue.offer(current.right);
            }
            i++;
        }

        return root;
    }
}



