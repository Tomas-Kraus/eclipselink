/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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
//     04/21/2022: Tomas Kraus
//       - Issue 1474: Update JPQL Grammar for Jakarta Persistence 2.2, 3.0 and 3.1
package org.eclipse.persistence.jpa.jpql.parser;

import org.eclipse.persistence.jpa.jpql.ExpressionTools;
import org.eclipse.persistence.jpa.jpql.JPAVersion;

import static org.eclipse.persistence.jpa.jpql.parser.Expression.CEILING;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.EXP;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.FLOOR;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.LN;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.LOCAL_DATE;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.LOCAL_DATETIME;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.LOCAL_TIME;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.POWER;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.ROUND;
import static org.eclipse.persistence.jpa.jpql.parser.Expression.SIGN;

/**
 * This {@link JPQLGrammar} provides support for parsing JPQL queries defined in Jakarta Persistence 3.1.
 * <pre><code>
 * functions_returning_numerics ::= ABS(arithmetic_expression) |
 *                                  CEILING(arithmetic_expression) |
 *                                  EXP(arithmetic_expression) |
 *                                  FLOOR(arithmetic_expression) |
 *                                  LN(arithmetic_expression) |
 *                                  MOD(arithmetic_expression, arithmetic_expression) |
 *                                  POWER(arithmetic_expression, arithmetic_expression) |
 *                                  ROUND(arithmetic_expression, arithmetic_expression) |
 *                                  SIGN(arithmetic_expression) |
 *                                  SQRT(arithmetic_expression) |
 *                                  SIZE(collection_valued_path_expression) |
 *                                  INDEX(identification_variable) |
 *                                  extract_datetime_field
 *
 * extract_datetime_field := EXTRACT(datetime_field FROM datetime_expression)
 *
 * datetime_field := identification_variable
 * </code></pre>
 */
public class JPQLGrammar3_1 extends AbstractJPQLGrammar {

    /**
     * The singleton instance of this {@link JPQLGrammar3_1}.
     */
    private static final JPQLGrammar INSTANCE = new JPQLGrammar3_1();

    /**
     * Creates an insance of Jakarta Persistence 3.1 JPQL grammar.
     */
    public JPQLGrammar3_1() {
        super();
    }

    /**
     * Creates an instance of Jakarta Persistence 3.1 JPQL grammar.
     *
     * @param jpqlGrammar The {@link JPQLGrammar} to extend with the content of this one without
     * instantiating the base {@link JPQLGrammar}
     */
    private JPQLGrammar3_1(AbstractJPQLGrammar jpqlGrammar) {
        super(jpqlGrammar);
    }

    /**
     * Extends the given {@link JPQLGrammar} with the information of this one without instantiating
     * the base {@link JPQLGrammar}.
     *
     * @param jpqlGrammar The {@link JPQLGrammar} to extend with the content of this one without
     * instantiating the base {@link JPQLGrammar}
     */
    public static void extend(AbstractJPQLGrammar jpqlGrammar) {
        new JPQLGrammar3_1(jpqlGrammar);
    }

    /**
     * Returns the singleton instance of the default implementation of {@link JPQLGrammar} which
     * provides support for the JPQL grammar defined in the Jakarta Persistence 3.1 functional specification.
     *
     * @return The {@link JPQLGrammar} that only has support for Jakarta Persistence 3.1
     */
    public static JPQLGrammar instance() {
        return INSTANCE;
    }

    @Override
    protected JPQLGrammar buildBaseGrammar() {
        return new JPQLGrammar3_0();
    }

    @Override
    public JPAVersion getJPAVersion() {
        return JPAVersion.VERSION_3_1;
    }

    @Override
    public String getProvider() {
        return DefaultJPQLGrammar.PROVIDER_NAME;
    }

    @Override
    public String getProviderVersion() {
        return ExpressionTools.EMPTY_STRING;
    }

    @Override
    protected void initializeBNFs() {
        registerBNF(new InternalPowerExpressionBNF());
        registerBNF(new InternalRoundExpressionBNF());
    }

    @Override
    protected void initializeExpressionFactories() {
        registerFunctionExpressionFactory(new MathExpressionFactory.Ceiling(), CEILING);
        registerFunctionExpressionFactory(new MathExpressionFactory.Exp(), EXP);
        registerFunctionExpressionFactory(new MathExpressionFactory.Floor(), FLOOR);
        registerFunctionExpressionFactory(new MathExpressionFactory.Ln(), LN);
        registerFunctionExpressionFactory(new MathExpressionFactory.Power(), POWER);
        registerFunctionExpressionFactory(new MathExpressionFactory.Round(), ROUND);
        registerFunctionExpressionFactory(new MathExpressionFactory.Sign(), SIGN);

        registerFactory(new LocalDateTimeFactory());
        addChildFactory(FunctionsReturningDatetimeBNF.ID, LocalDateTimeFactory.ID);
    }

    @Override
    protected void initializeIdentifiers() {
        registerFunctionIdentifier(CEILING);
        registerFunctionIdentifier(EXP);
        registerFunctionIdentifier(FLOOR);
        registerFunctionIdentifier(LN);
        registerFunctionIdentifier(POWER);
        registerFunctionIdentifier(ROUND);
        registerFunctionIdentifier(SIGN);

        registerFunctionIdentifier(LOCAL_DATE);
        registerFunctionIdentifier(LOCAL_TIME);
        registerFunctionIdentifier(LOCAL_DATETIME);

//        registerIdentifierRole("LOCAL", IdentifierRole.CLAUSE); // Part of LOCAL TIME
//        registerIdentifierRole("LOCAL_DATE", IdentifierRole.CLAUSE); // Part of LOCAL TIME

    }

    @Override
    public String toString() {
        return "JPQLGrammar 3.1";
    }

    // Register math function expression factory
    private void registerFunctionExpressionFactory(MathExpressionFactory factory, String identifier) {
        registerFactory(factory);
        addChildFactory(FunctionsReturningNumericsBNF.ID, identifier);
    }

    // Register role and version of math function identifier.
    private void registerFunctionIdentifier(String identifier) {
        registerIdentifierRole(identifier, IdentifierRole.FUNCTION);
        registerIdentifierVersion(identifier, JPAVersion.VERSION_3_1);
    }

}
