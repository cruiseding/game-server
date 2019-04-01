package com.jzy.game.ai.nav.triangle;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.jzy.game.ai.nav.triangle.TriangleNavMesh;
import com.jzy.game.ai.nav.triangle.TriangleGraph;
import com.jzy.game.ai.nav.triangle.TriangleGraphPath;
import com.jzy.game.ai.nav.triangle.TrianglePointPath;
import com.jzy.game.ai.pfa.Connection;
import com.jzy.game.engine.math.Vector3;
import com.jzy.game.engine.util.FileUtils;
import com.jzy.game.engine.util.TimeUtils;

/**
 * 测试寻路
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年11月7日 下午5:38:46
 */
public class TriangleNavMeshTest {
	/** 地图数据路径 */
	// private static final String meshPath =
	// "E:\\Java\\game-server\\game-ai\\src\\test\\resources\\navmesh\\1000.navmesh";
//	private static final String meshPath = "E:\\ldlh\\client\\Config\\Nav_build\\102.navmesh";
//	 private static final String meshPath = "E:\\Project\\game-server2\\game-server\\game-ai\\101.navmesh";
	 private static final String meshPath = "E:\\game-server\\game-server\\game-ai\\119.navmesh";
	 TriangleNavMesh navMesh;

	@Before
	public void init() {
		long start = TimeUtils.currentTimeMillis();
		String navMeshStr = FileUtils.readTxtFile(meshPath);
		navMesh = new TriangleNavMesh(navMeshStr);
		System.out.println("加载地图耗时：" + (TimeUtils.currentTimeMillis() - start));
	}

	/**
	 * 加载NavMesh
	 * 
	 * @author CruiseDing
	 * @QQ 359135103 2017年11月7日 下午5:39:30
	 */
	@Test
	public void testLoadNavMesh() {
		TriangleGraph graph = navMesh.getGraph();
		System.out.println(String.format("三角形个数：%d 所有边%s 共享边：%d 独立边：%d", graph.getTriangleCont(),
				graph.getNumTotalEdges(), graph.getNumConnectedEdges(), graph.getNumDisconnectedEdges()));
//		System.out.println(JSON.toJSONString(navMesh.getGraph().getTriangles()));
	}

	/**
	 * 获取三角形
	 */
	@Test
	public void testTriangle() {
		long start = TimeUtils.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			Triangle triangle = navMesh.getTriangle(new Vector3(264, 18, 117));
			if (triangle != null) {
				System.err.println(triangle.toString());
			}
			Assert.assertNotNull(triangle);
		}
		
		System.out.println("获取三角形耗时：" + (TimeUtils.currentTimeMillis() - start));
	}

	/**
	 * 查找路径
	 */
	@Test
	public  void testPerformance() {
		TriangleGraphPath path=new TriangleGraphPath();
		TrianglePointPath pointPath=new TrianglePointPath();
		long start=TimeUtils.currentTimeMillis();
		List<Vector3> list=null;
		for(int i=0;i<10000;i++) {
//			list = navMesh.findPath(new Vector3(61,13,191), new Vector3(107,11,146), pointPath);				//1
//			list = navMesh.findPath(new Vector3(61,13,191), new Vector3(305,35,213), pointPath);				//2
//			 list = navMesh.findPath(new Vector3(28f,27.6f,111f), new Vector3(50,28,100), pointPath);			//3
//			 list = navMesh.findPath(new Vector3(28f,27.6f,111f), new Vector3(221.4f,70,161.3f), pointPath);	//4 
//			list = navMesh.findPath(new Vector3(28f,27.6f,111f), new Vector3(176.5f,19.8f,41.3f), pointPath);	//5 
//			list = navMesh.findPath(new Vector3(60.27f,0f,495.56f), new Vector3(429.0f,0f,125.0f), pointPath);	//7
//			list = navMesh.findPath(new Vector3(12f,0f,505f), new Vector3(407f,0f,95f), pointPath);				//8
			list = navMesh.findPath(new Vector3(373f,0f,247f), new Vector3(353f,0f,213f), pointPath);				//9
			
		}
		System.err.println("耗时："+(TimeUtils.currentTimeMillis()-start));
		if(list!=null) {
            list.forEach(v->System.out.println(v.toString()));
        }
		
	}
	
	@Test
	public void testFindPath() {
	    TrianglePointPath pointPath=new TrianglePointPath();
	    List<Vector3> list = navMesh.findPath(new Vector3(104, 138), new Vector3(212.0f,232.0f), pointPath);
	    if(list!=null) {
            list.forEach(v->System.out.println(v.toString()));
        }
	}
	
	@Test
	public void test() {
		System.out.println(1 << 9);
	}
}
