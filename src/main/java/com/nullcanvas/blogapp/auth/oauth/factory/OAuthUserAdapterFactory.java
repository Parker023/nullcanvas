package com.nullcanvas.blogapp.auth.oauth.factory;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.adapter.OAuthUserAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Component
public class OAuthUserAdapterFactory {

	private final Map<ProviderType, OAuthUserAdapter> adapterMap;

	public OAuthUserAdapterFactory(List<OAuthUserAdapter> adapters) {
		adapterMap = adapters.stream()
			.collect(Collectors.toMap(OAuthUserAdapter::getProviderType, Function.identity()));
	}

	public OAuthUserAdapter getAdapter(ProviderType providerType) {
		if (!adapterMap.containsKey(providerType))
			throw new IllegalArgumentException("invalid provider type: " + providerType);
		return adapterMap.get(providerType);
	}

}
