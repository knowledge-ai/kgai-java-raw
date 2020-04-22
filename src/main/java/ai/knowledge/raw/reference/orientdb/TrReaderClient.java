package ai.knowledge.raw.reference.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;

import java.util.ArrayList;

@CommonsLog
public class TrReaderClient extends TrConnectionPool {


    public TrReaderClient(@NonNull String orientUrl, @NonNull String orientDB,
                          @NonNull String orientUser, @NonNull String orientPass) {
        super(orientUrl, orientDB, orientUser, orientPass);
    }

    public ArrayList<OResult> executeQuery(String query) {
        try (ODatabaseSession db = pool.acquire()) {
            OResultSet rs = db.query(query);
            ArrayList<OResult> results = new ArrayList<>();

            while (rs.hasNext()) {
                results.add(rs.next());
            }

            rs.close(); //REMEMBER TO ALWAYS CLOSE THE RESULT SET!!!
            return results;
        } finally {
            // releases back connection to pool, weird, hope
            // later version of orient driver fixes this
            orient.close();
        }
    }
}
