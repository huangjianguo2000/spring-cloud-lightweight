package com.huang.lightweight.common.util.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lightweight
 * @Date 2023/5/24 15:09
 */
public class NodeObjectUtil {

    /**
     * create empty tree
     * @return
     */
    public static NodeObject createEmptyNodeObject(){
        return new NodeObject(new ArrayList<>());
    }

    /**
     * node object
     */
    public static class NodeObject implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * tree node
         */
        private List<NodeObject> children;

        public NodeObject(List<NodeObject> children) {
            this.children = children;
        }

        public List<NodeObject> getChildren() {
            return children;
        }

        public void setChildren(List<NodeObject> children) {
            this.children = children;
        }
    }
}
