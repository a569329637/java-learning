package com.gsq.jvm.file.processor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic.Kind;
import java.util.EnumSet;

/**
 * @author guishangquan
 * @date 2018/10/10
 */
public class NameChecker {

    private final Messager messager;

    private NameCheckScan nameCheckScan = new NameCheckScan();

    public NameChecker(ProcessingEnvironment processingEnv) {
        messager = processingEnv.getMessager();
    }

    public void checkNames(Element element) {
        nameCheckScan.scan(element);
    }

    private class NameCheckScan extends ElementScanner8<Void, Void> {

        @Override
        public Void visitVariable(VariableElement e, Void p) {
            if (e.getKind() == ElementKind.ENUM_CONSTANT || e.getConstantValue() != null || heuristicallyConstant(e)) {
                checkAllCaps(e);
            } else {
                checkCamelCase(e, false);
            }
            return super.visitVariable(e, p);
        }

        @Override
        public Void visitType(TypeElement e, Void p) {
            scan(e, p);
            checkCamelCase(e, true);
            return super.visitType(e, p);
        }

        @Override
        public Void visitExecutable(ExecutableElement e, Void p) {
            if (e.getKind() == ElementKind.METHOD) {
                Name name = e.getSimpleName();
                if (name.contentEquals(e.getEnclosingElement().getSimpleName())) {
                    messager.printMessage(Kind.WARNING, "一个普通方法:" + name + " 不应当与类名重复，避免与构造函数产生混淆", e);
                    checkCamelCase(e, false);
                }
            }
            return super.visitExecutable(e, p);
        }

        private void checkCamelCase(Element e, boolean initialCaps) {
            String name = e.getSimpleName().toString();
            boolean previousUpper = false;
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if (Character.isUpperCase(firstCodePoint)) {
                previousUpper = true;
                if (!initialCaps) {
                    messager.printMessage(Kind.WARNING, "名称：" + name + " 应当已小写字符开头", e);
                    return;
                }
            } else if (Character.isLowerCase(firstCodePoint)) {
                if (initialCaps) {
                    messager.printMessage(Kind.WARNING, "名称:" + name + " 应当已大写字母开否", e);
                    return;
                }
            } else {
                conventional = false;
            }
            if (conventional) {
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (Character.isUpperCase(cp)) {
                        if (previousUpper) {
                            conventional = false;
                            break;
                        }
                        previousUpper = true;
                    } else {
                        previousUpper = false;
                    }
                }
            }
            if (!conventional) {
                messager.printMessage(Kind.WARNING, "名称:" + name + "应当符合驼式命名法（Camel Case Names）", e);
            }
        }

        private void checkAllCaps(VariableElement e) {
            String name = e.getSimpleName().toString();
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if (!Character.isUpperCase(firstCodePoint)) {
                conventional = false;
            } else {
                boolean previousUnderscore = false;
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (cp == (int) '_') {
                        if (previousUnderscore) {
                            conventional = false;
                            break;
                        }
                        previousUnderscore = true;
                    } else {
                        previousUnderscore = false;
                        if (!Character.isUpperCase(cp) && !Character.isDigit(cp)) {
                            conventional = false;
                            break;
                        }
                    }

                }
            }
            if (!conventional) {
                messager.printMessage(Kind.WARNING, "常量:" + name + " 应该全部以大写字母" + "或下划线命名，并且以字符开否", e);
            }
        }

        private boolean heuristicallyConstant(VariableElement e) {
            if (e.getEnclosingElement().getKind() == ElementKind.INTERFACE) {
                return true;
            } else if (e.getKind() == ElementKind.FIELD && e.getModifiers().containsAll(EnumSet.of(javax.lang.model.element.Modifier.FINAL, javax.lang.model.element.Modifier.STATIC, javax.lang.model.element.Modifier.PUBLIC))) {
                return true;
            }
            return false;
        }
    }
}
