/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.golo.compiler;

import org.eclipse.golo.compiler.ir.GoloException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

class JavaBytecodeExceptionGenerator {

  private final String PARENT_EXCEPTION = "java/lang/RuntimeException";

  public CodeGenerationResult compile(GoloException exception, String sourceFilename) {
    ClassWriter classWriter = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
    classWriter.visitSource(sourceFilename, null);
    classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER | ACC_FINAL,
      exception.getPackageAndClass().toJVMType(), null, PARENT_EXCEPTION, null);
    makeFields(classWriter, exception);
    makeConstructors(classWriter, exception);
    classWriter.visitEnd();
    return new CodeGenerationResult(classWriter.toByteArray(), exception.getPackageAndClass());
  }

  private void makeFields(ClassWriter classWriter, GoloException exception) {
    for (String name : exception.getMembers()) {
      FieldVisitor fieldVisitor = classWriter.visitField(ACC_PRIVATE, name, "Ljava/lang/Object;", null, null);
      fieldVisitor.visitEnd();
    }
  }

  private void makeConstructors(ClassWriter classWriter, GoloException exception) {
    makeNoArgsConstructor(classWriter, exception);
  }

  private void makeNoArgsConstructor(ClassWriter classWriter, GoloException exception) {
    String owner = exception.getPackageAndClass().toJVMType();
    String signature = "(" +
      IntStream.range(0, exception.getMembers().size())
        .mapToObj(i -> "Ljava/lang/Object;")
        .collect(Collectors.joining())
      + ")V";
    MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, "<init>", signature, null, null);
    mv.visitCode();
    mv.visitMethodInsn(INVOKESPECIAL, PARENT_EXCEPTION, "<init>", "()V", false);

    mv.visitMaxs(0, 0);
    mv.visitEnd();
  }
}
