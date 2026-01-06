package trees;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * InvertTree
 *
 * This class demonstrates how to invert (mirror) a binary tree
 * using both DFS (recursive) and BFS (iterative) approaches.
 *
 * ============================================================
 * ORIGINAL TREE (Level Order Input):
 * {1, 2, 3, null, 4, 5, null}
 *
 * Tree Structure BEFORE inversion:
 *
 *                 1
 *                / \
 *               2   3
 *                \  /
 *                 4 5
 *
 * ============================================================
 */
public class InvertTree {

    /**
     * Invert Binary Tree using DFS (Recursive)
     *
     * DFS Visualization:
     *
     * Step 1: Swap children at node 1
     *
     *                 1
     *                / \
     *               3   2
     *                  /
     *                 4
     *
     * Step 2: Recurse on left (3)
     *
     *                 1
     *                / \
     *               3   2
     *                \  /
     *                 5 4
     *
     * Step 3: Recurse on right (2)
     *
     * Final Inverted Tree:
     *
     *                 1
     *                / \
     *               3   2
     *                \  /
     *                 5 4
     *
     * Time Complexity: O(n)
     * Space Complexity: O(h) (recursion stack)
     */
    public static void invertTreeUsingDFS(BinaryTreeBuilder.BinaryTreeNode root) {

        // Base case: null node
        if (root == null) {
            return;
        }

        // Swap left and right children
        BinaryTreeBuilder.BinaryTreeNode temp = root.left;
        root.left = root.right;
        root.right = temp;

        // Recursively invert left subtree
        invertTreeUsingDFS(root.left);

        // Recursively invert right subtree
        invertTreeUsingDFS(root.right);
    }

    /**
     * Invert Binary Tree using BFS (Level Order)
     *
     * BFS Visualization:
     *
     * Queue initially contains:
     * [1]
     *
     * Process 1 → swap children → enqueue 3, 2
     * Queue: [3, 2]
     *
     * Process 3 → swap children → enqueue 5
     * Queue: [2, 5]
     *
     * Process 2 → swap children → enqueue 4
     * Queue: [5, 4]
     *
     * Process 5, 4 → leaf nodes
     *
     * Final Inverted Tree:
     *
     *                 1
     *                / \
     *               3   2
     *                \  /
     *                 5 4
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    private static void invertTreeUsingBFS(BinaryTreeBuilder.BinaryTreeNode root) {

        Queue<BinaryTreeBuilder.BinaryTreeNode> queue = new ArrayDeque<>();

        // Start BFS from root
        queue.offer(root);

        while (!queue.isEmpty()) {

            BinaryTreeBuilder.BinaryTreeNode curr = queue.poll();

            // Swap children
            BinaryTreeBuilder.BinaryTreeNode temp = curr.left;
            curr.left = curr.right;
            curr.right = temp;

            // Add children to queue
            if (curr.left != null) {
                queue.offer(curr.left);
            }

            if (curr.right != null) {
                queue.offer(curr.right);
            }
        }
    }

    /**
     * Main method to demonstrate inversion
     */
    public static void main(String[] args) {

        /*
         Build the tree from array:

         Input: {1, 2, 3, null, 4, 5, null}

         Tree:
                 1
                / \
               2   3
                \  /
                 4 5
         */

        BinaryTreeBuilder.BinaryTreeNode root =
                BinaryTreeBuilder.buildTree(new Integer[]{1, 2, 3, null, 4, 5, null});

        Traversal traversal = new Traversal();

        // Level order before inversion
        System.out.println("Before Inversion (Level Order): "
                + traversal.levelOrderTraversal(root));

        traversal.clearAllLists();

        // Invert using DFS
        invertTreeUsingDFS(root);

        System.out.println("After DFS Inversion (Level Order): "
                + traversal.levelOrderTraversal(root));

        traversal.clearAllLists();

        // Invert again using BFS (restores original tree)
        invertTreeUsingBFS(root);

        System.out.println("After BFS Inversion (Level Order): "
                + traversal.levelOrderTraversal(root));
    }
}
