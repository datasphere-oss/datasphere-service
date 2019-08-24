/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.shaker.processor.model;

public class BinaryTree {
    public int data;           //根节点数据
    public BinaryTree left;    //左子树
    public BinaryTree right;   //右子树

    public BinaryTree(int data) { //实例化二叉树类
        this.data = data;
        left = null;
        right = null;
    }

    public void insert(BinaryTree root,int data) { //向二叉树中插入子节点
        if(data > root.data) {// 二叉树的左节点都比根节点小
            if(root.right == null) {
                root.right = new BinaryTree(data);
            } else {
                this.insert(root.right, data);
            }
        } else {// 二叉树的右节点都比根节点大
            if(root.left == null) {
                root.left = new BinaryTree(data);
            } else {
                this.insert(root.left, data);
            }
        }
    }
}
