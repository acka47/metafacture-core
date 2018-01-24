/*
 * Copyright 2016 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
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
package org.metafacture.metamorph.api;

/**
 * Thrown by Metamorph if an error occurs while parsing and building the
 * Metamorph transformation pipeline.
 *
 * @author Markus Michael Geipel
 */
public final class MorphBuildException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public MorphBuildException(final String message) {
        super(message);
    }

    public MorphBuildException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
