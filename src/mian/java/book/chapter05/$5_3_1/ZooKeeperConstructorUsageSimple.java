package book.chapter05.$5_3_1;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建一个基本的ZooKeeper会话实例
 * 
 * @author wyshenfei
 *
 */
public class ZooKeeperConstructorUsageSimple implements Watcher {

	/**
	 * 控制连接的信号量
	 */
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * 主方法
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// 与ZK服务器创建TCP连接
		ZooKeeper zookeeper = new ZooKeeper("127.0.0.1", 5000, new ZooKeeperConstructorUsageSimple());
		System.out.println("当前ZK的状态:" + zookeeper.getState());
		try {
			System.out.println(zookeeper.getSessionId());
			System.out.println(zookeeper.getSessionPasswd());
			connectedSemaphore.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("ZK会话已经建立!");
	}

	/**
	 * 捕获连接
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("接收到watcher事件:" + event);
		if (KeeperState.SyncConnected == event.getState()) {
			connectedSemaphore.countDown();
		}
	}

}