package trees;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static trees.BinaryTreeBuilder.buildTree;

/**
 * This class performs different traversals on a Binary Tree:
 * 1. Inorder Traversal
 * 2. Preorder Traversal
 * 3. Postorder Traversal
 * 4. Level Order Traversal
 */
public class Traversal {

    // Stores inorder traversal result
    private final List<Integer> inOrderTraversalList;

    // Stores preorder traversal result
    private final List<Integer> preOrderTraversalList;

    // Stores postorder traversal result
    private final List<Integer> postOrderTraversalList;

    // Stores level order traversal result
    private final List<Integer> levelOrderTraversalList;

    /**
     * Constructor initializes all traversal lists
     */
    public Traversal() {
        this.inOrderTraversalList = new ArrayList<>();
        this.preOrderTraversalList = new ArrayList<>();
        this.postOrderTraversalList = new ArrayList<>();
        this.levelOrderTraversalList = new ArrayList<>();
    }

    /**
     * Inorder Traversal (Left → Root → Right)
     *
     * @param root current node of the binary tree
     */
    public void inOrderTraversal(BinaryTreeBuilder.BinaryTreeNode root) {
        // Base condition: if node is null, return
        if (root == null) {
            return;
        }

        // Traverse left subtree
        inOrderTraversal(root.left);

        // Visit root node
        inOrderTraversalList.add(root.val);

        // Traverse right subtree
        inOrderTraversal(root.right);
    }

    /**
     * Preorder Traversal (Root → Left → Right)
     *
     * @param root current node of the binary tree
     */
    public void preOrderTraversal(BinaryTreeBuilder.BinaryTreeNode root) {
        // Base condition
        if (root == null) {
            return;
        }

        // Visit root node first
        preOrderTraversalList.add(root.val);

        // Traverse left subtree
        preOrderTraversal(root.left);

        // Traverse right subtree
        preOrderTraversal(root.right);
    }

    /**
     * Postorder Traversal (Left → Right → Root)
     *
     * @param root current node of the binary tree
     */
    public void postOrderTraversal(BinaryTreeBuilder.BinaryTreeNode root) {
        // Base condition
        if (root == null) {
            return;
        }

        // Traverse left subtree
        postOrderTraversal(root.left);

        // Traverse right subtree
        postOrderTraversal(root.right);

        // Visit root node at the end
        postOrderTraversalList.add(root.val);
    }

    /**
     * Level Order Traversal (Breadth-First Search)
     * Uses a Queue to traverse the tree level by level
     *
     * @param root root node of the binary tree
     */
    public List<Integer> levelOrderTraversal(BinaryTreeBuilder.BinaryTreeNode root) {

        // Queue to store nodes of each level
        Queue<BinaryTreeBuilder.BinaryTreeNode> levelOrderTraversalQueue =
                new ArrayDeque<>();

        // Add root node to queue
        levelOrderTraversalQueue.offer(root);

        // Process nodes until queue becomes empty
        while (!levelOrderTraversalQueue.isEmpty()) {

            // Remove front node from queue
            BinaryTreeBuilder.BinaryTreeNode curr =
                    levelOrderTraversalQueue.poll();

            // Visit current node
            levelOrderTraversalList.add(curr.val);

            // Add left child if it exists
            if (curr.left != null) {
                levelOrderTraversalQueue.offer(curr.left);
            }

            // Add right child if it exists
            if (curr.right != null) {
                levelOrderTraversalQueue.offer(curr.right);
            }
        }
        return levelOrderTraversalList;
    }

    public void clearAllLists() {
        this.levelOrderTraversalList.clear();
        this.preOrderTraversalList.clear();
        this.postOrderTraversalList.clear();
        this.inOrderTraversalList.clear();
    }

    /**
     * Main method – program execution starts here
     */
    public static void main(String[] args) {

        // Build binary tree from array
        BinaryTreeBuilder.BinaryTreeNode root =
                buildTree(new Integer[]{1, 2, 3, null, 4, 5, null});

        /*
        Example Tree used for traversal:

        Input Array: {1, 2, 3, null, 4, 5, null}

        Tree Structure:
            1
           / \
          2   3
           \  /
            4 5
        */


        // Create traversal object
        Traversal traversal = new Traversal();

        // Perform all traversals
        traversal.inOrderTraversal(root);
        traversal.preOrderTraversal(root);
        traversal.postOrderTraversal(root);
        traversal.levelOrderTraversal(root);

        // Print traversal results
        System.out.print("Inorder traversal of tree is : ");
        printTraversal(traversal.inOrderTraversalList);

        System.out.print("Preorder traversal of tree is : ");
        printTraversal(traversal.preOrderTraversalList);

        System.out.print("Postorder traversal of tree is : ");
        printTraversal(traversal.postOrderTraversalList);

        System.out.print("Levelorder traversal of tree is : ");
        printTraversal(traversal.levelOrderTraversalList);
    }

    /**
     * Utility method to print traversal list
     *
     * @param traversal list containing traversal order
     */
    static void printTraversal(List<Integer> traversal) {
        for (int i : traversal) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
