# siran_raft
首先此项目的目的是为了加强自己对所学知识点的一个巩固，若有不妥之处 请多多包涵！

[这是我的个人博客用来分享自己的学习笔记](http://www.sirann.cn)

## Raft(Java版本) 实现
### Raft 算法的四个部分
1. Leader Election
2. Log Replication
3. Configuration Changes
4. Log Compact

### 技术选型

- RPC通信：Raft算法所有的读写请求都是通过leader 来发送给 follower 所以这里需要实现rpc通信,可以采用dubbo、sofa-bolt。因为之前看了dubbo的源码为了加深印象这里我选择自己实现。
- Log：因为Raft算法是基于日志来实现Consensus。 采用开源的RocksDB

### 实现过程
#### 1. 整个集群中需要节点之间不断的交互，那么RPC通信首先实现，如果采用开源rpc组件可以直接略过
整个Raft算法中只会发送三种RPCs
- RequestVote(请求投票)：由候选人再选举的期间发起
- AppendEntries(附加条目)：由leader发起，用来复制日志和提供一种心跳机制
- Install Snapshot(安装快照)：由leader发起，用来与follower保持同步、压缩日志
#### 2. 日志的存储以及状态机指令的存储 
* 可以参考kafka的 log module，这个不是重点，所以将采用开源的k,v形式的存储
#### 3. 一致性(核心)
#### Leader Election：
流程：

原则：相同term，先来先服务

发送者：

服务启动至为follower状态，等待接受rpc (leader发送的心跳，candidate发送的投票)
1. 如果在这段时间内没有接受到任何rpc，那么认为整个集群中没有leader，那么term+1转换为candidate状态并且发送投票rpc竞选成为leader
2. 如果candidate 获得集群中大多数节点的认可那么成为leader
3. 如果在其他节点成为了leader那么他会接收到leader节点发送过来的心跳rpc，那么candidate转换成follower
4. 如果在同一个term下有多个candidate竞争并且获得的票数相同那么没有candidate可以成为leader，然后超时，继续发送vote请求

接收者：

遵守：
1. 如果commitIndex > lastApplied，那么就 lastApplied 加一，并把log[lastApplied]应用到状态机中
2. 如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者

接收者处理流程：
1. 如果voteRequest中的term < curTerm 直接返回false 不投票
2. 如果voteFor 字段为空或者就是voteRequest中的candidateId，并且判断这个candidate的日志是否和自己一样新 是的话就投票给他返回true 否则返回false

note：选举限制：
1. voteRequest中包含了candidate的日志信息。查看是否包含自己的日志信息，不包含则会拒绝
2. 通过比较两份日志中的最后一条日志条目的索引值和任期号定义谁的日志比较新
3. 任期号相同，日志索引较长的更新
4. 任期号不同，任期号大的更新
#### Log Replication
发送者：
1. Raft的强领导性只有leader节点可以接收客户端发过来的请求，再由leader节点，把这条消息发送给其他的follower节点。
2. 注意：只有安全的消息才能被执行。安全的消息：在leader节点把这条消息发送给其他follower节点被大部分的follower节点成功添加到本地日志的时候，这条消息才算是安全的消息。
3. 异常情况，如果有的节点宕机了或者说网络异常处理的太慢了，那么需要把之前的消息一并发给它，一致性。
4. 如果follower没有成功处理这条消息或者返回失败，leader节点会不断的重试直到成功为止
5. 如果leader发送给follower的消息，follower会根据接收到的日志的index进行比较如果太大，那么也会返回false，leader需要减小发送日志的index。
   * leader 节点上的日志不会删除或者覆盖，只会新增。故如果一个节点要成为leader，那么它所拥有的日志必须是最新的
   * 如果在不同的日志中的两个条目拥有相同的索引和任期号，那么他们存储了相同的指令。
   * 如果在不同的日志中的两个条目拥有相同的索引和任期号，那么他们之前的所有日志条目也全部相同。
   * 如果follower 与 leader 节点的日志发生冲突。那么leader 会不断减小nextIndex的值，从而与follower进行匹配，然后follower删除这些不匹配的值，从而达到一致性
   
接收者：
1. 和心跳一样，要先检查对方 term，如果 term 都不对。那么直接返回false
2. 如果日志不匹配，那么返回 leader，告诉他，减小 nextIndex 重试。基于日志的一致性检查。
3. 如果本地存在的日志和 leader 的日志冲突了，以 leader 的为准，删除自身的。
4. 最后，将日志应用到状态机，更新本地的 commitIndex，返回 leader 成功。

#### 4. 节点变更

#### 5. 日志压缩(不是重点，没有实现)

#### 6. 客户端
1. 客户端启动的时候会随机发送给一个节点，如果这个节点不是leader节点，那么这个节点会转发请求给leader节点
2. 如果这个时候leader崩溃，或者请求超时，或者在重新选举leader，那么客户端会请求失败，客户端会再次随机挑选一个服务器发送请求
3. 但是如果leader节点提交这条请求后，准备响应客户端，但是客户端崩溃了。那么客户端会重新执行这条，解决方案就是客户端对每条指令都赋予一个index。就算重复执行，服务端只要发现已经处理过这条指令了那么就不会处理直接返回
4. 但是注意这个只读操作是直接处理的不需要任何错误，那么有可能会需要脏数据的情况，因为在该leader准备响应客户端的时候，可能已经被新的leader废弃了，那么这个响应就是不准确的。所以Raft做了一些限制：
   * leader 中的被提交的信息必须是最新的，但是一个节点成为leader他并不知道当前的日志中哪些是被提交的，那么在他成为leader的时候他会发送一条没有任何操作的条目去实现。
   * leader 在响应客户端的时候，必须发送一个heartbeat来确定自己是否已经被废弃了。
#### 7. 测试用例

### 参考文献
[In Search of an Understandable Consensus Algorithm (Extended Version)](https://ramcloud.atlassian.net/wiki/download/attachments/6586375/raft.pdf)

[寻找一种易于理解的一致性算法（扩展版）](https://github.com/maemual/raft-zh_cn/blob/master/raft-zh_cn.md)

[Raft 一致性协议](https://zhuanlan.zhihu.com/p/29678067)

[莫那.鲁道博客](https://thinkinjava.cn/categories/%E5%88%86%E5%B8%83%E5%BC%8F/)

