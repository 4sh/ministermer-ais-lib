/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.reader;

import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.ais.sentence.Abk;
import java.util.function.Consumer;

/**
 * Class to hold a pool of send threads.
 */
@ThreadSafe
public class SendThreadPool {

    private static final Logger LOG = LoggerFactory.getLogger(SendThreadPool.class);

    /**
     * The pool is implemented as a hash map
     */
    private ConcurrentHashMap<String, SendThread> threads = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Send thread pool.
     */
    public SendThreadPool() {}

    /**
     * Create a new send thread and register in pool
     *
     * @param sendRequest    the send request
     * @param resultListener the result listener
     * @return send thread
     */
    public SendThread createSendThread(SendRequest sendRequest, Consumer<Abk> resultListener) {
        // Generate hash value that uniquely defines message
        String hash = sendRequest.hash();

        // Create thread
        LOG.debug("Creating SendThread with hash: " + hash);
        SendThread sendThread = new SendThread(hash, resultListener, this);

        // Add thread to threads
        threads.put(hash, sendThread);
        LOG.debug("Threads in pool: " + threads.size());

        return sendThread;
    }

    /**
     * Handle received ABK
     *
     * @param abk the abk
     */
    public void handleAbk(Abk abk) {
        LOG.debug("Handling Abk: " + abk);
        // Find thread
        String hash = abkHash(abk);
        SendThread sendThread = threads.get(hash);
        if (sendThread == null) {
            LOG.debug("SendThread for abk " + abk + " does not exist");
            return;
        }
        // Set Abk for thread
        sendThread.setAbk(abk);
    }

    /**
     * Remove thread.
     *
     * @param hash the hash
     */
    public void removeThread(String hash) {
        LOG.debug("Removing thread with hash: " + hash);
        threads.remove(hash);
        LOG.debug("Threads in pool: " + threads.size());
    }

    /**
     * Static method to generate hash from ABK
     *
     * @param abk the abk
     * @return seq +sentenceStr id+dest
     */
    public static String abkHash(Abk abk) {
        int seq = abk.getSequence();
        int msgId = abk.getMsgId();
        int destination = abk.getDestination();
        return String.format("%d+%d+%d", seq, msgId, destination);
    }

}
