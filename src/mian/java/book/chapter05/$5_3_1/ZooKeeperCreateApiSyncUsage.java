package book.chapter05.$5_3_1;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZooKeeper API创建节点，使用同步接口
 * 
 * @author wyshenfei
 *
 */
public class ZooKeeperCreateApiSyncUsage implements Watcher {

	/**
	 * 控制连接的信号量
	 */
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * 主方法
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		// 与ZK服务器创建TCP连接
		ZooKeeper zookeeper = new ZooKeeper("127.0.0.1", 5000, new ZooKeeperCreateApiSyncUsage());
		connectedSemaphore.await();
		String path1 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("成功创建znode:" + path1);
		String path2 = zookeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("成功创建znode:" + path2);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("接收到watcher事件:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}
