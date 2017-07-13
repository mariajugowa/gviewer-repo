# I know what I'm doing
-dontwarn **


-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable


# RxJava configuration
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* 
{
    long producerIndex;
    long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef 
{
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef 
{
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# Retrofit configuration
-keepattributes Signature,Exceptions
-keepclasseswithmembers class * 
{
    @retrofit2.http.* <methods>;
}


# Preserve entities
-keep class ru.geekbrains.gviewer.model.entity.** { *; }
