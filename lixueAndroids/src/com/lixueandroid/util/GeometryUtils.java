package com.lixueandroid.util;

import android.graphics.PointF;

/**
 * 几何工具箱
 */
public class GeometryUtils {

	/**
	 * 使用X轴射线法判断给定的点是否在给定的多边形内部
	 * @param point 给定的点
	 * @param vertexPoints 多边形的所有顶点坐标
	 * @return
	 */
	public static boolean isPolygonContainPoint(PointF point,PointF[] vertexPoints){
		int nCross = 0;
		for (int i = 0; i < vertexPoints.length; i++) {
			PointF p1 = vertexPoints[i];
			PointF p2 = vertexPoints[(i + 1) % vertexPoints.length];
			if (p1.y == p2.y)
				continue;
			if (point.y < Math.min(p1.y, p2.y))
				continue;
			if (point.y >= Math.max(p1.y, p2.y))
				continue;
			double x = (double) (point.y - p1.y) * (double) (p2.x - p1.x)/ (double) (p2.y - p1.y) + p1.x;
			if (x > point.x)
				nCross++;
		}
		return (nCross % 2 == 1);
	}
}