package net.typho.big_shot_lib.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Arrays;

public class BigShotLibAgent {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach(args[0]);

        try {
            vm.loadAgent(args[1]);
        } finally {
            vm.detach();
        }
    }

    public static void premain(String args, Instrumentation inst) throws UnmodifiableClassException {
        init(inst);
    }

    public static void agentmain(String args, Instrumentation inst) throws UnmodifiableClassException {
        init(inst);
    }

    public static void init(Instrumentation inst) throws UnmodifiableClassException {
        Class<?> agentCls = Arrays.stream(inst.getAllLoadedClasses())
                .filter(c -> c.getName().equals("net.typho.big_shot_lib.api.AgentLoader"))
                .findAny()
                .orElseThrow(() -> new AssertionError("Class net.typho.big_shot_lib.api.AgentLoader was not loaded when the Big Shot Lib agent was launched, this should never happen."));

        try {
            agentCls.getField("INSTRUMENTATION").set(null, inst);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
