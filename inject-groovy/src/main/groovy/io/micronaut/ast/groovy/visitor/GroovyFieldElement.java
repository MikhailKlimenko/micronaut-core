/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.ast.groovy.visitor;

import io.micronaut.ast.groovy.utils.AstAnnotationUtils;
import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.FieldElement;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.control.SourceUnit;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;

/**
 * A field element returning data from a {@link Variable}. The
 * variable could be a field or property node.
 *
 * @author James Kleeh
 * @since 1.0
 */
public class GroovyFieldElement extends AbstractGroovyElement implements FieldElement {

    private final Variable variable;
    private final SourceUnit sourceUnit;

    /**
     * @param sourceUnit         the source unit
     * @param variable           The {@link Variable}
     * @param annotatedNode       The annotated ndoe
     * @param annotationMetadata The annotation medatada
     */
    GroovyFieldElement(
            SourceUnit sourceUnit,
            Variable variable, AnnotatedNode annotatedNode, AnnotationMetadata annotationMetadata) {
        super(sourceUnit, annotatedNode, annotationMetadata);
        this.variable = variable;
        this.sourceUnit = sourceUnit;
    }

    @Override
    public String getName() {
        return variable.getName();
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(variable.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(variable.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(variable.getModifiers());
    }

    @Override
    public boolean isPrivate() {
        return Modifier.isPrivate(variable.getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(variable.getModifiers());
    }

    @Override
    public boolean isProtected() {
        return Modifier.isProtected(variable.getModifiers());
    }

    @Override
    public Object getNativeType() {
        return variable;
    }

    @Nullable
    @Override
    public ClassElement getType() {
        return new GroovyClassElement(sourceUnit, variable.getType(), AstAnnotationUtils.getAnnotationMetadata(sourceUnit, variable.getType()));
    }

    @Override
    public ClassElement getDeclaringType() {
        ClassNode declaringClass = null;
        if (variable instanceof FieldNode) {
            FieldNode fn = (FieldNode) variable;
            declaringClass = fn.getDeclaringClass();
        } else if (variable instanceof PropertyNode) {
            PropertyNode pn = (PropertyNode) variable;
            declaringClass = pn.getDeclaringClass();
        }

        if (declaringClass == null) {
            throw new IllegalStateException("Declaring class could not be established");
        }

        return new GroovyClassElement(
                sourceUnit,
                declaringClass,
                AstAnnotationUtils.getAnnotationMetadata(
                        sourceUnit,
                        declaringClass
                )
        );
    }
}
