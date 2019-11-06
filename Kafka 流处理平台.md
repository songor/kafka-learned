# Kafka 流处理平台

### 致谢

[Kafka 流处理平台](https://www.imooc.com/learn/1043)

### 什么是 Kafka

* [**Introduction**](http://kafka.apache.org/intro)

* A streaming platform has three key capabilities:
  * Publish and subscribe to streams of records, similar to a message queue or enterprise messaging system.
  * Store streams of records in a fault-tolerant durable way.
  * Process streams of records as they occur.

* Kafka is generally used for two broad classes of applications:
  * Building real-time streaming data pipelines that reliably get data between systems or applications.
  * Building real-time streaming applications that transform or react to the streams of data.

### Kafka 的设计和结构

* Kafka 基本概念
  * Producer
  
    Producers publish data to the topics of their choice. The producer is responsible for choosing which record to assign to which partition within the topic. This can be done in a round-robin fashion simply to balance load or it can be done according to some semantic partition function (say based on some key in the record).
  
  * Consumer
  
    Consumers label themselves with a consumer group name, and each record published to a topic is delivered to one consumer instance within each subscribing consumer group. Consumer instances can be in separate processes or on separate machines. 
  
    If all the consumer instances have the same consumer group, then the records will effectively be load balanced over the consumer instances.
  
    If all the consumer instances have different consumer groups, then each record will be broadcast to all the consumer processes. 
  
  * Consumer Group（逻辑）
  
  * Broker（物理）
  
    Kafka 集群中的每个 Kafka 节点
  
  * Topic（逻辑）
  
    A topic is a category or feed name to which records are published. Topics in Kafka are always multi-subscriber; that is, a topic can have zero, one, or many consumers that subscribe to the data written to it.
  
  * Partition（物理）
  
    Each partition is an ordered, immutable sequence of records that is continually appended to—a structured commit log. The records in the partitions are each assigned a sequential id number called the offset that uniquely identifies each record within the partition. 
  
    The Kafka cluster durably persists all published records—whether or not they have been consumed—using a configurable retention period.
  
    The partitions of the log are distributed over the servers in the Kafka cluster with each server handling data and requests for a share of the partitions. Each partition is replicated across a configurable number of servers for fault tolerance. 
  
  * Replication
  
    Each partition has one server which acts as the "leader" and zero or more servers which act as "followers". The leader handles all read and write requests for the partition while the followers passively replicate the leader. If the leader fails, one of the followers will automatically become the new leader. Each server acts as a leader for some of its partitions and a follower for others so load is well balanced within the cluster. 
  
  * Replication Leader
  
  * ReplicaManager
  
* Kafka 概念延伸

  * Partition

    每一个 Topic 被切分成多个 Partitions

    消费者数目小于或等于 Partition 的数目

    Broker Group 中的每一个 Broker 保存 Topic 的一个或多个 Partitions

    Consumer Group 中仅有一个 Consumer 读取 Topic 的一个或多个 Partitions

  * Replication

    系统默认每一个 Topic 的 Replication 为 1

    Replication 的基本单位是 Topic 的 Partition

    所有的读写都从 Leader 进，Followers 只是做备份

    Follower 必须能够及时复制 Leader 的数据

* 

### Kafka 的应用场景和实战

### Kafka 高级特性