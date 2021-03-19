package org.acme.phone.sector.cache.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Prefixes local cache.
 *
 * @author Jose Monteiro (j.pedroteixeira.monteiro@gmail.com)
 * @since 1.0.0
 */
@Singleton
public class PrefixesCache {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PrefixesCache.class);
    /**
     * Prefixes file resources file path.
     */
    private static final String PREFIXES_FILE = "/phones/prefixes.txt";
    /**
     * In-memory prefixes storage.
     */
    private final HashSet<String> prefixes = new HashSet<>();

    /**
     * Constructor.
     *
     * @throws Exception when fails to initialize prefixes cache.
     */
    public PrefixesCache() throws Exception {
        loadPrefixesFromFile();
    }

    /**
     * Verify if prefix exist in cache.
     *
     * @param prefix the prefix to check if exist
     * @return true if exists, false otherwise
     */
    public boolean exists(final String prefix) {
        return prefixes.contains(prefix);
    }

    /**
     * Load prefixes from resources file.
     *
     * @throws Exception when fails to load prefixes from file.
     */
    private void loadPrefixesFromFile() throws Exception {
        try {
            final InputStream in = getClass().getResourceAsStream(PREFIXES_FILE);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                prefixes.add(line);
            }
        } catch (final Exception e) {
            LOGGER.error("Failed to load prefixes numbers.");
            throw new Exception("Failed to load prefixes numbers.");
        }
    }
}
