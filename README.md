# siran_raft
## Raft(Java版本) 实现
### Raft 算法的四个部分
1. Leader Election
2. Log Replication
3. Configuration Changes
4. Log Compact

### 技术选型

- RPC通信：Raft算法所有的读写请求都是通过leader 来发送给 follower 所以这里需要实现rpc通信,可以采用dubbo、sofa-bolt。因为之前看了dubbo的源码为了加深印象这里我选择自己实现。
- Log：因为Raft算法是基于日志来实现Consensus。
- 状态机：执行的指令。

### 实现过程
#### 1. 整个集群中需要节点之间不断的交互，那么RPC通信首先实现，如果采用开源rpc组件可以直接略过
整个Raft算法中只会发送三种RPCs
- RequestVote(请求投票)：由候选人再选举的期间发起
- AppendEntries(附加条目)：由leader发起，用来复制日志和提供一种心跳机制
- Install Snapshot(安装快照)：由leader发起，用来与follower保持同步、压缩日志
#### 2. 日志的存储以及状态机指令的存储 
#### 3. 一致性(核心)
#### Leader Election：
流程：
原则：相同term，先来先服务
1 服务启动至为follower状态，等待接受rpc (leader发送的心跳，candidate发送的投票)
1.1 如果在这段时间内没有接受到任何rpc，那么认为整个集群中没有leader，那么term+1转换为candidate状态并且发送投票rpc竞选成为leader
1.2 如果candidate 获得集群中大多数节点的认可那么成为leader
1.3 如果在其他节点成为了leader那么他会接收到leader节点发送过来的心跳rpc，那么candidate转换成follower
1.4 如果在同一个term下有多个candidate竞争并且获得的票数相同那么没有candidate可以成为leader，然后超时，继续发送vote请求
#### Log Replication
#### 4. 日志压缩
#### 5. 测试用例

### 参考文献
[In Search of an Understandable Consensus Algorithm (Extended Version)](https://ramcloud.atlassian.net/wiki/download/attachments/6586375/raft.pdf)

[寻找一种易于理解的一致性算法（扩展版）](https://github.com/maemual/raft-zh_cn/blob/master/raft-zh_cn.md)

[Raft 一致性协议](https://zhuanlan.zhihu.com/p/29678067)

[莫那.鲁道博客](https://thinkinjava.cn/categories/%E5%88%86%E5%B8%83%E5%BC%8F/)

