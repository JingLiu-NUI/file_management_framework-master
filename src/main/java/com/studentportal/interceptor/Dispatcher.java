package com.studentportal.interceptor;

import java.util.ArrayList;
import java.util.List;

//call back concrete interceptor
//attach interceptor object
public class Dispatcher {
    List<I_Intercepter> concreteIntercepters;

    public Dispatcher() {
        concreteIntercepters = new ArrayList<I_Intercepter>();
    }

    // register concrete interceptor
    public void attach(I_Intercepter ob) {
        concreteIntercepters.add(ob);
    }

    // find the related concreteinterceptor and notify it
    //iterate_list();
    public void notifyObsevers(ContextFile fileCtx) {
        for (I_Intercepter o : concreteIntercepters) {
            o.interceptExecute(fileCtx);
        }
    }
}
