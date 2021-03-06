/*
 * Copyright 2009-2016 the original author or authors.
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
package org.codehaus.groovy.eclipse.codebrowsing.tests;

import static java.util.Arrays.asList;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.SourceRange;
import org.eclipse.jdt.internal.core.BinaryMember;
import org.eclipse.jdt.internal.core.BinaryMethod;

/**
 * @author Andrew Eisenberg
 * @created Jan 21, 2012
 */
public final class CodeSelectStaticImportsTests extends BrowsingTestCase {

    public static junit.framework.Test suite() {
        return newTestSuite(CodeSelectStaticImportsTests.class);
    }

    public void testCodeSelectOnStaticImport1() {
        String one = "class Other {\n  public static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.FOO";
        assertCodeSelect(asList(one, two), "FOO");
    }

    public void testCodeSelectOnStaticImport1a() {
        String one = "class Other {\n  static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.FOO";
        assertCodeSelect(asList(one, two), "FOO");
    }

    public void testCodeSelectOnStaticImport2() {
        String one = "class Other {\n  public static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.BAR";
        assertCodeSelect(asList(one, two), "BAR");
    }

    public void testCodeSelectOnStaticImport3() {
        String one = "class Other {\n  public static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.FOO\nFOO";
        assertCodeSelect(asList(one, two), "FOO");
    }

    public void testCodeSelectOnStaticImport3a() {
        String one = "class Other {\n  static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.FOO\nFOO";
        assertCodeSelect(asList(one, two), "FOO");
    }

    public void testCodeSelectOnStaticImport4() {
        String one = "class Other {\n  public static int FOO\n static boolean BAR() { } }";
        String two = "import static Other.BAR\nBAR";
        assertCodeSelect(asList(one, two), "BAR");
    }

    public void testCodeSelectOnStaticImport5() {
        String source = "import static java.util.regex.Pattern.compile\n" + "def p = compile('123')";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("compile"), 7), "compile");
        assertTrue(elem instanceof BinaryMethod);
    }

    public void testCodeSelectOnStaticImport6() {
        String source = "import static java.lang.Thread.State.BLOCKED\n" + "def st = BLOCKED";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("BLOCKED"), 7), "BLOCKED");
        assertTrue(elem instanceof BinaryMember);
    }

    public void testCodeSelectOnStaticImportPackage() {
        String source = "import static java.util.regex.Pattern.compile\n" + "def p = compile('123')";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("regex"), 5), "java.util.regex");
        assertTrue(elem instanceof IPackageFragment);
    }

    public void testCodeSelectOnStaticImportPackage2() {
        String source = "import static java.util.regex.Pattern.*\n" + "def p = compile('123')";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("regex"), 5), "java.util.regex");
        assertTrue(elem instanceof IPackageFragment);
    }

    public void testCodeSelectOnStaticImportPackage3() {
        String source = "import static java.lang.Thread.State.BLOCKED\n" + "def st = BLOCKED";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("lang"), 5), "java.lang");
        assertTrue(elem instanceof IPackageFragment);
    }

    public void testCodeSelectOnStaticImportPackage4() {
        String source = "import static java.lang.Thread.State.*\n" + "def st = BLOCKED";

        IJavaElement elem = assertCodeSelect(source, new SourceRange(source.indexOf("lang"), 5), "java.lang");
        assertTrue(elem instanceof IPackageFragment);
    }
}
