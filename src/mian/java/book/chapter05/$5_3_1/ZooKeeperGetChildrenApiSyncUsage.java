package book.chapter05.$5_3_1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperGetChildrenApiSyncUsage implements Watcher {

	/**
	 * 控制连接的信号量
	 */
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * zk连接
	 */
	private static ZooKeeper zk = null;

	/**
	 * 主方法
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		String path = "/zk-book5";
		zk = new ZooKeeper("127.0.0.1", 5000, new ZooKeeperGetChildrenApiSyncUsage());
		connectedSemaphore.await();
		zk.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		List<String> childrenList = zk.getChildren(path, true);
		System.out.println(childrenList);
		zk.create(path + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Thread.sleep(Integer.MAX_VALUE);
	}

	/**
	 * 事件处理
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("接收到watcher事件:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				connectedSemaphore.countDown();
			} else if (event.getType() == EventType.NodeChildrenChanged) {
				try {
					System.out.println("重新获得Child节点:" + zk.getChildren(event.getPath(), true));
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}