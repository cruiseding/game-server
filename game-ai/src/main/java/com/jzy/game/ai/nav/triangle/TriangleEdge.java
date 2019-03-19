package com.jzy.game.ai.nav.triangle;

import com.jzy.game.ai.pfa.Connection;
import com.jzy.game.engine.math.Vector3;

/**
 * 相连接三角形的共享边
 * 
 * @author CruiseDing
 * @QQ 359135103 2017年11月7日 下午4:50:11
 */
public class TriangleEdge implements Connection<Triangle> {
	/** 右顶点 */
	public Vector3 rightVertex;
	public Vector3 leftVertex;

	/** 源三角形 */
	public Triangle fromNode;
	/** 指向的三角形 */
	public Triangle toNode;

	public TriangleEdge(Vector3 rightVertex, Vector3 leftVertex) {
		this(null, null, rightVertex, leftVertex);
	}

	public TriangleEdge(Triangle fromNode, Triangle toNode, Vector3 rightVertex, Vector3 leftVertex) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.rightVertex = rightVertex;
		this.leftVertex = leftVertex;
	}

	@Override
	public float getCost() {
		return 1;
	}

	@Override
	public Triangle getFromNode() {
		return fromNode;
	}

	@Override
	public Triangle getToNode() {
		return toNode;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("Edge{");
		sb.append("fromNode=").append(fromNode.index);
		sb.append(", toNode=").append(toNode == null ? "null" : toNode.index);
		sb.append(", rightVertex=").append(rightVertex);
		sb.append(", leftVertex=").append(leftVertex);
		sb.append('}');
		return sb.toString();
	}

}
