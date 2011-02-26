/*******************************************************************************
 * Copyright (c) 2011 Codehaus.org, SpringSource, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Andrew Eisenberg - Initial implemenation
 *******************************************************************************/
package org.codehaus.groovy.eclipse.dsl.pointcuts.impl;

import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.eclipse.dsl.pointcuts.GroovyDSLDContext;
import org.objectweb.asm.Opcodes;

/**
 * the match returns true if the pattern passed in has a modifier of the kind specified
 * @author andrew
 * @created Feb 11, 2011
 */
public class AbstractModifierPointcut extends FilteringPointcut<AnnotatedNode> {
    
    public static class FinalPointcut extends AbstractModifierPointcut {
        public FinalPointcut(String containerIdentifier) {
            super(containerIdentifier, Opcodes.ACC_FINAL);
        }
    }

    public static class StaticPointcut extends AbstractModifierPointcut {
        public StaticPointcut(String containerIdentifier) {
            super(containerIdentifier, Opcodes.ACC_STATIC);
        }
    }
    
    public static class PublicPointcut extends AbstractModifierPointcut {
        public PublicPointcut(String containerIdentifier) {
            super(containerIdentifier, Opcodes.ACC_PUBLIC);
        }
    }
    
    public static class PrivatePointcut extends AbstractModifierPointcut {
        public PrivatePointcut(String containerIdentifier) {
            super(containerIdentifier, Opcodes.ACC_PRIVATE);
        }
    }
    
    public static class SynchronizedPointcut extends AbstractModifierPointcut {
        public SynchronizedPointcut(String containerIdentifier) {
            super(containerIdentifier, Opcodes.ACC_SYNCHRONIZED);
        }
    }
    

    private final int modifier;
    
    public AbstractModifierPointcut(String containerIdentifier, int modifier) {
        super(containerIdentifier, AnnotatedNode.class);
        this.modifier = modifier;
    }

    /**
     * filters the passed in object based on the modifier
     * @param result
     * @return
     */
    @Override
    protected AnnotatedNode filterObject(AnnotatedNode result, GroovyDSLDContext pattern, String firstArgAsString) {
        boolean success = false;
        if (result instanceof ClassNode) {
            success = (((ClassNode) result).getModifiers() & modifier) != 0;
        } else if (result instanceof FieldNode) {
            success = (((FieldNode) result).getModifiers() & modifier) != 0;
        } else if (result instanceof MethodNode) {
            success = (((MethodNode) result).getModifiers() & modifier) != 0;
        }
        return success ? result : null;
    }
    
    @Override
    public String verify() {
        return getArgumentValues().length > 0 ? "This pointcut does not take any arguments." : null;
    }
}