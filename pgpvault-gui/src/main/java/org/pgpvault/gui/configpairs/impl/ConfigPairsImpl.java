package org.pgpvault.gui.configpairs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.pgpvault.gui.config.api.ConfigRepository;
import org.pgpvault.gui.configpairs.api.ConfigPairs;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is VERY simple map-based impl of this storage. It uses config repo and
 * performs write operation each time after single key-value pair change
 * 
 * @author sergeyk
 *
 */
public class ConfigPairsImpl implements ConfigPairs {
	@Autowired
	private ConfigRepository configRepository;

	private ConfigPairsEnvelop configPairsEnvelop;

	@Override
	public synchronized void put(String key, Object value) {
		ensureLoadded();
		if (value == null) {
			configPairsEnvelop.remove(key);
		} else {
			configPairsEnvelop.put(key, value);
		}
		save();
	}

	private void save() {
		configRepository.persist(configPairsEnvelop);
	}

	private void ensureLoadded() {
		if (configPairsEnvelop == null) {
			configPairsEnvelop = configRepository.readOrConstruct(ConfigPairsEnvelop.class);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T> T find(String key, T defaultValue) {
		ensureLoadded();
		T ret = (T) configPairsEnvelop.get(key);
		return ret != null ? ret : defaultValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllWithPrefixedKey(String keyPrefix) {
		ensureLoadded();
		List<T> ret = new ArrayList<>();
		for (Entry<String, Object> entry : configPairsEnvelop.entrySet()) {
			if (entry.getKey().startsWith(keyPrefix)) {
				ret.add((T) entry.getValue());
			}
		}
		return ret;
	}

}