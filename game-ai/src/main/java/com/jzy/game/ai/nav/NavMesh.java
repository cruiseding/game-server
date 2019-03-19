package com.jzy.game.ai.nav;

import java.util.List;

import com.jzy.game.engine.math.Vector3;

/**
 * navmesh寻路入口
 * 
 * @author CruiseDing
 * @mail 359135103@qq.com
 */
public abstract class NavMesh {

	/** 地图宽x轴 */
	protected float width;
	/** 地图高y轴 */
	protected float height;
	/** 配置id */
	protected int mapId;

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * 获取在路径中的坐标点<br>
	 * 屏幕输入坐标点
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public abstract Vector3 getPointInPath(float x, float z);

	/**
	 * 获取随机坐标
	 * 
	 * @param center
	 *            中心坐标
	 * @param radius
	 *            随机半径
	 * @param minDisToCenter
	 *            到中心点的最小距离
	 * @return
	 */
	public List<Vector3> getRandomPointsInPath(Vector3 center, float radius, float minDisToCenter) {
		throw new UnsupportedOperationException("不支持获取随机点");
	}

	/**
	 * 获取随机坐标
	 * 
	 * @param center
	 * @param radius
	 * @param minDisToCenter
	 * @return
	 */
	public Vector3 getRandomPointInPath(Vector3 center, float radius, float minDisToCenter) {
		throw new UnsupportedOperationException("不支持获取随机点");
	}

	/**
	 * 获取随机坐标
	 * 
	 * @param center
	 * @param radius
	 * @param minDisToCenter
	 * @return
	 */
	public Vector3 getRandomPointInPath(Vector3 center, float radius) {
		return this.getRandomPointInPath(center, radius, 0);
	}

	/**
	 * 坐标点是否在路径中
	 * 
	 */
	public boolean isPointInPath(Vector3 point) {
		return getPointInPath(point.x, point.z) != null;
	}
}
