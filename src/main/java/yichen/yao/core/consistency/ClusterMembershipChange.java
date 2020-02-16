package yichen.yao.core.consistency;

/**
 * @Author: siran.yao
 * @time: 2020/2/15:下午1:32
 */
public interface ClusterMembershipChange {

    /**
     * 新增节点
     * @param node
     */
    void addServerToCluster(Node node);

    /**
     * 删除节点
     * @param node
     */
    void removeServerFromCluster(Node node);

}
