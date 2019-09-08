package com.studentportal.announcement;

import java.util.Stack;

public class CareTaker {

    private Stack<Memento> stack_memento;

    public CareTaker() {
        super();
        stack_memento = new Stack<Memento>();
    }

    public Memento getLastMemento() {
        if (stack_memento.isEmpty()) {
            return null;
        } else
            return stack_memento.pop();
    }

    public void saveMemento(Memento memento) {
        System.err.println("save pos :" + memento.toString());
        stack_memento.push(memento);
    }

}
