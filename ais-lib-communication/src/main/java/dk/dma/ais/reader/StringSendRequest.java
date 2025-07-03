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

import java.util.Arrays;

/**
 * Class to represent an AIS send request. A send request is defined by AIS message, sequence number and a destination
 * that is zero for broadcasting.
 */
public class StringSendRequest extends SendRequest {
    private final String[] sentences;

    /**
     * Instantiates a new String Send request.
     *
     * @param sentences the ais message
     */
    public StringSendRequest(String... sentences) {
        super(null, 0, 0);
        this.sentences = sentences;
    }

    /**
     * Generate sentences to send. ABM or BBM. c
     *
     * @return array of actual sentences
     * @throws SendException the send exception
     */
    public String[] createSentences() throws SendException {
        return this.sentences;
    }

    /**
     * Static method to generate hash from send request
     *
     * @return sentences.hashCode()
     */
    public String hash() {
        return String.valueOf(Arrays.hashCode(sentences));
    }

    @Override
    public String toString() {
        return "SendRequest [sentences=" + Arrays.toString(sentences) + "]";
    }

}
