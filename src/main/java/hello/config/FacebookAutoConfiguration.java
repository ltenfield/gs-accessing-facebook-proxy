package hello.config;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.social.FacebookProperties;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.facebook.api.Facebook;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Social connectivity with
 * Facebook.
 *
 * @author Craig Walls
 * @since 1.1.0
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, FacebookConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.facebook", name = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class FacebookAutoConfiguration {

	@Configuration
	@EnableSocial
	@EnableConfigurationProperties(FacebookProperties.class)
	@ConditionalOnWebApplication
	protected static class FacebookConfigurerAdapter extends SocialAutoConfigurerAdapter {

		HttpClient httpClient;

		@Autowired
		private FacebookProperties properties;

		@Bean
		@ConditionalOnMissingBean(Facebook.class)
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		public Facebook facebook(ConnectionRepository repository) {
			Connection<Facebook> connection = repository
					.findPrimaryConnection(Facebook.class);
			return connection != null ? connection.getApi() : null;
		}

		@Bean(name = { "connect/facebookConnect", "connect/facebookConnected" })
		@ConditionalOnProperty(prefix = "spring.social", name = "auto-connection-views")
		public GenericConnectionStatusView facebookConnectView() {
			return new GenericConnectionStatusView("facebook", "Facebook");
		}

		@Override
		protected ConnectionFactory<?> createConnectionFactory() {
			return new FacebookConnectionFactory(this.properties.getAppId(),
					this.properties.getAppSecret(),
					this.httpClient);
		}

		@Autowired
		@Qualifier("httpClientCentral")
		public void setHttpClient(HttpClient client) {
			this.httpClient = client;
		}
	}

}
