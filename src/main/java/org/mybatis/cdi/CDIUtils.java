/**
 *    Copyright 2013-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.cdi;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author Frank D. Martinez [mnesarco]
 */
public final class CDIUtils {

  private CDIUtils() {
      // this class cannot be instantiated
  }

  public static SqlSessionManagerRegistry getRegistry(BeanManager beanManager, CreationalContext creationalContext) {
    Iterator<Bean<?>> beans = beanManager.getBeans(SqlSessionManagerRegistry.class).iterator();
    return (SqlSessionManagerRegistry) beanManager.getReference(beans.next(), SqlSessionManagerRegistry.class, creationalContext);
  }

  public static SqlSessionFactory findSqlSessionFactory(String name, Set<Annotation> qualifiers, BeanManager beanManager, CreationalContext creationalContext) {
    Set<Bean<?>> beans;
    if (name != null) {
      beans = beanManager.getBeans(name);
    } else {
      beans = beanManager.getBeans(SqlSessionFactory.class, qualifiers.toArray(new Annotation[]{}));
    }
    Bean bean = beanManager.resolve(beans);
    if (bean == null) {
      throw new MybatisCdiConfigurationException("There are no SqlSessionFactory producers properly configured.");
    }
    return (SqlSessionFactory) beanManager.getReference(bean, SqlSessionFactory.class, creationalContext);
  }

  public static class SerializableDefaultAnnotationLiteral extends AnnotationLiteral<Default> {
    private static final long serialVersionUID = 1L;
  }

  public static class SerializableAnyAnnotationLiteral extends AnnotationLiteral<Any> {
    private static final long serialVersionUID = 1L;
  }

}
