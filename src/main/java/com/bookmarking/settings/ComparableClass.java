package com.bookmarking.settings;

import java.util.*;

/**
 * A class to use for comparing class objects.
 */
public class ComparableClass implements Comparable<ComparableClass>
{
        private Class clazz;

        public ComparableClass(Class clazz)
        {
            this.clazz = clazz;
        }

        public Class getClazz()
        {
            return clazz;
        }

        public void setClazz(Class clazz)
        {
            this.clazz = clazz;
        }

        @Override
        public int compareTo(ComparableClass o)
        {
            return clazz.getCanonicalName().compareTo(o.getClazz().getCanonicalName());
        }

        public static List<Class> sortClassList(Collection<Class> collection)
        {
            List<ComparableClass> classList = new ArrayList<>();

            for (Class clazz: collection)
            {
                classList.add(new ComparableClass(clazz));
            }

            Collections.sort(classList);

            List<Class> res = new ArrayList<>();

            for (ComparableClass comparableClass: classList)
            {
                res.add(comparableClass.getClazz());
            }

            return res;
        }
}
