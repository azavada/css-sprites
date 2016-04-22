package layout;

import java.awt.*;

public class RectangularPacker {
    class Node {
        public int x = 0;
        public int y = 0;
        public int w = 0;
        public int h = 0;

        public Node right = null;
        public Node down = null;
        public boolean used = false;

        public Node(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public Node(Node node) {
            this.x = node.x;
            this.y = node.y;
            this.w = node.w;
            this.h = node.h;

            this.right = node.right;
            this.down = node.down;
            this.used = node.used;
        }
    }

    private Node root = null;

    public RectangularPacker(int initialWidth, int initialHeight) {
        this.root = new Node(0, 0, initialWidth, initialHeight);
    }

    private Node findNode(Node root, int w, int h) {
        if (root.used) {
            Node node = findNode(root.right, w, h);
            if (node == null) {
                node = findNode(root.down, w, h);
            }
            return node;

        } else if (w <= root.w && h <= root.h) {
            return root;
        }
        return null;
    }

    private Node splitNode(Node node, int w, int h) {
        node.used = true;
        node.down = new Node(node.x, node.y + h, node.w, node.h - h);
        node.right = new Node(node.x + w, node.y, node.w - w, h);
        return node;
    }

    private Node growNode(int w, int h) {
        boolean canGrowDown  = (w <= this.root.w);
        boolean canGrowRight = (h <= this.root.h);

        boolean shouldGrowRight = canGrowRight && (this.root.h >= (this.root.w + w));
        boolean shouldGrowDown  = canGrowDown  && (this.root.w >= (this.root.h + h));

        if (shouldGrowRight)
            return this.growRight(w, h);
        else if (shouldGrowDown)
            return this.growDown(w, h);
        else if (canGrowRight)
            return this.growRight(w, h);
        else if (canGrowDown)
            return this.growDown(w, h);
        else
            return null;
    }

    private Node growRight(int w, int h) {
        Node oldRoot = new Node(this.root);

        this.root.used = true;
        this.root.x = 0;
        this.root.y = 0;
        this.root.w = oldRoot.w + w;
        this.root.down = oldRoot;
        this.root.right = new Node(oldRoot.w, 0, w, oldRoot.h);

        Node node = findNode(this.root, w, h);
        if (node != null) {
            return splitNode(node, w, h);
        } else {
            return null;
        }
    }

    private Node growDown(int w, int h) {
        Node oldRoot = new Node(this.root);

        this.root.used = true;
        this.root.x = 0;
        this.root.y = 0;
        this.root.h = oldRoot.h + h;
        this.root.down = new Node(0, oldRoot.h, oldRoot.w, h);
        this.root.right = oldRoot;

        Node node = findNode(this.root, w, h);
        if (node != null) {
            return splitNode(node, w, h);
        } else {
            return null;
        }
    }

    public Point packRectangle(int width, int height) {
        Node node = findNode(this.root, width, height);

        if (node != null) {
            node = splitNode(node, width, height);
        } else {
            node = growNode(width, height);
        }

        return new Point(node.x, node.y);
    }
}
