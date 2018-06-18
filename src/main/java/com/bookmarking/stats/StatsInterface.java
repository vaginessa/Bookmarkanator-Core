package com.bookmarking.stats;

public interface StatsInterface<K,C, V>
{
    V getStat(K key, C config);
}
