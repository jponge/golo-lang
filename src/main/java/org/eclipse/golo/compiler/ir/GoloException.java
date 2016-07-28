/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.golo.compiler.ir;

import org.eclipse.golo.compiler.PackageAndClass;
import org.eclipse.golo.compiler.parser.GoloASTNode;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public final class GoloException extends GoloElement {

  private PackageAndClass moduleName;
  private final String name;
  private final Set<String> members = new LinkedHashSet<>();

  @Override
  public GoloException ofAST(GoloASTNode node) {
    super.ofAST(node);
    return this;
  }

  GoloException(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public GoloException members(String... members) {
    return this.members(asList(members));
  }

  public GoloException members(Collection<String> members) {
    members.forEach(this::addMember);
    return this;
  }

  public void addMember(String name) {
    this.members.add(name);
  }

  public PackageAndClass getPackageAndClass() {
    return new PackageAndClass(moduleName.toString() + ".types", name);
  }

  public void setModuleName(PackageAndClass module) {
    this.moduleName = module;
  }

  public Set<String> getMembers() {
    return Collections.unmodifiableSet(members);
  }

  @Override
  public void accept(GoloIrVisitor visitor) {
    visitor.visitGoloException(this);
  }

  @Override
  public void walk(GoloIrVisitor visitor) {
  }

  @Override
  protected void replaceElement(GoloElement original, GoloElement newElement) {
    throw cantReplace();
  }
}
