package trees;

import java.util.ArrayDeque;
import java.util.Queue;

public class MaxDepth {

    /**
     * Computes the maximum depth of a binary tree using
     * Depth-First Search (DFS) with recursion.
     * <p>
     * Definition:
     * - Depth of an empty tree = 0
     * - Depth of a non-empty tree =
     * 1 + max(depth of left subtree, depth of right subtree)
     */
    public static int getMaxDepthOfTree(BinaryTreeBuilder.BinaryTreeNode root) {

        // Base case:
        // If the current node is null, it contributes no depth
        if (root == null) {
            return 0;
        }

        // Recursively compute depth of left and right subtrees
        int leftDepth = getMaxDepthOfTree(root.left);
        int rightDepth = getMaxDepthOfTree(root.right);

        // Current node adds 1 to the maximum of left and right depths
        return 1 + Math.max(leftDepth, rightDepth);
    }

    /**
     * Computes the maximum depth of a binary tree using
     * Breadth-First Search (BFS), also known as level-order traversal.
     * <p>
     * Idea:
     * - Traverse the tree level by level
     * - Each iteration of the while-loop processes one full level
     * - The number of levels processed equals the maximum depth
     */
    public static int getMaxDepthUsingBfs(BinaryTreeBuilder.BinaryTreeNode root) {

        // Queue to store nodes for BFS traversal
        Queue<BinaryTreeBuilder.BinaryTreeNode> bfs = new ArrayDeque<>();

        // Start BFS from the root node
        bfs.offer(root);

        int level = 0;

        // Continue until all levels are processed
        while (!bfs.isEmpty()) {

            // Number of nodes at the current level
            int size = bfs.size();

            // Process each node in the current level
            for (int i = 0; i < size; i++) {
                BinaryTreeBuilder.BinaryTreeNode temp = bfs.poll();

                // Add left child to queue if it exists
                if (temp.left != null) {
                    bfs.offer(temp.left);
                }

                // Add right child to queue if it exists
                if (temp.right != null) {
                    bfs.offer(temp.right);
                }
            }

            // One full level has been processed
            level++;
        }

        return level;
    }

    public static void main(String[] args) {

        /*
         * Tree built from level-order array:
         * [1, 2, 3, null, 4, 5, null, 6]
         *
         * Binary Tree Structure:
         *
         *         1
         *        / \
         *       2   3
         *        \  /
         *         4 5
         *        /
         *       6
         *
         * Longest path from root to leaf:
         * 1 → 2 → 4 → 6
         *
         * Maximum Depth = 4
         */

        BinaryTreeBuilder.BinaryTreeNode root =
                BinaryTreeBuilder.buildTree(
                        new Integer[]{1, 2, 3, null, 4, 5, null, 6});

        System.out.println("Max Depth of binary tree is "
                + getMaxDepthOfTree(root));

        System.out.println("Max Depth using bfs is : "
                + getMaxDepthUsingBfs(root));
    }
}
