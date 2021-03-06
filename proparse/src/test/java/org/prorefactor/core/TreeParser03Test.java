/********************************************************************************
 * Copyright (c) 2003-2015 John Green
 * Copyright (c) 2015-2020 Riverside Software
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU Lesser General Public License v3.0
 * which is available at https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-3.0
 ********************************************************************************/
package org.prorefactor.core;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.prorefactor.core.util.UnitTestModule;
import org.prorefactor.refactor.RefactorSession;
import org.prorefactor.treeparser.DataType;
import org.prorefactor.treeparser.Parameter;
import org.prorefactor.treeparser.ParseUnit;
import org.prorefactor.treeparser.symbols.Routine;
import org.prorefactor.treeparser.symbols.Symbol;
import org.prorefactor.treeparser.symbols.Variable;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class simply runs the tree parser through various code, and as long as the tree parser does not throw any
 * errors, then the tests pass.
 */
public class TreeParser03Test {
  private RefactorSession session;

  @BeforeTest
  public void setUp() {
    Injector injector = Guice.createInjector(new UnitTestModule());
    session = injector.getInstance(RefactorSession.class);
  }

  @Test
  public void test01() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test01.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
  }

  @Test
  public void test02() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test02.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
  }

  @Test
  public void testTreeParser01() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test03.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    boolean found1 = false;
    boolean found2 = false;
    for (JPNode node : unit.getTopNode().query(ABLNodeType.DEFINE)) {
      if ((node.getState2() == ABLNodeType.TEMPTABLE.getType())
          && "myTT2".equals(node.nextNode().nextNode().getText())) {
        assertEquals(node.query(ABLNodeType.USEINDEX).get(0).nextNode().attrGet(IConstants.INVALID_USEINDEX),
            IConstants.TRUE);
        found1 = true;
      }
      if ((node.getState2() == ABLNodeType.TEMPTABLE.getType())
          && "myTT3".equals(node.nextNode().nextNode().getText())) {
        assertEquals(node.query(ABLNodeType.USEINDEX).get(0).nextNode().attrGet(IConstants.INVALID_USEINDEX), 0);
        found2 = true;
      }
    }
    assertTrue(found1);
    assertTrue(found2);
  }

  @Test
  public void test04() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test04.cls"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable xx = unit.getRootScope().getVariable("xx");
    assertNotNull(xx);
    Variable yy = unit.getRootScope().getVariable("yy");
    assertNotNull(yy);
    Variable zz = unit.getRootScope().getVariable("zz");
    assertNotNull(zz);
  }

  @Test
  public void test05() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test05.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    Routine f1 = unit.getRootScope().lookupRoutine("f1");
    assertNotNull(f1);
    assertEquals(f1.getParameters().size(), 1);
    assertEquals(f1.getParameters().get(0).getSymbol().getName(), "zz");
    assertEquals(f1.getParameters().get(0).getSymbol().getNumReads(), 1);
    assertEquals(f1.getParameters().get(0).getSymbol().getNumWrites(), 0);

    Routine f2 = unit.getRootScope().lookupRoutine("f2");
    assertNotNull(f2);
    assertEquals(f2.getParameters().size(), 2);
    assertEquals(f2.getParameters().get(0).getSymbol().getName(), "a");
    assertEquals(f2.getParameters().get(0).getSymbol().getNumReads(), 0);
    assertEquals(f2.getParameters().get(0).getSymbol().getNumWrites(), 0);
    assertEquals(f2.getParameters().get(1).getSymbol().getName(), "zz");
    assertEquals(f2.getParameters().get(1).getSymbol().getNumReads(), 1);
    assertEquals(f2.getParameters().get(1).getSymbol().getNumWrites(), 0);

    Routine f3 = unit.getRootScope().lookupRoutine("f3");
    assertNotNull(f3);
    assertEquals(f3.getParameters().size(), 1);
    assertEquals(f3.getParameters().get(0).getSymbol().getName(), "a");
    assertEquals(f3.getParameters().get(0).getSymbol().getNumReads(), 1);
    assertEquals(f3.getParameters().get(0).getSymbol().getNumWrites(), 0);

    Routine f4 = unit.getRootScope().lookupRoutine("f4");
    assertNotNull(f4);
    assertEquals(f4.getParameters().size(), 0);

    Routine f5 = unit.getRootScope().lookupRoutine("f5");
    assertNotNull(f5);
    assertEquals(f5.getParameters().size(), 0);
  }

  @Test
  public void test06() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test06.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
  }

  @Test
  public void test07() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test07.cls"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable prop = unit.getRootScope().getVariable("cNextSalesRepName");
    assertNotNull(prop);
    assertEquals(prop.getNumReads(), 1);
    assertEquals(prop.getNumWrites(), 0);
  }

  @Test
  public void test08() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test08.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable xx = unit.getRootScope().getChildScopes().get(0).getVariable("xx");
    assertNotNull(xx);
    assertEquals(xx.getNumReads(), 1);
    assertEquals(xx.getNumWrites(), 0);
  }

  @Test
  public void test09() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test09.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable xxx = unit.getRootScope().getVariable("xxx");
    assertNotNull(xxx);
  }

  @Test
  public void test11() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test11.cls"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Routine r1 = unit.getRootScope().getRoutineMap().get("foo1");
    assertNotNull(r1);
    assertEquals(r1.getParameters().size(), 1);
    Parameter p1 = r1.getParameters().get(0);
    Symbol s1 = p1.getSymbol();
    assertNotNull(s1);
    assertEquals(s1.getName(), "ipPrm");
    assertTrue(s1 instanceof Variable);
    assertEquals(((Variable) s1).getDataType(), DataType.INTEGER);
    assertNotNull(s1.getDefineNode());
  }

  @Test
  public void test10() {
    ParseUnit unit = new ParseUnit(new ByteArrayInputStream("define input parameter ipPrm no-undo like customer.custnum.".getBytes()), "<unnamed>", session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable ipPrm = unit.getRootScope().getVariable("ipPrm");
    assertNotNull(ipPrm);
    assertEquals(ipPrm.getDataType(), DataType.INTEGER);
  }

  @Test
  public void test12() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test12.cls"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Routine r1 = unit.getRootScope().getRoutineMap().get("foo1");
    assertEquals(r1.getReturnDatatypeNode(), DataType.CLASS);
    Routine r2 = unit.getRootScope().getRoutineMap().get("foo2");
    assertEquals(r2.getReturnDatatypeNode(), DataType.CLASS);
    Routine r3 = unit.getRootScope().getRoutineMap().get("foo3");
    assertEquals(r3.getReturnDatatypeNode(), DataType.INTEGER);
    Routine r4 = unit.getRootScope().getRoutineMap().get("foo4");
    assertEquals(r4.getReturnDatatypeNode(), DataType.CHARACTER);
  }

  @Test
  public void test13() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test13.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());
    Variable xxx = unit.getRootScope().getVariable("xxx");
    assertNotNull(xxx);
    assertEquals(xxx.getNumReads(), 1);
    assertEquals(xxx.getNumWrites(), 0);
    Variable yyy = unit.getRootScope().getVariable("yyy");
    assertNotNull(yyy);
    assertEquals(yyy.getNumReads(), 0);
    assertEquals(yyy.getNumWrites(), 1);
  }

  @Test
  public void test14() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test14.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    Variable xxx = unit.getRootScope().getVariable("xxx");
    assertNotNull(xxx);
    assertEquals(xxx.getNumReads(), 1);
    assertEquals(xxx.getNumWrites(), 3);
  }

  @Test
  public void test15() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test15.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    assertEquals(unit.getRootScope().getVariables().size(), 2);
    Variable v1 = unit.getRootScope().getVariable("v1");
    Variable v2 = unit.getRootScope().getVariable("v2");
    assertNotNull(v1);
    assertNotNull(v2);
    Routine dummy = unit.getRootScope().getRoutineMap().get("dummy");
    assertNotNull(dummy);
    assertEquals(dummy.getParameters().size(), 1);
    assertEquals(dummy.getParameters().get(0).getSymbol().getName(), "p1");
  }

  @Test
  public void test16() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test16.cls"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    assertEquals(unit.getRootScope().getVariables().size(), 1);
    Variable hInstance = unit.getRootScope().getVariable("hInstance");
    assertNotNull(hInstance);

    Routine dummy = unit.getRootScope().getRoutineMap().get("dummy");
    assertNotNull(dummy);
    assertEquals(dummy.getParameters().size(), 1);
    assertEquals(dummy.getParameters().get(0).getSymbol().getName(), "picVariable");

    Routine doIt = unit.getRootScope().getRoutineMap().get("doit");
    assertNotNull(doIt);
    assertEquals(doIt.getParameters().size(), 1);
    assertEquals(doIt.getParameters().get(0).getSymbol().getName(), "picVariable");

    // Should not be the same object
    assertNotEquals(dummy.getParameters().get(0), doIt.getParameters().get(0));
    assertNotEquals(dummy.getParameters().get(0).getSymbol(), doIt.getParameters().get(0).getSymbol());
  }

  @Test
  public void test17() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test17.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    assertEquals(unit.getRootScope().getVariables().size(), 4);

    Variable hMenuItem = unit.getRootScope().getVariable("hMenuItem");
    assertNotNull(hMenuItem);
    assertTrue(hMenuItem.isGraphicalComponent());
    Variable hQuery = unit.getRootScope().getVariable("hQuery");
    assertNotNull(hQuery);
    assertFalse(hQuery.isGraphicalComponent());
    Variable hbCust = unit.getRootScope().getVariable("hbCust");
    assertNotNull(hbCust);
    assertTrue(hbCust.isGraphicalComponent());
    Variable hSock = unit.getRootScope().getVariable("hSock");
    assertNotNull(hSock);
    assertFalse(hSock.isGraphicalComponent());
  }

  @Test
  public void test18() {
    ParseUnit unit = new ParseUnit(new File("src/test/resources/treeparser03/test18.p"), session);
    assertNull(unit.getTopNode());
    unit.treeParser01();
    assertNotNull(unit.getTopNode());
    assertNotNull(unit.getRootScope());

    assertEquals(unit.getRootScope().getVariables().size(), 11);
    Variable var1 = unit.getRootScope().getVariable("prm1");
    assertEquals(var1.getDataType(), DataType.INTEGER);
    Variable var2 = unit.getRootScope().getVariable("prm2");
    assertEquals(var2.getDataType(), DataType.INTEGER);
    Variable var3 = unit.getRootScope().getVariable("prm3");
    assertEquals(var3.getDataType(), DataType.INTEGER);
    Variable var4 = unit.getRootScope().getVariable("prm4");
    assertEquals(var4.getDataType(), DataType.INTEGER);
    Variable var5 = unit.getRootScope().getVariable("prm5");
    assertEquals(var5.getDataType(), DataType.INTEGER);
    Variable var6 = unit.getRootScope().getVariable("prm6");
    assertEquals(var6.getDataType(), DataType.CHARACTER);
    Variable var7 = unit.getRootScope().getVariable("prm7");
    assertEquals(var7.getDataType(), DataType.LONGCHAR);
    Variable var8 = unit.getRootScope().getVariable("prm8");
    assertEquals(var8.getDataType(), DataType.HANDLE);
    Variable var9 = unit.getRootScope().getVariable("prm9");
    assertEquals(var9.getDataType(), DataType.CLASS);
    assertEquals(var9.getClassName(), "Progress.Lang.Object");
    Variable var10 = unit.getRootScope().getVariable("prm10");
    assertEquals(var10.getDataType(), DataType.CLASS);
    assertEquals(var10.getClassName(), "Progress.Lang.Object");
    Variable var11 = unit.getRootScope().getVariable("prm11");
    assertEquals(var11.getDataType(), DataType.CLASS);
    assertEquals(var9.getClassName(), "Progress.Lang.Object");
  }
}
