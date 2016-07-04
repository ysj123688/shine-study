package com.bazinga.shine.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class Reference {
    
    ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
    
    SoftReference<Object> objs = new SoftReference<Object>(new Object(), queue);
    

}
