package com.jjy.game.engine.util;

import org.junit.Test;

import com.jzy.game.engine.util.SysUtils;

/**
 * 系统工具测试
 * @author CruiseDing
 * @QQ 359135103
 * 2017年10月12日 下午2:28:07
 */
public class SysUtilTest {

	@Test
	public void testJvmInfo() {
		System.err.println(SysUtils.jvmInfo("\r\n"));
//		System.err.println(SysUtil.jvmInfo("<br>"));
	}
}
