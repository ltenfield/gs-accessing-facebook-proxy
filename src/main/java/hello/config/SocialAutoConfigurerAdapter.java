package hello.config;

import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * Base class for auto-configured {@link SocialConfigurerAdapter}s.
 *
 * @author Craig Walls
 * @author Phillip Webb
 * @since 1.1.0
 */
abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {

	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
			Environment environment) {
		configurer.addConnectionFactory(createConnectionFactory());
	}

	protected abstract ConnectionFactory<?> createConnectionFactory();

}
