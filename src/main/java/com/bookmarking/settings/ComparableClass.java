package com.bookmarking.settings;

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
}
