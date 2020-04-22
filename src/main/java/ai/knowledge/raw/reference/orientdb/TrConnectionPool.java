package ai.knowledge.raw.reference.orientdb;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabasePool;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.OrientDBConfigBuilder;
import lombok.NonNull;

public class TrConnectionPool {
    protected OrientDB orient;
    protected ODatabasePool pool;

    public TrConnectionPool(@NonNull String orientUrl, @NonNull String orientDB,
                            @NonNull String orientUser, @NonNull String orientPass) {

        orient = new OrientDB(orientUrl, OrientDBConfig.defaultConfig());
        OrientDBConfigBuilder poolCfg = OrientDBConfig.builder();
        poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MIN, 2);
        poolCfg.addConfig(OGlobalConfiguration.DB_POOL_MAX, 5);
        pool = new ODatabasePool(orientUrl, orientDB, orientUser, orientPass,
                poolCfg.build());
    }

    public void finalize() throws Throwable {
        orient.close();
        pool.close();
    }
}
