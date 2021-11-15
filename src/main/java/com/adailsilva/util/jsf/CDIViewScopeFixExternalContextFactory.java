package com.adailsilva.util.jsf;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.enterprise.inject.spi.CDI;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.ExternalContextFactory;
import javax.faces.context.ExternalContextWrapper;

import com.sun.faces.util.Util;

public class CDIViewScopeFixExternalContextFactory extends ExternalContextFactory {

	private final ExternalContextFactory wrapped;

	public CDIViewScopeFixExternalContextFactory(ExternalContextFactory wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExternalContext getExternalContext(Object context, Object request, Object response) throws FacesException {
		ExternalContext externalContext = getWrapped().getExternalContext(context, request, response);
		CDIViewScopeWorkaround wrapper = new CDIViewScopeWorkaround(externalContext);
		return wrapper;
	}

	@Override
	public ExternalContextFactory getWrapped() {
		return this.wrapped;
	}

	private static class CDIViewScopeWorkaround extends ExternalContextWrapper {

		private static String CDI_KEY;
		static {
			try {
				Field f = Util.class.getDeclaredField("CDI_AVAILABLE_PER_APP_KEY");
				f.setAccessible(true);
				CDI_KEY = (String) f.get(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		private final ExternalContext wrapped;

		public CDIViewScopeWorkaround(ExternalContext wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public ExternalContext getWrapped() {
			return wrapped;
		}

		@Override
		public Map<String, Object> getApplicationMap() {
			MapFix mapFix = new MapFix(super.getApplicationMap());
			return mapFix;
		}
	}

	private static class MapFix extends MapWrapper<String, Object> {

		public MapFix(Map<String, Object> delegate) {
			super(delegate);
		}

		@Override
		public Object get(Object key) {
			if (CDIViewScopeWorkaround.CDI_KEY.equals(key)) {
				return CDI.current().getBeanManager();
			}
			return super.get(key);
		}
	}

	private static class MapWrapper<K, V> implements Map<K, V> {

		private Map<K, V> delegate;

		public int size() {
			return delegate.size();
		}

		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		public boolean containsKey(Object key) {
			return delegate.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return delegate.containsValue(value);
		}

		public V get(Object key) {
			return delegate.get(key);
		}

		public V put(K key, V value) {
			return delegate.put(key, value);
		}

		public V remove(Object key) {
			return delegate.remove(key);
		}

		public void putAll(Map<? extends K, ? extends V> m) {
			delegate.putAll(m);
		}

		public void clear() {
			delegate.clear();
		}

		public Set<K> keySet() {
			return delegate.keySet();
		}

		public Collection<V> values() {
			return delegate.values();
		}

		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return delegate.entrySet();
		}

		public boolean equals(Object o) {
			return delegate.equals(o);
		}

		public int hashCode() {
			return delegate.hashCode();
		}

		public MapWrapper(Map<K, V> delegate) {
			this.delegate = delegate;
		}
	}
}