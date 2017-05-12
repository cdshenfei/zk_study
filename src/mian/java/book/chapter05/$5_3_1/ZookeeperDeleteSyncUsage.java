package book.chapter05.$5_3_1;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperDeleteSyncUsage implements Watcher {

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
		String path = "/";
		zk = new ZooKeeper("127.0.0.1", 5000, new ZookeeperDeleteSyncUsage());
		deletePath(path);
		Thread.sleep(Integer.MAX_VALUE);
	}

	/**
	 * 删除节点
	 * 
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private static void deletePath(String path) throws KeeperException, InterruptedException {
		List<String> childrenList = zk.getChildren(path, true);
		if (childrenList == null || childrenList.size() == 0) {
			return;
		}
		for (String childPath : childrenList) {
			String deletePath = path + "/" + childPath;
			if ("/".equals(path)) {
				deletePath = path + childPath;
			}
			deletePath(deletePath);
			System.out.println("开始删除节点:" + deletePath);
			zk.delete(deletePath, 4);
		}
	}

	/**
	 * 事件处理
	 */
	@Override
	public void process(WatchedEvent event) {
		System.out.println("接收到watcher事件:" + event);
	}
}
