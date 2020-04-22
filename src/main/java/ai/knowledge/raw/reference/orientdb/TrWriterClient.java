package ai.knowledge.raw.reference.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@CommonsLog
public class TrWriterClient extends TrConnectionPool {

    public TrWriterClient(@NonNull String orientUrl, @NonNull String orientDB,
                          @NonNull String orientUser, @NonNull String orientPass) {
        super(orientUrl, orientDB, orientUser, orientPass);
    }

    public OVertex createVertex(@NonNull Object vertexObj) {
        try (ODatabaseSession db = pool.acquire()) {
            // check and create vertex class
            if (db.getClass(vertexObj.getClass().getSimpleName()) == null) {
                db.createVertexClass(vertexObj.getClass().getSimpleName());
            }

            // Create vertex
            OVertex vertex = db.newVertex(vertexObj.getClass().getSimpleName());

            // Getting fields of the class
            Field[] fields = vertexObj.getClass().getDeclaredFields();
            // for each field get its value and set in Vertex
            for (Field f : fields) {
                String fieldName = f.getName();
                Object retObj = callGetter(vertexObj, fieldName);
                if (retObj != null && checkJDK(retObj)) {
                    vertex.setProperty(fieldName, retObj);
                }
            }
            // save the vertex object
            //log.info(String.format("saved DB the following Vertex: %s",
            // vertex.save().toJSON()));
            return (OVertex) vertex.save();
        } finally {
            // releases back connection to pool, weird, hope
            // later version of orient driver fixes this
            orient.close();
        }

    }

    public OEdge createEdge(@NonNull OVertex fromV, @NonNull OVertex toV,
                            @NonNull String edgeName) {
        try (ODatabaseSession db = pool.acquire()) {
            // check and create vertex class
            if (db.getClass(edgeName) == null) {
                db.createEdgeClass(edgeName);
            }

            // Create vertex
            OEdge edge = db.newEdge(fromV, toV, edgeName);
            //save the edge
            return (OEdge) edge.save();
        } finally {
            // releases back connection to pool, weird, hope
            // later version of orient driver fixes this
            orient.close();
        }
    }

    public OEdge createEdge(@NonNull OVertex fromV, @NonNull OVertex toV,
                            @NonNull Object edgeObj) {
        try (ODatabaseSession db = pool.acquire()) {
            // check and create vertex class
            if (db.getClass(edgeObj.getClass().getSimpleName()) == null) {
                db.createEdgeClass(edgeObj.getClass().getSimpleName());
            }

            // Create vertex
            OEdge edge = db.newEdge(fromV, toV, edgeObj.getClass().getSimpleName());

            // Getting fields of the class
            Field[] fields = edgeObj.getClass().getDeclaredFields();

            // for each field get its value and set in Vertex
            for (Field f : fields) {
                String fieldName = f.getName();
                Object retObj = callGetter(edgeObj, fieldName);
                if (retObj != null && checkJDK(retObj)) {
                    edge.setProperty(fieldName, retObj);
                }
            }
            // save the edge
            return (OEdge) edge.save();
        } finally {
            // releases back connection to pool, weird, hope
            // later version of orient driver fixes this
            orient.close();
        }
    }

    private Object callGetter(Object obj, String fieldName) {
        PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            return pd.getReadMethod().invoke(obj);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            log.error(String.format("Could not call getter for object: %s, " +
                            "fieled: %s, error: %s",
                    obj.getClass().getSimpleName(), fieldName, e.getMessage()));
        }
        return null;
    }

    private boolean checkJDK(Object obj) {
        // checks if an object is from JDK package, otherwise custom
        // and needs to be excluded from orientDB vertex/Edge creation as
        // user defined nested objects are not supported
        return obj.getClass().getPackage().getName().startsWith("java");
    }

}
