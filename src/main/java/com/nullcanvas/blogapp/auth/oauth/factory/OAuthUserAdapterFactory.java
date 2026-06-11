package com.nullcanvas.blogapp.auth.oauth.factory;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.adapter.OAuthUserAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory that resolves the {@link OAuthUserAdapter} for a given {@link ProviderType}.
 *
 * <p>
 * Spring injects every adapter bean; the factory indexes them by provider so a new
 * provider can be added simply by contributing another adapter, with no change here.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Component
public class OAuthUserAdapterFactory {

	/** Index of adapters keyed by the provider each one handles. */
	private final Map<ProviderType, OAuthUserAdapter> adapterMap;

	/**
	 * Builds the provider-to-adapter index from all adapters discovered by Spring.
	 * @param adapters every {@link OAuthUserAdapter} bean in the context
	 */
	public OAuthUserAdapterFactory(List<OAuthUserAdapter> adapters) {
		// Index each adapter by the provider it reports via getProviderType().
		adapterMap = adapters.stream()
			.collect(Collectors.toMap(OAuthUserAdapter::getProviderType, Function.identity()));
	}

	/**
	 * Returns the adapter registered for the requested provider.
	 * @param providerType the provider to resolve
	 * @return the matching adapter
	 * @throws IllegalArgumentException if no adapter is registered for the provider
	 */
	public OAuthUserAdapter getAdapter(ProviderType providerType) {
		// Fail fast for unsupported providers rather than returning null.
		if (!adapterMap.containsKey(providerType))
			throw new IllegalArgumentException("invalid provider type: " + providerType);
		return adapterMap.get(providerType);
	}

}
