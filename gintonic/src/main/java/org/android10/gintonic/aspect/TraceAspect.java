/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package org.android10.gintonic.aspect;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.android10.gintonic.internal.DebugLog;
import org.android10.gintonic.internal.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect representing the cross cutting-concern: Method and Constructor Tracing.
 */
@Aspect
public class TraceAspect {

  private static Object currentObject = null;
  //进行类似于正则表达式的匹配，被匹配到的方法都会被截获
  ////截获任何包中以类名以Activity、Layout结尾，并且该目标类和当前类是一个Object的对象的所有方法
  private static final String PC_METHOD =
          "execution(* *..CoreApiClient+.*(..))";
  //精确截获MyFrameLayou的onMeasure方法
  private static final String PC_CALL = "call(* org.android10.viewgroupperformance.component.MyFrameLayout.onMeasure(..))";

  private static final String PC_METHOD_MAINACTIVITY = "execution(* *..OnClickListener.onClick(..))";

  //切点，ajc会将切点对应的Advise编织入目标程序当中
  @Pointcut(PC_METHOD)
  public void methodAnnotated() {}

  @Pointcut(PC_METHOD_MAINACTIVITY)
  public void methodAnootatedWith(){}


//  @Before("methodAnootatedWith()")
//  public void beforeMethodAnootatedWith(JoinPoint joinPoint) {
//    Log.e("before", "before->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
//  }



  private static final String POINTCUT_METHOD =
      "execution(@org.android10.gintonic.annotation.DebugTrace * *(..))";

  private static final String POINTCUT_CONSTRUCTOR =
      "execution(@org.android10.gintonic.annotation.DebugTrace *.new(..))";

  @Pointcut(POINTCUT_METHOD)
  public void methodAnnotatedWithDebugTrace() {}

  @Pointcut(POINTCUT_CONSTRUCTOR)
  public void constructorAnnotatedDebugTrace() {}



  @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace() || methodAnnotated() || methodAnootatedWith()")
  public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String className = methodSignature.getDeclaringType().getSimpleName()+"x";
    String methodName = methodSignature.getName();

    Object[] paramValues = joinPoint.getArgs();
    String[] paramNames = ((CodeSignature) joinPoint.getStaticPart()
            .getSignature()).getParameterNames();

    for(int i=0;i<paramNames.length;i++){
      Log.e("param",paramNames[i]+","+paramValues[i]);

    }
    if(methodName.contains("test")){
      paramValues[0]=20;
      joinPoint.proceed( paramValues);
    }

    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Object result = joinPoint.proceed();
    stopWatch.stop();

    DebugLog.log(className, buildLogMessage(methodName, stopWatch.getTotalTimeMillis()));

    return result;
  }

  /**
   * Create a log message.
   *
   * @param methodName A string with the method name.
   * @param methodDuration Duration of the method in milliseconds.
   * @return A string representing message.
   */
  private static String buildLogMessage(String methodName, long methodDuration) {
    StringBuilder message = new StringBuilder();
    message.append("Gintonic --> ");
    message.append(methodName);
    message.append(" --> ");
    message.append("[");
    message.append(methodDuration);
    message.append("ms");
    message.append("]");

    return message.toString();
  }
}
