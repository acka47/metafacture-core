/*
 *  Copyright 2013, 2014 Deutsche Nationalbibliothek
 *
 *  Licensed under the Apache License, Version 2.0 the "License";
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.culturegraph.mf.morph.collectors;

import org.culturegraph.mf.morph.Metamorph;
import org.culturegraph.mf.morph.NamedValueSource;

/**
 * Corresponds to the <code>&lt;collect-literal&gt;</code> tag.
 *
 * @author Markus Michael Geipel
 */
public final class Concat extends AbstractFlushingCollect {

	private final StringBuilder builder = new StringBuilder();

	private String prefix = "";
	private String postfix = "";
	private String delimiter = "";
	private boolean reverse = false;

	private String currentDelimiter = "";

	public Concat(final Metamorph metamorph) {
		super(metamorph);
		setNamedValueReceiver(metamorph);
	}

	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public void setPostfix(final String postfix) {
		this.postfix = postfix;
	}

	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	public void setReverse(final boolean reverse) {
		this.reverse = reverse;
	}

	@Override
	protected void emit() {
		if (builder.length() != 0) {
			final String value = prefix + builder.toString() + postfix;
			getNamedValueReceiver().receive(getName(), value, this,
					getRecordCount(), getEntityCount());
		}
	}

	@Override
	protected boolean isComplete() {
		return false;
	}

	@Override
	protected void receive(final String name, final String value,
			final NamedValueSource source) {

		if (reverse) {
			builder.insert(0, currentDelimiter);
			builder.insert(0, value);
		} else {
			builder.append(currentDelimiter);
			builder.append(value);
		}
		currentDelimiter = delimiter;
	}

	@Override
	protected void clear() {
		builder.delete(0, builder.length());
		currentDelimiter = "";
	}

}
