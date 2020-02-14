# siran_raft
## Raft(Java版本) 实现
### Raft 算法的四个部分
1. Leader Election
2. Log Replication
3. Configuration Changes
4. Log Compact

### 技术选型

- RPC通信：Raft算法所有的读写请求都是通过leader 来发送给 follower 所以这里需要实现rpc通信,可以采用dubbo、sofa-bolt。因为之前看了dubbo的源码为了加深印象这里我选择自己实现。
- Log：因为Raft算法是基于日志来实现Consensus，
- 状态机：执行的指令，

### 实现过程
#### 1. 整个集群中需要节点之间不断的交互，那么RPC通信首先实现，如果采用开源rpc组件可以直接略过 (实现中)
整个Raft算法中只会发送三种RPCs
- RequestVote(请求投票)：由候选人再选举的期间发起
- AppendEntries(附加条目)：由leader发起，用来复制日志和提供一种心跳机制
- Install Snapshot(安装快照)：由leader发起，用来与follower保持同步、压缩日志
#### 2. 日志的存储以及状态机指令的存储 (未实现)
#### 3. 一致性(核心)  (未实现)
#### 4. 日志压缩  (未实现)
#### 5. 测试用例 (未实现)

### 参考文献
[In Search of an Understandable Consensus Algorithm (Extended Version)](https://ramcloud.atlassian.net/wiki/download/attachments/6586375/raft.pdf)

[寻找一种易于理解的一致性算法（扩展版）](https://github.com/maemual/raft-zh_cn/blob/master/raft-zh_cn.md)

[莫那.鲁道博客](https://thinkinjava.cn/categories/%E5%88%86%E5%B8%83%E5%BC%8F/)

[Raft 一致性协议](https://zhuanlan.zhihu.com/p/29678067)
