package com.jzy.game.gate.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;
import org.apache.mina.filter.ssl.SslFilter;
import com.jzy.game.engine.mina.config.MinaServerConfig;
import com.jzy.game.engine.mina.service.ClientServerService;
import com.jzy.game.engine.thread.ThreadPoolExecutorConfig;
import com.jzy.game.gate.manager.UserSessionManager;
import com.jzy.game.gate.server.handler.GateTcpUserServerHandler;
import com.jzy.game.gate.server.ssl.GateSslContextFactory;

/**
 * 网关 用户 TCP服务器
 *
 * @author CruiseDing
 * @date 2017-03-30 QQ:359135103
 */
public class GateTcpUserServer extends ClientServerService {
	private static final Map<String, IoFilter> filters = new HashMap<>();
	private static final BlacklistFilter blacklistFilter = new BlacklistFilter(); // IP黑名单过滤器

	static {
		filters.put("Blacklist", blacklistFilter);

		// //添加ssl支持
		// if (USE_SSL) {
		// //ssl是串行执行，加上加解密，速度很慢
		// SslFilter sslFilter = new SslFilter(GateSslContextFactory.getInstance(true));
		// filters.put("SSL", sslFilter);
		// }
	}

	public GateTcpUserServer(ThreadPoolExecutorConfig threadExcutorConfig, MinaServerConfig minaServerConfig) {
		super(threadExcutorConfig, minaServerConfig, new GateTcpUserServerHandler(), filters);
	}

	public static BlacklistFilter getBlacklistFilter() {
		return blacklistFilter;
	}

	@Override
	protected void onShutdown() {
		UserSessionManager.getInstance().onShutdown();
		super.onShutdown();
	}

}
