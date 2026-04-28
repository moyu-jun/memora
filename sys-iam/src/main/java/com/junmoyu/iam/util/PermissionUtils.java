package com.junmoyu.iam.util;

import com.junmoyu.iam.model.converter.PermissionConverter;
import com.junmoyu.iam.model.entity.PermissionEntity;
import com.junmoyu.iam.model.response.PermissionTreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * PermissionUtils
 */
public class PermissionUtils {

    public static List<PermissionTreeNode> buildTree(List<PermissionEntity> permissions) {
        // 1. 实体映射为树节点
        List<PermissionTreeNode> nodes = permissions.stream()
                .map(PermissionConverter.INSTANCE::toTreeNode)
                .toList();

        // 2. 构建 id -> node 的映射，方便快速查找父节点
        Map<Long, PermissionTreeNode> nodeMap = nodes.stream()
                .collect(Collectors.toMap(PermissionTreeNode::getId, Function.identity()));

        List<PermissionTreeNode> roots = new ArrayList<>();
        for (PermissionTreeNode node : nodes) {
            Long parentId = node.getParentId();
            if (parentId == null || parentId == 0L) {
                // 顶层节点
                roots.add(node);
            } else {
                PermissionTreeNode parent = nodeMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 父节点缺失（脏数据），可按需作为顶层或丢弃，这里作为顶层
                    roots.add(node);
                }
            }
        }

        // 3. 可选：对根节点和所有 children 排序
        sortTree(roots);
        return roots;
    }

    /**
     * 按 sort 字段递归排序
     */
    public static void sortTree(List<PermissionTreeNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return;
        }
        nodes.sort(Comparator.comparing(PermissionTreeNode::getSort,
                Comparator.nullsLast(Comparator.naturalOrder())));
        nodes.forEach(node -> sortTree(node.getChildren()));
    }
}
