package com.studentportal.announcement;

import java.util.ArrayList;

public class Originator {

    public Originator() {
        super();
    }

    //Create memento and return a memento obj 备忘操作，并且返回备忘录对象
    public Memento createMemento(ArrayList<String> message_in_word) {
        return new MementoConcrete(message_in_word);
    }

    //recovery the data to the value in memento 进行数据恢复，数据恢复成指定的备忘录的值
    public String Undo(Memento m) {
        return ((MementoConcrete) m).getState().toString();
    }

    // 备忘录
    class MementoConcrete implements Memento {

        private ArrayList<String> Message_in_world;

        public MementoConcrete(ArrayList<String> Message_in_world) {
            super();
            this.Message_in_world = Message_in_world;
        }

        public ArrayList<String> getState() {
            return Message_in_world;
        }

        public void setState(ArrayList<String> message_in_world) {
            Message_in_world = message_in_world;
        }
    }
}

