package com.zsf.m_algorithm.binaryTree;

import com.zsf.utils.Singleton;

import java.util.ArrayList;

/**
 * @Author: zsf
 * @Date: 2020-07-28 15:15
 */
public class BinaryTreeDisplay {

    /**
     * 二叉树节点
     */
    private ArrayList<TreeNode> treeNodes = new ArrayList<>();

    TreeNode rootTree;
    TreeNode treeNode1;
    TreeNode treeNode2;
    TreeNode treeNode3;
    TreeNode treeNode4;
    TreeNode treeNode5;
    TreeNode treeNode6;
    TreeNode treeNode7;
    TreeNode treeNode8;
    TreeNode treeNode9;
    TreeNode treeNode10;
    TreeNode treeNode11;
    TreeNode treeNode12;
    TreeNode treeNode13;

    /**
     * 创建二叉树
     */
    private TreeRoot treeRoot;

    private static Singleton<BinaryTreeDisplay> binaryTreeDisplaySingleton = new Singleton<BinaryTreeDisplay>() {
        @Override
        protected BinaryTreeDisplay create() {
            return new BinaryTreeDisplay();
        }
    };

    private BinaryTreeDisplay() {
        initData();
    }

    private void initData() {

        rootTree = new TreeNode(0);
        treeNode1 = new TreeNode(1);
        treeNode2 = new TreeNode(2);
        treeNode3 = new TreeNode(3);
        treeNode4 = new TreeNode(4);
        treeNode5 = new TreeNode(5);
        treeNode6 = new TreeNode(6);
        treeNode7 = new TreeNode(7);
        treeNode8 = new TreeNode(8);
        treeNode9 = new TreeNode(9);
        treeNode10 = new TreeNode(10);
        treeNode11 = new TreeNode(11);
        treeNode12 = new TreeNode(12);
        treeNode13 = new TreeNode(13);

        rootTree.setLeftTreeNode(treeNode1);
        rootTree.setRightTreeNode(treeNode2);

        treeNode1.setLeftTreeNode(treeNode3);
        treeNode1.setRightTreeNode(treeNode4);

        treeNode2.setLeftTreeNode(treeNode5);
        treeNode2.setRightTreeNode(treeNode6);

        treeNode3.setLeftTreeNode(treeNode7);
        treeNode3.setRightTreeNode(treeNode8);

        treeNode5.setLeftTreeNode(treeNode9);
        treeNode5.setRightTreeNode(treeNode10);

        treeNode6.setRightTreeNode(treeNode13);

        treeNode10.setLeftTreeNode(treeNode11);
        treeNode10.setRightTreeNode(treeNode12);

    }

    public static BinaryTreeDisplay getInstance() {
        return binaryTreeDisplaySingleton.get();
    }

    /**
     * 前序遍历 根->左->右
     */
    public String binaryTreeBefore() {
        return binaryTreeBefore(rootTree);
    }

    private String binaryTreeBefore(TreeNode rootTree) {
        if (rootTree == null) {
            return "";
        }
        StringBuilder stringBuilderResult = new StringBuilder();
        stringBuilderResult.append(rootTree.getValue() + " ");
        stringBuilderResult.append(binaryTreeBefore(rootTree.getLeftTreeNode()) + " ");
        stringBuilderResult.append(binaryTreeBefore(rootTree.rightTreeNode) + " ");
        return stringBuilderResult.toString();
    }

    /**
     * 中序遍历 左->根->右
     * @return
     */
    public String binaryTreeIn() {
        return binaryTreeIn(rootTree);
    }

    private String binaryTreeIn(TreeNode rootTree) {
        StringBuilder stringBuilder = new StringBuilder();
        if (rootTree != null) {
            stringBuilder.append(binaryTreeIn(rootTree.getLeftTreeNode()) + " ");
            stringBuilder.append(rootTree.getValue() + " ");
            stringBuilder.append(binaryTreeIn(rootTree.getRightTreeNode()) + " ");
        }
        return stringBuilder.toString();
    }

    /**
     * 后续遍历 左->右->根
     * @return
     */
    public String binaryTreeRear() {
        return binaryTreeRear(rootTree);
    }

    public String binaryTreeRear(TreeNode rootTree) {
        StringBuilder stringBuilder = new StringBuilder();

        if (rootTree != null) {
            stringBuilder.append(binaryTreeRear(rootTree.getLeftTreeNode()) + " ");
            stringBuilder.append(binaryTreeRear(rootTree.getRightTreeNode()) + " ");
            stringBuilder.append(rootTree.getValue() + " ");
        }
        return stringBuilder.toString();
    }

    /**
     * 创建二叉树 左侧小于节点 右侧大于
     */
    public String createBinaryTree() {
        StringBuilder stringBuilder = new StringBuilder();
        treeRoot = new TreeRoot();
        int[] binaryArr = {6, 7, 3, 5, 9, 2, 13, 1, 8};
        for (int i = 0; i < binaryArr.length; i++) {
            createBinaryTree(treeRoot, binaryArr[i]);
        }
        stringBuilder.append("{6, 7, 3, 5, 9, 2, 13, 1, 8} : \n");
        stringBuilder.append("前序遍历: " + binaryTreeBefore(treeRoot.getTreeRoot()) + "\n");
        stringBuilder.append("中序遍历: " + binaryTreeIn(treeRoot.getTreeRoot()) + "\n");
        stringBuilder.append("后序遍历: " + binaryTreeRear(treeRoot.getTreeRoot()) + "\n");
        stringBuilder.append("获取最大值: " + get2TreeMax(treeRoot.getTreeRoot()) + "\n");
        stringBuilder.append("获取深度: " + get2TreeHeight(treeRoot.getTreeRoot()) + "\n");
        return stringBuilder.toString();
    }

    private void createBinaryTree(TreeRoot treeRoot, int value) {
        if (treeRoot.getTreeRoot() == null) {
            TreeNode treeNode = new TreeNode(value);
            treeRoot.setTreeRoot(treeNode);
        } else {
            // 根节点
            TreeNode tmpTreeNode = treeRoot.getTreeRoot();
            while (tmpTreeNode != null) {
                // 如果大于根节点 置右
                if (value > tmpTreeNode.getValue()) {
                    if (tmpTreeNode.getRightTreeNode() == null) {
                        // 右侧没有节点, 直接插入
                        tmpTreeNode.setRightTreeNode(new TreeNode(value));
                        return;
                    } else {
                        // 右侧有节点, 替换子节点对比
                        tmpTreeNode = tmpTreeNode.getRightTreeNode();
                    }

                } else {
                    if (tmpTreeNode.getLeftTreeNode() == null) {
                        // 左侧没有节点, 直接插入
                        tmpTreeNode.setLeftTreeNode(new TreeNode(value));
                        return;
                    } else {
                        tmpTreeNode = tmpTreeNode.getLeftTreeNode();
                    }
                }
            }
        }
    }

    /**
     * 获取二叉树最大值
     * @param treeNode
     * @return
     */
    private int get2TreeMax(TreeNode treeNode) {
        if (treeNode == null) {
            return -1;
        } else {
            // 获取左节点最大值
            int left = get2TreeMax(treeNode.getLeftTreeNode());
            // 获取有节点最大值
            int right = get2TreeMax(treeNode.getRightTreeNode());
            // 当前节点值
            int current = treeNode.getValue();

            int max = left;
            if (right > max){
                max = right;
            }
            if (current > max) {
                max = current;
            }
            return max;
        }
    }

    /**
     * 获取二叉树深度
     * @param treeNode
     * @return
     */
    private int get2TreeHeight(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        } else {
            // 获取左侧节点深度
            int left = get2TreeHeight(treeNode.getLeftTreeNode());
            // 获取右侧节点深度
            int right = get2TreeHeight(treeNode.getRightTreeNode());
            int max = left;
            if (right > max) {
                max = right;
            }
            return max + 1;
        }
    }

    /**
     * 获取节点个数
     * @return
     */
    public int getNodeCount() {
        return getNodeCount(treeRoot.getTreeRoot());
    }
    private int getNodeCount(TreeNode treeNode) {
        if (treeNode == null) {
            return 0;
        }
        return getNodeCount(treeNode.getLeftTreeNode()) + getNodeCount(treeNode.getRightTreeNode()) + 1;
    }


    /**
     * 二叉树节点
     */
    private class TreeNode {

        /**
         * 左节点
         */
        private TreeNode leftTreeNode;

        /**
         * 右节点
         */
        private TreeNode rightTreeNode;

        /**
         * 节点值
         */
        private int value;

        public TreeNode(int value) {
            this.value = value;
        }

        public TreeNode getLeftTreeNode() {
            return leftTreeNode;
        }

        public void setLeftTreeNode(TreeNode leftTreeNode) {
            this.leftTreeNode = leftTreeNode;
        }

        public TreeNode getRightTreeNode() {
            return rightTreeNode;
        }

        public void setRightTreeNode(TreeNode rightTreeNode) {
            this.rightTreeNode = rightTreeNode;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 二叉树根
     */
    private class TreeRoot {

        private TreeNode treeNode;

        public TreeNode getTreeRoot() {
            return treeNode;
        }

        public void setTreeRoot(TreeNode treeNode) {
            this.treeNode = treeNode;
        }
    }

}
