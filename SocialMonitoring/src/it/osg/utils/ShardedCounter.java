package it.osg.utils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This initial implementation simply counts all instances of the
 * SimpleCounterShard kind in the datastore. The only way to increment the
 * number of shards is to add another shard by creating another entity in the
 * datastore.
 */
public class ShardedCounter {

    /**
     * DatastoreService object for Datastore access.
     */
    private static final DatastoreService DS = DatastoreServiceFactory
            .getDatastoreService();

    /**
     * Default number of shards.
     */
    private static final int NUM_SHARDS = 150;

    /**
     * A random number generator, for distributing writes across shards.
     */
    private final Random generator = new Random();

    /**
     * A logger object.
     */
    private static final Logger LOG = Logger.getLogger(ShardedCounter.class
            .getName());

    /**
     * Retrieve the value of this sharded counter.
     *
     * @return Summed total of all shards' counts
     */
    public final long getCount(String counterName) {
        long sum = 0;

        Query query = new Query(counterName);
        for (Entity e : DS.prepare(query).asIterable()) {
            sum += (Long) e.getProperty("executedtask");
        }

        return sum;
    }

    /**
     * Increment the value of this sharded counter.
     */
    public final void increment(String counterName) {
        int shardNum = generator.nextInt(NUM_SHARDS);
        Key shardKey = KeyFactory.createKey(counterName,
                Integer.toString(shardNum));

        Transaction tx = DS.beginTransaction();
        Entity shard;
        try {
            try {
                shard = DS.get(tx, shardKey);
                long count = (Long) shard.getProperty("executedtask");
                shard.setUnindexedProperty("executedtask", count + 1L);
            } catch (EntityNotFoundException e) {
                shard = new Entity(shardKey);
                shard.setUnindexedProperty("executedtask", 1L);
            }
            DS.put(tx, shard);
            tx.commit();
        } catch (ConcurrentModificationException e) {
            LOG.log(Level.WARNING,
                    "You may need more shards. Consider adding more shards.");
            LOG.log(Level.WARNING, e.toString(), e);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.toString(), e);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    public final void delete(String counterName) {
        Query query = new Query(counterName);
        for (Entity e : DS.prepare(query).asIterable()) {
            DS.delete(e.getKey());
        }
    }
}