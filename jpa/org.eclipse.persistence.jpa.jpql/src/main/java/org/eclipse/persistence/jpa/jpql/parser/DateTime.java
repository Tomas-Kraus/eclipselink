/*
 * Copyright (c) 2006, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Oracle - initial API and implementation
package org.eclipse.persistence.jpa.jpql.parser;

import java.util.List;
import org.eclipse.persistence.jpa.jpql.WordParser;

/**
 * This {@link Expression} represents a date or time.
 *
 * The JDBC escape syntax may be used for the specification of date, time, and timestamp literals.
 * <div><b>BNF:</b> <code>expression ::= {d 'yyyy-mm-dd'} | {t 'hh:mm:ss'} | {ts 'yyyy-mm-dd hh:mm:ss.f...'}</code></div>
 *
 * @version 2.4
 * @since 2.3
 * @author Pascal Filion
 */
public abstract class DateTime extends AbstractExpression {

    /**
     * The actual identifier found in the string representation of the JPQL query.
     */
    private String identifier;

    /**
     * Creates a new <code>DateTime</code>.
     *
     * @param parent The parent of this expression
     */
    public DateTime(AbstractExpression parent) {
        super(parent);
    }

    @Override
    public void acceptChildren(ExpressionVisitor visitor) {
    }

    @Override
    protected void addOrderedChildrenTo(List<Expression> children) {
        children.add(buildStringExpression(getText()));
    }

    /**
     * Returns the actual identifier found in the string representation of the JPQL query, which has
     * the actual case that was used.
     *
     * @return The identifier that was actually parsed
     */
    public String getActualIdentifier() {
        return identifier;
    }

    @Override
    public JPQLQueryBNF getQueryBNF() {
        return getQueryBNF(FunctionsReturningDatetimeBNF.ID);
    }

    @Override
    public String getText() {
        return super.getText();
    }

    @Override
    protected void parse(WordParser wordParser, boolean tolerant) {

        // JDBC escape format for date/time/timestamp
        if (wordParser.startsWith(LEFT_CURLY_BRACKET)) {
            parseJDBCEscapeFormat(wordParser);
        }
        // JPQL identifiers
        else {
            String identifier = parseIdentifier(wordParser);
            setText(identifier);
            this.identifier = wordParser.moveForward(identifier);
        }
    }

    private String parseIdentifier(WordParser wordParser) {

        int position = wordParser.position();
        char character = wordParser.character(position + 8);

        if (character == 'd' || character == 'D') {
            return CURRENT_DATE;
        }

        character = wordParser.character(position + 12);

        if (character == 's' || character == 'S') {
            return CURRENT_TIMESTAMP;
        }

        return CURRENT_TIME;
    }

    private void parseJDBCEscapeFormat(WordParser wordParser) {

        int startIndex = wordParser.position();
        int stopIndex  = startIndex + 1;

        for (int index = startIndex + 1, length = wordParser.length(); index < length; index++) {
            char character = wordParser.character(index);

            if (character == RIGHT_CURLY_BRACKET) {
                stopIndex = index + 1;
                break;
            }

            stopIndex++;
        }

        setText(wordParser.substring(startIndex, stopIndex));
        wordParser.moveForward(stopIndex - startIndex);
    }

    /**
     * Determines whether this {@link CurrentDateTime} represents the JDBC escape syntax for date, time,
     * timestamp formats.
     *
     * @return <code>true</code> if this {@link Expression} represents a JDBC escape syntax;
     * <code>false</code> otherwise
     */
    public boolean isJDBCDate() {
        return getText().charAt(0) == LEFT_CURLY_BRACKET;
    }

    @Override
    public String toActualText() {
        return getText();
    }

    @Override
    public String toParsedText() {
        return getText();
    }

    @Override
    protected void toParsedText(StringBuilder writer, boolean actual) {
        writer.append(actual && !isJDBCDate() ? getIdentifier() : getText());
    }

    /**
     * The actual identifier found in the string representation of the JPQL query.
     */
    String getIdentifier() {
        return identifier;
    }

}
