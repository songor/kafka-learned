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

* Kafka 基本结构

  * Producer API

    allows an application to publish a stream of records to one or more Kafka topics.

  * Consumer API

    allows an application to subscribe to one or more topics and process the stream of records produced to them.

  * Streams API

    allows an application to act as a stream processor, consuming an input stream from one or more topics and producing an output stream to one or more output topics, effectively transforming the input streams to output streams.

  * Connector API

    allows building and running reusable producers or consumers that connect Kafka topics to existing applications or data systems. For example, a connector to a relational database might capture every change to a table.

* Kafka 消息结构

  Offset（4 bytes）、Length（4 bytes）、CRC32（4 bytes）、Magic（1 bytes）、attributes（1 bytes）、Timestamp（8 bytes）、Key Length（4 bytes）、Key、Value Length（4 bytes）、Value

* Kafka 特点

  * 分布式

    多分区、多副本、多订阅者、基于 ZooKeeper 调度

  * 高性能

    高吞吐量、低延迟、高并发、时间复杂度为 O(1)

  * 持久性与扩展性

    数据可持久化、容错性、支持在线水平扩展、消息自动平衡


### Kafka 的应用场景和实战

* Kafka 应用场景

  消息队列、行为跟踪、元信息监控、日志收集、流处理、事件源、持久性日志

* Kafka 简单案例

  * 启动 ZooKeeper

  * 启动 Kafka

    cd C:\developer\kafka_2.12-2.3.1

    bin\windows\kafka-server-start.bat config\server.properties

  * 创建 Topic

    bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic imooc-kafka-topic

    bin\windows\kafka-topics.bat --list --zookeeper localhost:2181

  * 启动 Producer

    bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic imooc-kafka-topic

  * 启动 Consumer

    bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic imooc-kafka-topic --from-beginning

* Kafka 代码案例

  kafka-demo

### Kafka 高级特性

* Kafka 消息事务

  * 为什么要支持事务

    满足“读取、处理、写入”模式

    流处理需求的不断增强

    不准确的数据处理的容忍度

  * 数据传输的事务定义

    * 最多一次

      消息不会被重复发送，最多被传输一次，但也有可能一次都不传输

    * 最少一次

      消息不会被漏发送，最少被传输一次，但也有可能被重复传输

    * 精确的一次（Exactly once）

      不会被漏传输也不会被重复传输，每个消息都被传输一次且仅被传输一次

  * 事务保证

    * 内部重试问题

      Procedure 幂等处理

    * 多分区原子写入

      offsets topic

    * 事务保证——避免僵尸实例

      每个事务 Producer 分配一个 transactional.id，在进程重新启动时能够识别相同的 Producer 实例

      Kafka 增加了一个与 transactional.id 相关的 epoch，存储每个 transactional.id 内部元数据

      一旦 epoch 被触发，任何具有相同 transactional.id 和更旧的 epoch 的 Producer 被视为僵尸，Kafka 会拒绝来自这些 Procedure 的后续事务性写入

* 零拷贝（用户空间和内核空间没有交互）

  * 简介

    * 网络传输持久性日志块

    * Java NIO（channel.transforTo()）
    * Linux sendfile 系统调用

  * 文件传输到网络的路径

    操作系统将数据从磁盘读入到内核空间的页缓存

    应用程序将数据从内核空间读入到用户空间缓存中

    应用程序将数据写回到内核空间 socket 缓存中

    操作系统将数据从 socket 缓冲区复制到网卡缓冲区，以便将数据经网络发出

  * 零拷贝过程

    操作系统将数据从磁盘读入到内核空间的页缓存

    将数据的位置和长度的信息的描述符增加至内核空间 socket 缓冲区

    操作系统将数据从 socket 缓冲区复制到网卡缓冲区，以便将数据经网络发出