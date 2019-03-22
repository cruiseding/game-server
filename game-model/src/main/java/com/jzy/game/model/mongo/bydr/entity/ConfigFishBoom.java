/**
* 工具自动生成,暂时不支持泛型和对象
* @author CruiseDing
* @date 2017-10-31
*/
package com.jzy.game.model.mongo.bydr.entity;

import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import com.jzy.game.model.mongo.IConfigChecker;

/**
 *
 * @date 2017-10-31
 */
@Entity(value = "config_fish_boom", noClassnameStored = true)
public class ConfigFishBoom implements IConfigChecker {

	@Id
	@Indexed
	/** 编号 */
	private int id;
	
	/** 阵型id */
	private List<String> formationIds;
	
	/** 时间 */
	private List<String> refreshTimes;
	
	/** 线路id */
	private List<String> lineIds;
	
	/** 存活时间 */
	private List<String> surviveTimes;

	/** 编号 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/** 阵型id */
	public List<String> getFormationIds() {
		return formationIds;
	}

	public void setFormationIds(List<String> formationIds) {
		this.formationIds = formationIds;
	}

	/** 时间 */
	public List<String> getRefreshTimes() {
		return refreshTimes;
	}

	public void setRefreshTimes(List<String> refreshTimes) {
		this.refreshTimes = refreshTimes;
	}

	/** 线路id */
	public List<String> getLineIds() {
		return lineIds;
	}

	public void setLineIds(List<String> lineIds) {
		this.lineIds = lineIds;
	}

	/** 存活时间 */
	public List<String> getSurviveTimes() {
		return surviveTimes;
	}

	public void setSurviveTimes(List<String> surviveTimes) {
		this.surviveTimes = surviveTimes;
	}

}
